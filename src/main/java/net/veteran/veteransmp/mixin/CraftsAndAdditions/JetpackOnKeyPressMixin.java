package net.veteran.veteransmp.mixin.CraftsAndAdditions;

import net.mcreator.createstuffadditions.procedures.FlyingOnKeyPressedProcedure;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.veteran.veteransmp.VeteranSMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(FlyingOnKeyPressedProcedure.class)
public class JetpackOnKeyPressMixin {

    @Inject(method = "execute", at = @At("RETURN"))
    private static void onExecute(Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity living) {
            AtomicBoolean hasJetpack = new AtomicBoolean(false);

            ItemStack chestItem = living.getItemBySlot(EquipmentSlot.CHEST);
            if (chestItem.is(ItemTags.create(ResourceLocation.parse("create_sa:jetpack")))) {
                hasJetpack.set(true);
            }

            if (!hasJetpack.get()) {
                CuriosApi.getCuriosInventory(living).ifPresent(curiosInventory -> {
                    Map<String, ICurioStacksHandler> curios = curiosInventory.getCurios();
                    for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                        ICurioStacksHandler handler = entry.getValue();
                        for (int i = 0; i < handler.getSlots(); i++) {
                            ItemStack stackInSlot = handler.getStacks().getStackInSlot(i);
                            if (stackInSlot.is(ItemTags.create(ResourceLocation.parse("create_sa:jetpack")))) {
                                hasJetpack.set(true);
                            }
                        }
                    }
                });
            }

            if (hasJetpack.get()) {
                living.getPersistentData().putBoolean("CsaFlying", true);
            }
        }
    }
}
