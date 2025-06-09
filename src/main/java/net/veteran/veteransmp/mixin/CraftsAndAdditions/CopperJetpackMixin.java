package net.veteran.veteransmp.mixin.CraftsAndAdditions;

import net.mcreator.createstuffadditions.item.BrassJetpackItem;
import net.mcreator.createstuffadditions.item.CopperJetpackItem;
import net.mcreator.createstuffadditions.procedures.BrassEncasedPropelerBodyTickEventProcedure;
import net.mcreator.createstuffadditions.procedures.CopperPropelerBodyTickEventProcedure;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.veteran.veteransmp.VeteranSMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(CopperJetpackItem.Chestplate.class)
public class CopperJetpackMixin {

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void onInventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if (entity instanceof LivingEntity living) {

            CuriosApi.getCuriosInventory(living).ifPresent(curiosInventory -> {
                Map<String, ICurioStacksHandler> curios = curiosInventory.getCurios();
                for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                    ICurioStacksHandler handler = entry.getValue();
                    for (int i = 0; i < handler.getSlots(); i++) {
                        ItemStack stackInSlot = handler.getStacks().getStackInSlot(i);
                        if (stackInSlot.is(itemstack.getItem())) {
                            CopperPropelerBodyTickEventProcedure.execute(world, entity.getX(), entity.getY(), entity.getZ(), entity, itemstack);
                        }
                    }
                }
            });
        }
    }
}