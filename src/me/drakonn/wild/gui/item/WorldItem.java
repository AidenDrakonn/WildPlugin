package me.drakonn.wild.gui.item;

import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public class WorldItem extends AbstractItem {

    private World target;

    public WorldItem(ItemStack item, int invSlot, ItemType type, World target, int cost, String permission)
    {
        super(item, invSlot, type, cost, permission);
        this.target = target;
    }

    public World getTarget() {
        return target;
    }
}

