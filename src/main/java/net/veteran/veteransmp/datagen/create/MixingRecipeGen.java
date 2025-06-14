package net.veteran.veteransmp.datagen.create;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.veteran.veteransmp.VeteranSMP;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class MixingRecipeGen extends CreateRecipeProvider {
    public MixingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    GeneratedRecipe

    MoltenIron = create("molten_iron", b -> b
			.require(Tags.Items.INGOTS_IRON)
			.output(Fluids.LAVA, 500)
            .requiresHeat(HeatCondition.SUPERHEATED)),

    Lapis_Lazuli = create("lapis_lazuli", b-> b
            .require(Items.QUARTZ)
            .require(Items.BLUE_DYE)
            .output(Items.LAPIS_LAZULI, 1)),

    Rooted_Dirt = create("rooted_dirt", b-> b
            .require(Items.DIRT)
            .require(Items.HANGING_ROOTS)
            .output(Items.ROOTED_DIRT, 1)),

    Netherack = create("netherack", b->b
            .require(AllTags.commonItemTag("stone"))
            .require(Items.NETHER_WART)
            .output(Items.NETHERRACK, 1));

    protected GeneratedRecipe create(String name, UnaryOperator<ProcessingRecipeBuilder<ProcessingRecipe<?>>> transform) {
        ProcessingRecipeSerializer<ProcessingRecipe<?>> serializer = AllRecipeTypes.MIXING.getSerializer();
        GeneratedRecipe generatedRecipe =
                c -> transform.apply(new ProcessingRecipeBuilder<>(serializer.getFactory(), VeteranSMP.asResource(name)))
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    @Override
    public @NotNull String getName() {
        return "Veteran's SMP Mixing Recipes";
    }
}
