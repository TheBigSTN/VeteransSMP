package net.veteran.veteransmp.mixin.CraftsAndAdditions;

import net.mcreator.createstuffadditions.configuration.CreateSaConfigConfiguration;
import net.mcreator.createstuffadditions.procedures.SmallFillingTankItemInInventoryTickProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Map;

@Mixin(SmallFillingTankItemInInventoryTickProcedure.class)
public class FillingTankMixin {

    @Inject(method = "execute", at = @At("TAIL"))
    private static void onExecute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack, CallbackInfo ci) {
        if (!(entity instanceof LivingEntity living)) return;
        if (!(world instanceof Level level)) return;
        if (!ModList.get().isLoaded("curios")) return;

        CuriosApi.getCuriosInventory(living).ifPresent(curiosInventory -> {
            Map<String, ICurioStacksHandler> curios = curiosInventory.getCurios();
            for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                ICurioStacksHandler handler = entry.getValue();
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack target = handler.getStacks().getStackInSlot(i);

                    if (target.is(ItemTags.create(ResourceLocation.parse("create_sa:fillable")))) {
                        double tagWater = ((CustomData) target.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY))
                                .copyTag().getDouble("tagWater");
                        double tagStock = ((CustomData) itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY))
                                .copyTag().getDouble("tagStock");
                        double maxCapacity = CreateSaConfigConfiguration.GADGETCAPACITY.get();

                        if (tagStock > 0 && tagWater < maxCapacity) {
                            double newWater = Math.min(tagWater + 1.0, maxCapacity);
                            double newStock = tagStock - 1.0;

                            CustomData.update(DataComponents.CUSTOM_DATA, target, tag -> tag.putDouble("tagWater", newWater));
                            CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble("tagStock", newStock));

                            if (!level.isClientSide()) {
                                level.playSound(null, BlockPos.containing(x, y, z), (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")), SoundSource.NEUTRAL, 0.01F, 2.0F);
                            } else {
                                level.playLocalSound(x, y, z, (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.bucket.empty")), SoundSource.NEUTRAL, 0.01F, 2.0F, false);
                            }
                        }
                    }
                }
            }
        });
    }
}