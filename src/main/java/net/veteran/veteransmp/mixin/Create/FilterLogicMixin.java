package net.veteran.veteransmp.mixin.Create;

import io.github.lightman314.lightmanscurrency.api.easy_data.EasyDataSettings;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.veteran.veteransmp.VeteranSMP;
import net.veteran.veteransmp.datagen.CuriosDatagenProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.simibubi.create.content.logistics.filter.FilterItem")
public class FilterLogicMixin {
    @Inject(
            method = "testDirect",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void onTest(ItemStack filter, ItemStack stack, boolean matchNBT, CallbackInfoReturnable<Boolean> cir) {
        if ( veteransSMP$isOurTank(filter) && veteransSMP$isOurTank(stack)) {
            if (matchNBT && cir.getReturnValue()) {
                double tagFilter = ((CustomData) filter.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY))
                        .copyTag().getDouble("tagStock");
                double tagStack = ((CustomData) stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY))
                        .copyTag().getDouble("tagStock");
                cir.setReturnValue(tagStack == tagFilter);
                
            }
        }
    }

    @Unique
    private static boolean veteransSMP$isOurTank(ItemStack stack) {
        return stack.is(CuriosDatagenProvider.item("create_sa", "large_filling_tank"))
                || stack.is(CuriosDatagenProvider.item("create_sa", "medium_filling_tank"))
                || stack.is(CuriosDatagenProvider.item("create_sa", "small_filling_tank"))
                || stack.is(CuriosDatagenProvider.item("create_sa", "large_fueling_tank"))
                || stack.is(CuriosDatagenProvider.item("create_sa", "medium_fueling_tank"))
                || stack.is(CuriosDatagenProvider.item("create_sa", "small_fueling_tank"));
    }
}