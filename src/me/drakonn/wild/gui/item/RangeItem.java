package me.drakonn.wild.gui.item;

import org.bukkit.inventory.ItemStack;

public class RangeItem extends AbstractItem
{
    private int maxRange;
    private int minRange;

    public RangeItem(ItemStack item, int invSlot, ItemType type, int maxRange, int minRange, int cost, String permission)
    {
        super(item, invSlot, type, cost, permission);
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
