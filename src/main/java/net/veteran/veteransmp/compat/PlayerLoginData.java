package net.veteran.veteransmp.compat;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerLoginData extends SavedData {

    public static final String DATA_NAME = "player_login_data";

    private final Map<UUID, Long> lastLoginMap = new HashMap<>();

    public void setPlayer(UUID player) {
        lastLoginMap.put(player, System.currentTimeMillis());
        this.setDirty();
    }

    public long getPlayer(UUID player) {
        return System.currentTimeMillis() - lastLoginMap.getOrDefault(player, 0L);
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, Long> entry : lastLoginMap.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putUUID("uuid", entry.getKey());
            entryTag.putLong("time", entry.getValue());
            list.add(entryTag);
        }
        tag.put(DATA_NAME, list);
        return tag;
    }

    public static PlayerLoginData load(CompoundTag tag, HolderLookup.Provider registries) {
        PlayerLoginData data = new PlayerLoginData();
        ListTag list = tag.getList(DATA_NAME, Tag.TAG_COMPOUND);
        for (Tag t : list) {
            CompoundTag entryTag = (CompoundTag) t;
            UUID uuid = entryTag.getUUID("uuid");
            long time = entryTag.getLong("time");
            data.lastLoginMap.put(uuid, time);
        }
        return data;
    }

    public static final SavedData.Factory<PlayerLoginData> FACTORY =
            new SavedData.Factory<>(PlayerLoginData::new, PlayerLoginData::load);
}