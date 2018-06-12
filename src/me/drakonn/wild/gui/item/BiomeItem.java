package me.drakonn.wild.gui.item;

import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BiomeItem extends AbstractItem {

    private Biome target;

    public BiomeItem(ItemStack item, int invSlot, ItemType type, Biome target, int cost, String permission)
    {
        super(item, invSlot, type, cost, permission);
        this.target = target;
    }

    public Biome getTarget() {
        return target;
    }
}
