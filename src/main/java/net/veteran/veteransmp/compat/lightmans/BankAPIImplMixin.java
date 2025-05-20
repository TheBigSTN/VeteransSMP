package net.veteran.veteransmp.compat.lightmans;

import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.IBankAccount;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.builtin.PlayerBankReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.builtin.TeamBankReference;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.teams.ITeam;
import io.github.lightman314.lightmanscurrency.api.teams.TeamAPI;
import io.github.lightman314.lightmanscurrency.common.data.types.BankDataCache;
import io.github.lightman314.lightmanscurrency.common.impl.BankAPIImpl;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.veteran.veteransmp.VConfig;
import net.veteran.veteransmp.compat.PlayerTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.UUID;

@Mixin(BankAPIImpl.class)
public class BankAPIImplMixin {

    @Inject(method = "ServerTick", at = @At("HEAD"), cancellable = true)
    private void onServerTick(ServerTickEvent.Pre event, CallbackInfo ci) {
        if (VConfig.Server.OVERRIDE_LC.get()) {
            double interestRate = LCConfig.SERVER.bankAccountInterestRate.get();
            if (interestRate <= 0.0F) {
                return;
            }

            long interest = PlayerTracker.timeSinceLastInterest();
            if (interest < LCConfig.SERVER.bankAccountInterestTime.get()) {
                return;
            }

            PlayerTracker.setLastInterestTime();
            LightmansCurrency.LogDebug("Applying interest to ACTIVE bank accounts!");

            BankDataCache data = BankDataCache.TYPE.get(false);
            assert data != null;

            List<MoneyValue> limits = LCConfig.SERVER.bankAccountInterestLimits.get();
            boolean forceInterest = LCConfig.SERVER.bankAccountForceInterest.get();
            boolean notifyPlayers = LCConfig.SERVER.bankAccountInterestNotification.get();

            for (BankReference reference : ((BankAPIImpl)(Object)this).GetAllBankReferences(false)) {
                IBankAccount account = reference.get();
                if (account != null) {
                    if (reference instanceof PlayerBankReference playerRef) {
                        PlayerReference player = playerRef.getPlayer();
                        assert player != null;
                        UUID playerUUID = player.id;
                        long lastLogin = PlayerTracker.get().getPlayer(playerUUID);
                        long daysSince = lastLogin / VConfig.Server.OFFLINE_LIMIT.getAsLong();
                        if (daysSince <= 7) {
                            account.applyInterest(interestRate, limits, forceInterest, notifyPlayers);
                            LightmansCurrency.LogDebug("Applying interest to " + account.getName().getString());
                        } else {
                            LightmansCurrency.LogDebug("Skipping interest for " + account.getName().getString() + " - inactive.");
                        }
                    } else if (reference instanceof TeamBankReference teamRef) {
                        long teamID = teamRef.teamID;
                        ITeam team = TeamAPI.API.GetTeam(false, teamID);
                        assert team != null;


                        boolean interestApplied = false;
                        for (PlayerReference playerReference : team.getAllMembers()) {
                            UUID playerUUID = playerReference.id;
                            long lastLogin = PlayerTracker.get().getPlayer(playerUUID);
                            long daysSince = lastLogin / VConfig.Server.OFFLINE_LIMIT.getAsLong();
                            if (daysSince <= 7) {
                                LightmansCurrency.LogDebug("Applying interest to " + account.getName().getString());
                                account.applyInterest(interestRate, limits, forceInterest, notifyPlayers);
                                interestApplied = true;
                                break;
                            }
                        }
                        if (!interestApplied) {
                            LightmansCurrency.LogDebug("Skipping interest for " + account.getName().getString() + " - no active members in last 7 days.");
                        }
                    }
                }
            }
            ci.cancel();
        }
    }
}
