package net.veteran.veteransmp.compat;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.veteran.veteransmp.VeteranSMP;

@EventBusSubscriber(modid = VeteranSMP.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class PlayerTracker {
    private static PlayerLoginData INSTANCE;

    public static PlayerLoginData get() {
        if (INSTANCE == null) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                ServerLevel overworld = server.overworld();
                INSTANCE = overworld.getDataStorage().computeIfAbsent(
                        PlayerLoginData.FACTORY,
                        PlayerLoginData.DATA_NAME
                );
            } else {
                throw new IllegalStateException("MinecraftServer not available yet.");
            }
        }
        return INSTANCE;
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        get();
        INSTANCE.setPlayer(player.getUUID());
    }
}
