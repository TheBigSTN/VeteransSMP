package net.veteran.veteransmp.datagen;

import com.simibubi.create.infrastructure.data.GeneratedEntriesProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.veteran.veteransmp.VeteranSMP;
import net.veteran.veteransmp.datagen.create.*;

import java.util.concurrent.CompletableFuture;

//@EventBusSubscriber(modid = VeteranSMP.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerator {

    //@SubscribeEvent(priority = EventPriority.LOW)
    public static void gatherData(GatherDataEvent event) {
        net.minecraft.data.DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(
                event.includeServer(),
                new CuriosDatagenProvider(
                        packOutput,
                        fileHelper,
                        lookupProvider
                )
        );

        generator.addProvider(
                event.includeServer(),
                new ModRecipeProvider(packOutput, lookupProvider)
        );

        generator.addProvider(
                event.includeServer(),
                new SequencedAssemblyGen(packOutput, lookupProvider)
        );

        generator.addProvider(
                event.includeServer(),
                new MixingRecipeGen(packOutput, lookupProvider)
        );

        generator.addProvider(
                event.includeServer(),
                new WashingRecipeGen(packOutput, lookupProvider)
        );

        generator.addProvider(
                event.includeServer(),
                new CompactingRecipeGen(packOutput, lookupProvider)
        );

        generator.addProvider(
                event.includeServer(),
                new HauntingRecipeGen(packOutput, lookupProvider)
        );

        generator.addProvider(
                event.includeServer(),
                new MillingRecipeGen(packOutput, lookupProvider)
        );
    }
}
