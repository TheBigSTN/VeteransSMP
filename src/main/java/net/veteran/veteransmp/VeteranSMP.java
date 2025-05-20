package net.veteran.veteransmp;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.veteran.veteransmp.commands.tpa.TpaCommand;
import net.veteran.veteransmp.compat.PlayerTracker;
import net.veteran.veteransmp.datagen.DataGenerator;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import top.theillusivec4.curios.api.CuriosApi;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(VeteranSMP.MOD_ID)
public class VeteranSMP
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "veteransmp";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null);

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public VeteranSMP(IEventBus modEventBus, ModContainer modContainer)
    {
        REGISTRATE.registerEventListeners(modEventBus);
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        ModItems.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(PlayerTracker.class);



        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(EventPriority.LOW, DataGenerator::gatherData);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.SERVER, VConfig.Server.SPEC);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    @SubscribeEvent
    public void onCommandLoading(RegisterCommandsEvent event) {
        TpaCommand.register(event.getDispatcher());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        CuriosApi.registerCurioPredicate(ResourceLocation.fromNamespaceAndPath(VeteranSMP.MOD_ID, "create_backtank"), (slotResult) -> {
            ItemStack stack = slotResult.stack();
            ResourceLocation itemIdentifier = ResourceLocation.fromNamespaceAndPath("create", "copper_backtank");
            Item targetItem = BuiltInRegistries.ITEM.get(itemIdentifier);

            return stack.is(targetItem);
        });
    }


    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }
}
