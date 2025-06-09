package net.veteran.veteransmp.datagen.create;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.veteran.veteransmp.ModItems;
import net.veteran.veteransmp.VeteranSMP;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class SequencedAssemblyGen extends CreateRecipeProvider {

    GeneratedRecipe

    SLIME_BALL = create("slime_ball", b->b.require(Items.SLIME_BALL)
            .transitionTo(ModItems.UNFINISHED_SLIMEBALL.get())
            .addOutput(new ItemStack(Items.SLIME_BALL, 8), 1)
            .loops(1)
            .addStep(FillingRecipe::new,
                    rb -> rb.require(Fluids.WATER, 100))
            .addStep(DeployerApplicationRecipe::new,
                    rb -> rb.require(Ingredient.of(ItemTags.LEAVES)))
            .addStep(DeployerApplicationRecipe::new,
                    rb -> rb.require(AllItems.WHEAT_FLOUR)));

    public SequencedAssemblyGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    protected GeneratedRecipe create(String name, UnaryOperator<SequencedAssemblyRecipeBuilder> transform) {
        GeneratedRecipe generatedRecipe =
                c -> transform.apply(new SequencedAssemblyRecipeBuilder(VeteranSMP.asResource(name)))
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    @Override
    public @NotNull String getName() {
        return "Veteran's SMP Sequenced Recipes";
    }
}