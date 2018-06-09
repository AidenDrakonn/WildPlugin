package me.drakonn.wild.gui.item;

import org.bukkit.inventory.ItemStack;

public class RangeItem extends AbstractItem
{
    public int maxRange;
    public int minRange;

    public RangeItem(ItemStack item, int invSlot, ItemType type, int maxRange, int minRange, int cost)
    {
        super(item, invSlot, type, cost);
        this.maxRange = maxRange;
        this.minRange = minRange;
    }


    public int getMaxRange() {
        return maxRange;
    }

    public int getMinRange() {
        return minRange;
    }
}
