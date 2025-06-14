package net.veteran.veteransmp.datagen.create;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.mixer.CompactingRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.veteran.veteransmp.VeteranSMP;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class CompactingRecipeGen extends CreateRecipeProvider {
    public CompactingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    GeneratedRecipe

    DripStone = create("dripstone", b -> b
            .require(Items.FLINT)
            .require(Items.FLINT)
            .require(Fluids.LAVA, 100)
            .require(Items.RED_SAND)
            .output(Blocks.DRIPSTONE_BLOCK, 1)),

    g = create("tuff", b -> b
                    .require(Items.FLINT)
                    .require(Items.FLINT)
                    .require(Fluids.LAVA, 100)
                    .require(Items.SAND)
                    .output(Blocks.TUFF, 1))
            ;

    protected GeneratedRecipe create(String name, UnaryOperator<ProcessingRecipeBuilder<CompactingRecipe>> transform) {
        ProcessingRecipeSerializer<CompactingRecipe> serializer = AllRecipeTypes.COMPACTING.getSerializer();
        GeneratedRecipe generatedRecipe =
                c -> transform.apply(new ProcessingRecipeBuilder<>(serializer.getFactory(), VeteranSMP.asResource(name)))
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    @Override
    public @NotNull String getName() {
        return "Veteran's SMP Compacting Recipes";
    }
}
