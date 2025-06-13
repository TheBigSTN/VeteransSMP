package net.veteran.veteransmp.compat.Create;

import com.simibubi.create.api.behaviour.spouting.BlockSpoutingBehaviour;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import net.mcreator.createstuffadditions.configuration.CreateSaConfigConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.veteran.veteransmp.datagen.CuriosDatagenProvider;

public class FillingSpoutBehaviours implements BlockSpoutingBehaviour {

    @Override
    public int fillBlock(Level level, BlockPos pos,
                         SpoutBlockEntity spout, FluidStack availableFluid, boolean simulate) {


        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) {
            return 0;
        }

        IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, blockEntity.getBlockPos(), Direction.UP);
        if (handler == null || handler.getSlots() == 0) {
            return 0;
        }


        ItemStack stack = handler.getStackInSlot(0);
        if (stack.isEmpty()) {
            return 0;
        }

        int amount = availableFluid.getAmount();
        int drain = amount / 10;
        if (drain <= 0) {
            return 0;
        }

        double maxfill = 0;

        if (stack.is(CuriosDatagenProvider.item("create_sa", "large_filling_tank"))) {
            if (!availableFluid.is(Fluids.WATER)) {
                return 0;
            }
            maxfill = CreateSaConfigConfiguration.LARGETANKCAPACITY.get();
        } else if (stack.is(CuriosDatagenProvider.item("create_sa", "medium_filling_tank"))) {
            if (!availableFluid.is(Fluids.WATER)) {
                return 0;
            }
            maxfill = CreateSaConfigConfiguration.MEDIUMTANKCAPACITY.get();
        } else if (stack.is(CuriosDatagenProvider.item("create_sa", "small_filling_tank"))) {
            if (!availableFluid.is(Fluids.WATER)) {
                return 0;
            }
            maxfill = CreateSaConfigConfiguration.SMALLTANKCAPACITY.get();
        } else if (stack.is(CuriosDatagenProvider.item("create_sa", "large_fueling_tank"))) {
            if (!availableFluid.is(Fluids.LAVA)) {
                return 0;
            }
            maxfill = CreateSaConfigConfiguration.LARGETANKCAPACITY.get();
        } else if (stack.is(CuriosDatagenProvider.item("create_sa", "medium_fueling_tank"))) {
            if (!availableFluid.is(Fluids.LAVA)) {
                return 0;
            }
            maxfill = CreateSaConfigConfiguration.MEDIUMTANKCAPACITY.get();
        } else if (stack.is(CuriosDatagenProvider.item("create_sa", "small_fueling_tank"))) {
            if (!availableFluid.is(Fluids.LAVA)) {
                return 0;
            }
            maxfill = CreateSaConfigConfiguration.SMALLTANKCAPACITY.get();
        } else {
            return 0;
        }



        double tagStock = ((CustomData) stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY))
                .copyTag().getDouble("tagStock");

        if (tagStock + drain > maxfill) {
            drain = (int)maxfill - (int)tagStock;
        } else if (tagStock == maxfill) {
            return 0;
        } else if (simulate) {
            return 1;
        }
        int finalDrain = drain;

        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putDouble("tagStock", tagStock + finalDrain));

        if (handler instanceof IItemHandlerModifiable modifiable) {
            modifiable.setStackInSlot(0, stack);
        }

        return finalDrain * 10;
    }

}
