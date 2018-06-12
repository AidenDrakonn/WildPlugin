package me.drakonn.wild.gui.item;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractItem
{
    private int invSlot;
    private ItemStack item;
    private ItemType type;
    private int cost;
    private String permission;
    private static List<AbstractItem> items = new ArrayList<>();

    public AbstractItem(ItemStack item, int invSlot, ItemType type, int cost, String permission) {
        this.item = item;
        this.invSlot = invSlot;
        this.type = type;
        this.cost = cost;
        this.permission = permission;
        items.add(this);
    }

    public static AbstractItem getAbstractItem(ItemStack item) {
        return items.stream().filter(abstractItem -> abstractItem.getItem().isSimilar(item))
                .findFirst().orElse(null);
    }

    public int getInvSlot() { return invSlot; }

    public ItemStack getItem() { return item; }

    public ItemType getType() { return type; }

    public int getCost() { return cost; }

    public String getPermission() { return permission; }

}
