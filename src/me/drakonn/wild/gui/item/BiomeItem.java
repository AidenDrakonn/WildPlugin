package me.drakonn.wild.gui.item;

import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BiomeItem extends AbstractItem {

    public List<Biome> target;

    public BiomeItem(ItemStack item, int invSlot, ItemType type, List<Biome> target)
    {
        super(item, invSlot, type);
        this.target = target;
    }

    public List<Biome> getTarget() {
        return target;
    }
}
