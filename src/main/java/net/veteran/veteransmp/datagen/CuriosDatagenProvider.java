package net.veteran.veteransmp.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.veteran.veteransmp.VeteranSMP;
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
                .size(4)
                .addValidator(VeteranSMP.asResource("create_backtank"));
        this.createSlot("head")
                .dropRule(ICurio.DropRule.ALWAYS_KEEP)
                .addValidator(VeteranSMP.asResource("head_goggles"));

        this.createEntities("main")
                .addPlayer()
                .addSlots("head", "body");
    }
}
