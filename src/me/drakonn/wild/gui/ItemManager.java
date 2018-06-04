package me.drakonn.wild.gui;

import me.drakonn.wild.Wild;
import me.drakonn.wild.gui.item.*;
import me.drakonn.wild.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ItemManager
{
    private List<BiomeItem> biomeItems = new ArrayList<>();
    private List<WorldItem> worldItems = new ArrayList<>();
    private List<RangeItem> rangeItems = new ArrayList<>();
    private List<AbstractItem> mainGuiItems = new ArrayList<>();
    private Inventory mainInv;
    private Inventory rangeInv;

    private Wild plugin;
    public ItemManager(Wild plugin)
    {
        this.plugin = plugin;
    }

    private boolean loadItems()
    {
        biomeItems.clear();
        worldItems.clear();
        rangeItems.clear();
        mainGuiItems.clear();
        Set<String> rangePaths = plugin.getConfig().getConfigurationSection("guiitem.rangegui").getKeys(false);
        Set<String> mainPaths = plugin.getConfig().getConfigurationSection("guiitem.maingui").getKeys(false);
        for(String path : mainPaths)
        {
            if(plugin.getConfig().getString(path+".type").equalsIgnoreCase("biome"))
            {
                loadBiomeItem(path);
            }
            if(plugin.getConfig().getString(path+"type").equalsIgnoreCase("world"))
            {
                loadWorldItem(path);
            }
            if(plugin.getConfig().getString(path+"type").equalsIgnoreCase("rangeselector"))
            {
                loadRangeSelectorItem(path);
            }
        }

        for(String path : rangePaths)
        {
            loadRangeItem(path);
        }

        int mainInvSize = plugin.getConfig().getInt("gui.main.size");
        mainInv = Bukkit.createInventory(null, mainInvSize, "Wild");
        for(AbstractItem mainGuiItem : mainGuiItems)
        {
            mainInv.setItem(mainGuiItem.getInvSlot(), mainGuiItem.getItem());
        }

        int rangeInvSize = plugin.getConfig().getInt("gui.range.size");
        rangeInv = Bukkit.createInventory(null, rangeInvSize, "Range Selector");
        for(RangeItem rangeItem : rangeItems)
        {
            rangeInv.setItem(rangeItem.getInvSlot(), rangeItem.getItem());
        }
        return true;
    }

    private void loadRangeItem(String path)
    {

        int maxRange = plugin.getConfig().getInt(path+".maxrange");
        int minRange = plugin.getConfig().getInt(path+".minrange");
        int invSlot = plugin.getConfig().getInt(path+".invslot");
        ItemStack item = loadItem(path);
        rangeItems.add(new RangeItem(item, invSlot, ItemType.RANGE, maxRange, minRange));
    }

    private void loadBiomeItem(String path)
    {
        int invSlot = plugin.getConfig().getInt(path+".invslot");
        List<Biome> target = new ArrayList<>();
        for(String biomeName : plugin.getConfig().getStringList(path+".target"))
            target.add(Biome.valueOf(biomeName));
        ItemStack item = loadItem(path);
        BiomeItem biomeItem = new BiomeItem(item, invSlot, ItemType.BIOME, target);
        biomeItems.add(biomeItem);
        mainGuiItems.add(biomeItem);
    }

    private void loadWorldItem(String path)
    {
        int invSlot = plugin.getConfig().getInt(path+".invslot");
        String worldName = plugin.getConfig().getString(path+".target");
        World target = Bukkit.getWorld(worldName);
        ItemStack item = loadItem(path);
        WorldItem worldItem = new WorldItem(item, invSlot, ItemType.WORLD, target);
        worldItems.add(worldItem);
        mainGuiItems.add(worldItem);
    }

    private void loadRangeSelectorItem(String path)
    {
        ItemStack item = loadItem(path);
        int invSlot = plugin.getConfig().getInt(path+".invslot");
        RangeSelectorItem rangeSelectorItem = new RangeSelectorItem(item, invSlot, ItemType.RANGESELECTOR);
        mainGuiItems.add(rangeSelectorItem);
    }

    private ItemStack loadItem(String path)
    {
        String materialData = plugin.getConfig().getString(path+".material");
        Material material = Material.valueOf(materialData.split(":")[0]);
        String name = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(path+".name"));
        List<String> lore = plugin.getConfig().getStringList(path+".lore");
        for(String loreString : lore)
        {
            lore.remove(loreString);
            loreString = ChatColor.translateAlternateColorCodes('&', loreString);
            lore.add(loreString);
        }
        ItemStack item = Util.createItem(material, name, lore);
        if(materialData.split(":").length == 2)
        {
            byte data = Byte.valueOf(materialData.split(":")[1]);
            MaterialData matData = item.getData();
            matData.setData(data);
            item.setData(matData);
        }

        return item;
    }

    public List<BiomeItem> getBiomeItems() {
        return biomeItems;
    }

    public List<WorldItem> getWorldItems() {
        return worldItems;
    }

    public List<RangeItem> getRangeItems() {
        return rangeItems;
    }

    public List<AbstractItem> getMainGuiItems() {
        return mainGuiItems;
    }
}
