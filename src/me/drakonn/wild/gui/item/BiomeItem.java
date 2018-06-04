package me.drakonn.wild.gui.item;

import me.drakonn.wild.util.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BiomeItem extends AbstractItem {

    public BiomeItem(Material material, String name, List<String> lore, int invSlot)
    {
        super(name, lore, material, invSlot);
    }

    public ItemStack getItem() {
        return Util.createItem(getType(), getName(), getLore());
    }
}
