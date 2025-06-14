package net.veteran.veteransmp.datagen.create;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.millstone.MillingRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.veteran.veteransmp.VeteranSMP;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class MillingRecipeGen extends CreateRecipeProvider {
    public MillingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    GeneratedRecipe

    ClayBall = create("clay_ball", b -> b
            .require(Items.DRIPSTONE_BLOCK)
            .duration(250)
            .output(0.25f, Items.CLAY_BALL)
            .output(0.125f, AllItems.COPPER_NUGGET ,3));

    protected GeneratedRecipe create(String name, UnaryOperator<ProcessingRecipeBuilder<MillingRecipe>> transform) {
        ProcessingRecipeSerializer<MillingRecipe> serializer = AllRecipeTypes.MILLING.getSerializer();
        GeneratedRecipe generatedRecipe =
                c -> transform.apply(new ProcessingRecipeBuilder<>(serializer.getFactory(), VeteranSMP.asResource(name)))
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    @Override
    public @NotNull String getName() {
        return "Veteran's SMP Milling Recipes";
    }
}
