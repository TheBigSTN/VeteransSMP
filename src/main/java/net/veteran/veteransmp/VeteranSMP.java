package net.veteran.veteransmp;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.behaviour.spouting.BlockSpoutingBehaviour;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.veteran.veteransmp.commands.CommandLoader;
import net.veteran.veteransmp.datagen.CuriosDatagenProvider;
import net.veteran.veteransmp.persistent.PlayerTracker;
import net.veteran.veteransmp.datagen.DataGenerator;
import net.veteran.veteransmp.compat.Create.FillingSpoutBehaviours;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(VeteranSMP.MOD_ID)
public class VeteranSMP
{
    public static final String MOD_ID = "veteransmp";

    public static final Logger Logger = LogUtils.getLogger();

    private static final CreateRegistrate REGISTRAR = CreateRegistrate.create(MOD_ID)
            .defaultCreativeTab(VeteranSmpCreativeTabs.BASE_CREATIVE_TAB.getKey());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public VeteranSMP(IEventBus modEventBus, ModContainer modContainer)
    {
        REGISTRAR.registerEventListeners(modEventBus);
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        VeteranSmpItems.register();
        VeteranSmpFluids.register();
        VeteranSmpCreativeTabs.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(PlayerTracker.class);

        modEventBus.addListener(VeteranSMP::init);
        modEventBus.addListener(EventPriority.LOW, DataGenerator::gatherData);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.SERVER, VeteranSmpConfig.Server.SPEC);
    }

    public static void init(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> BlockSpoutingBehaviour.BY_BLOCK.register(
            AllBlocks.DEPOT.get(),
                new FillingSpoutBehaviours()
        ));
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    @SubscribeEvent
    public void onCommandLoading(RegisterCommandsEvent event) {
        CommandLoader.register(event.getDispatcher());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        CuriosDatagenProvider.validators();
    }

    public static CreateRegistrate registrate() {
        return REGISTRAR;
    }
}
