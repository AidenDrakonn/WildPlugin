package me.drakonn.wild.datamanager;

import me.drakonn.wild.Wild;
import me.drakonn.wild.gui.item.*;
import me.drakonn.wild.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ItemManager
{
    private static List<BiomeItem> biomeItems = new ArrayList<>();
    private static List<WorldItem> worldItems = new ArrayList<>();
    private static List<RangeItem> rangeItems = new ArrayList<>();
    private static List<AbstractItem> mainGuiItems = new ArrayList<>();
    public static Material noPermissionMaterial;
    public static int mainInvSize;
    public static int rangeInvSize;
    public static String mainInvName;
    public static String rangeInvName;

    private FileConfiguration config;
    private Wild plugin;
    public ItemManager(Wild plugin)
    {
        this.plugin = plugin;
        config = plugin.getConfig();
    }

    public void loadItems()
    {
        biomeItems.clear();
        worldItems.clear();
        rangeItems.clear();
        mainGuiItems.clear();
        ConfigurationSection mainGui = config.getConfigurationSection("guiitem.maingui");
        ConfigurationSection rangeGui = config.getConfigurationSection("guiitem.rangegui");
        for(String path : mainGui.getKeys(false))
        {
            ConfigurationSection section = mainGui.getConfigurationSection(path);
            if(section.getString("type").equalsIgnoreCase("biome"))
            {
                loadBiomeItem(section);
            }
            if(section.getString("type").equalsIgnoreCase("world"))
            {
                loadWorldItem(section);
            }
            if(section.getString("type").equalsIgnoreCase("rangeselector"))
            {
                loadRangeSelectorItem(section);
            }
        }

        for(String path : rangeGui.getKeys(false))
        {
            ConfigurationSection section = rangeGui.getConfigurationSection(path);
            loadRangeItem(section);
        }

        rangeInvSize = config.getInt("gui.range.size");
        rangeInvName = config.getString("gui.range.name");
        mainInvSize = config.getInt("gui.main.size");
        mainInvName = config.getString("gui.main.name");

        noPermissionMaterial = Material.valueOf(config.getString("default.nopermissionmaterial"));
    }

    private void loadRangeItem(ConfigurationSection section)
    {
        int cost = getCost(section);
        int maxRange = section.getInt("maxrange");
        int minRange = section.getInt("minrange");
        int invSlot = section.getInt("invslot");
        String permission = section.getString("permission");
        ItemStack item = loadItem(section);
        item = Util.setPlaceHolders(item, String.valueOf(cost), "", "", String.valueOf(minRange), String.valueOf(maxRange));
        rangeItems.add(new RangeItem(item, invSlot, ItemType.RANGE, maxRange, minRange, cost, permission));
    }

    private void loadBiomeItem(ConfigurationSection section)
    {
        String targetname = getTargetName(section);
        int invSlot = section.getInt("invslot");
        int cost = getCost(section);
        String permission = section.getString("permission");
        Biome target = Biome.valueOf(section.getString("target"));
        ItemStack item = loadItem(section);
        item = Util.setPlaceHolders(item, String.valueOf(cost), targetname, "", "", "");
        BiomeItem biomeItem = new BiomeItem(item, invSlot, ItemType.BIOME, target, cost, permission);
        biomeItems.add(biomeItem);
        mainGuiItems.add(biomeItem);
    }

    private void loadWorldItem(ConfigurationSection section)
    {
        String targetname = getTargetName(section);
        int invSlot = section.getInt("invslot");
        int cost = getCost(section);
        String permission = section.getString("permission");
        String worldName = section.getString("target");
        World target = Bukkit.getWorld(worldName);
        ItemStack item = loadItem(section);
        item = Util.setPlaceHolders(item, String.valueOf(cost), "", targetname, "", "");
        WorldItem worldItem = new WorldItem(item, invSlot, ItemType.WORLD, target, cost, permission);
        worldItems.add(worldItem);
        mainGuiItems.add(worldItem);
    }

    private void loadRangeSelectorItem(ConfigurationSection section)
    {
        ItemStack item = loadItem(section);
        int invSlot = section.getInt("invslot");
        RangeSelectorItem rangeSelectorItem = new RangeSelectorItem(item, invSlot, ItemType.RANGESELECTOR);
        mainGuiItems.add(rangeSelectorItem);
    }

    private ItemStack loadItem(ConfigurationSection section)
    {
        String materialData = section.getString("material");
        Material material = Material.valueOf(materialData.split(":")[0]);
        String name = ChatColor.translateAlternateColorCodes('&', section.getString("name"));
        List<String> lore = getlore(section);
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

    private List<String> getlore(ConfigurationSection section)
    {
        List<String> lore;
        if(section.getKeys(false).contains("lore"))
            lore = section.getStringList("lore");
        else
        {
           lore = section.getParent().getStringList("standardlore");
        }

        for(String loreString : lore)
        {
            lore.remove(loreString);
            loreString = ChatColor.translateAlternateColorCodes('&', loreString);
            lore.add(loreString);
        }

        return lore;
    }

    private String getTargetName(ConfigurationSection section)
    {
        if(section.getKeys(false).contains("targetname"))
            return ChatColor.translateAlternateColorCodes('&', section.getString("targetname"));
        return "";
    }

    private int getCost(ConfigurationSection section)
    {
        if(section.getKeys(false).contains("cost"))
            return section.getInt("cost");
        return 0;
    }

    public static List<BiomeItem> getBiomeItems() {
        return biomeItems;
    }

    public static List<WorldItem> getWorldItems() {
        return worldItems;
    }

    public static List<RangeItem> getRangeItems() {
        return rangeItems;
    }

    public static List<AbstractItem> getMainGuiItems() {
        return mainGuiItems;
    }
}
