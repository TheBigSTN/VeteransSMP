package net.veteran.veteransmp;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;

public class ModItems {
    private static final CreateRegistrate REGISTRATE = VeteranSMP.registrate();

    public static final ItemEntry<SequencedAssemblyItem>
            UNFINISHED_SLIMEBALL = sequencedIngredient("unfinished_slimeball");


    // Shortcuts

    private static ItemEntry<Item> ingredient(String name) {
        return REGISTRATE.item(name, Item::new)
                .register();
    }

    private static ItemEntry<SequencedAssemblyItem> sequencedIngredient(String name) {
        return REGISTRATE.item(name, SequencedAssemblyItem::new)
                .register();
    }
    public static void register(IEventBus eventBus) {
        //Load the class
    }
}
