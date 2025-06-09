package net.veteran.veteransmp.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.veteran.veteransmp.VeteranSMP;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosDataProvider;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.concurrent.CompletableFuture;

public class CuriosDatagenProvider extends CuriosDataProvider {

    public CuriosDatagenProvider(PackOutput output,
                               ExistingFileHelper fileHelper,
                               CompletableFuture<HolderLookup.Provider> registries) {
        super(VeteranSMP.MOD_ID, output, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {
        this.createSlot("body")
                .dropRule(ICurio.DropRule.ALWAYS_DROP)
                .size(1)
                .addValidator(VeteranSMP.asResource("body_validator"));
        this.createSlot("tank")
                .dropRule(ICurio.DropRule.ALWAYS_DROP)
                .size(4)
                .addValidator(VeteranSMP.asResource("tank_validator"));
        this.createSlot("jetpack")
                .dropRule(ICurio.DropRule.ALWAYS_DROP)
                .size(1)
                .addValidator(VeteranSMP.asResource("jetpack_validator"));

        this.createEntities("main")
                .addPlayer()
                .addSlots("body", "tank", "jetpack");
    }

    public static void validators() {
        CuriosApi.registerCurioPredicate(ResourceLocation.fromNamespaceAndPath(VeteranSMP.MOD_ID, "body_validator"), (slotResult) -> {
            ItemStack stack = slotResult.stack();
            return stack.is(item("create", "copper_backtank"));
        });
        CuriosApi.registerCurioPredicate(ResourceLocation.fromNamespaceAndPath(VeteranSMP.MOD_ID, "tank_validator"), (slotResult) -> {
            ItemStack stack = slotResult.stack();
            if (stack.is(item("create_sa", "large_filling_tank")))
                return true;
            if (stack.is(item("create_sa", "large_fueling_tank")))
                return true;
            if (stack.is(item("create_sa", "medium_filling_tank")))
                return true;
            if (stack.is(item("create_sa", "medium_fueling_tank")))
                return true;
            if (stack.is(item("create_sa", "small_filling_tank")))
                return true;
            return stack.is(item("create_sa", "small_fueling_tank"));
        });
        CuriosApi.registerCurioPredicate(ResourceLocation.fromNamespaceAndPath(VeteranSMP.MOD_ID, "jetpack_validator"), (slotResult) -> {
            ItemStack stack = slotResult.stack();
            if (stack.is(item("create_sa", "copper_jetpack_chestplate")))
                return true;
            if (stack.is(item("create_sa", "andesite_jetpack_chestplate")))
                return true;
            return stack.is(item("create_sa", "brass_jetpack_chestplate"));
        });
    }

    public static Item item(String namespace, String path) {
        ResourceLocation itemIdentifier = ResourceLocation.fromNamespaceAndPath(namespace, path);
        return BuiltInRegistries.ITEM.get(itemIdentifier);
    }
}
