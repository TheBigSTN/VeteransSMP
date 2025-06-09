package net.veteran.veteransmp.mixin;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class VeteranMixinPlugin implements IMixinConfigPlugin {

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains(".mixin.CraftsAndAdditions.")) {
            boolean hasCreateAdditions = LoadingModList.get().getMods().stream()
                    .anyMatch(mod -> mod.getModId().equals("create_sa"));

            boolean hasCurios = LoadingModList.get().getMods().stream()
                    .anyMatch(mod -> mod.getModId().equals("curios"));

            return hasCreateAdditions && hasCurios;
        }
        if (mixinClassName.endsWith("BankAPIImplMixin"))
            return LoadingModList.get().getMods().stream()
                    .anyMatch(modInfo -> modInfo.getModId().equals("lightmanscurrency"));

        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

}
