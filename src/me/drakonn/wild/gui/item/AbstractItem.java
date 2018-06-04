package me.drakonn.wild.gui.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public abstract class AbstractItem
{
    private static int invSlot;
    private static ItemStack item;
    private static List<String> lore;
    private static Material type;
    private static String name;
    private static HashMap<ItemStack, Integer> mainGUIItems = new HashMap<>();
    private static HashMap<ItemStack, Integer> rangeGUIItems = new HashMap<>();


    public AbstractItem(String name, List<String> lore, Material material, int invSlot)
    {
        this.name = name;
        this.invSlot = invSlot;
        this.lore = lore;
        type = material;
    }

    public abstract ItemStack getItem();

    public static int getInvSlot() {
        return invSlot;
    }

    public static void setInvSlot(int invSlot) {
        AbstractItem.invSlot = invSlot;
    }

    public static void setItem(ItemStack item) {
        AbstractItem.item = item;
    }

    public static List<String> getLore() {
        return lore;
    }

    public static void setLore(List<String> lore) {
        AbstractItem.lore = lore;
    }

    public static Material getType() {
        return type;
    }

    public static void setType(Material type) {
        AbstractItem.type = type;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        AbstractItem.name = name;
    }

    public static HashMap<ItemStack, Integer> getMainGUIItems() {
        return mainGUIItems;
    }

    public static void setMainGUIItems(HashMap<ItemStack, Integer> mainGUIItems) {
        AbstractItem.mainGUIItems = mainGUIItems;
    }

    public static HashMap<ItemStack, Integer> getRangeGUIItems() {
        return rangeGUIItems;
    }

    public static void setRangeGUIItems(HashMap<ItemStack, Integer> rangeGUIItems) {
        AbstractItem.rangeGUIItems = rangeGUIItems;
    }
}
