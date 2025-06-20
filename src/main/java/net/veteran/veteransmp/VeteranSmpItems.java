package net.veteran.veteransmp;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

public class VeteranSmpItems {
    private static final CreateRegistrate REGISTRAR = VeteranSMP.registrate();

    public static final ItemEntry<SequencedAssemblyItem>
            UNFINISHED_SLIMEBALL = sequencedIngredient("unfinished_slimeball");


    // Shortcuts

    private static ItemEntry<Item> ingredient(String name) {
        return REGISTRAR.item(name, Item::new)
                .register();
    }

    private static ItemEntry<SequencedAssemblyItem> sequencedIngredient(String name) {
        return REGISTRAR.item(name, SequencedAssemblyItem::new)
                .register();
    }
    public static void register() {
        //Load the class
    }
}
