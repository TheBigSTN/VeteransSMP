package net.veteran.veteransmp.datagen.create;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.fan.processing.HauntingRecipe;
import com.simibubi.create.content.kinetics.mixer.CompactingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.veteran.veteransmp.VeteranSMP;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class HauntingRecipeGen extends CreateRecipeProvider {
    public HauntingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    GeneratedRecipe

            Phantom_Membrane = create("phantom_membrane", b -> b
            .require(Items.FEATHER)
            .output(Items.PHANTOM_MEMBRANE));

    protected GeneratedRecipe create(String name, UnaryOperator<ProcessingRecipeBuilder<HauntingRecipe>> transform) {
        ProcessingRecipeSerializer<HauntingRecipe> serializer = AllRecipeTypes.COMPACTING.getSerializer();
        GeneratedRecipe generatedRecipe =
                c -> transform.apply(new ProcessingRecipeBuilder<>(serializer.getFactory(), VeteranSMP.asResource(name)))
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    @Override
    public @NotNull String getName() {
        return "Veteran's SMP Haunting Recipes";
    }
}
