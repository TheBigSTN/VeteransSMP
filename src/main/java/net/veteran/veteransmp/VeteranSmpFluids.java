package net.veteran.veteransmp;

import com.simibubi.create.AllFluids;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;

public class VeteranSmpFluids {
    private static final CreateRegistrate REGISTRAR = VeteranSMP.registrate();

    public static final FluidEntry<BaseFlowingFluid.Flowing> MOLTEN_IRON;

    static {
        MOLTEN_IRON =
                REGISTRAR.standardFluid("molten_iron")
                        .properties(b -> b
                                .viscosity(2500)
                                .density(1600))
                        .fluidProperties(p -> p
                                .levelDecreasePerBlock(2)
                                .tickRate(15)
                                .slopeFindDistance(6)
                                .explosionResistance(100f))
                        .source(BaseFlowingFluid.Source::new)
                        .bucket()
                        .build()
                        .register();

    }

    public static void register() {
        //Load the class
    }
}
