package me.drakonn.wild.datamanager;

import me.drakonn.wild.Wild;
import me.drakonn.wild.gui.item.*;
import me.drakonn.wild.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
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
    private Material noPermissionMaterial;

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
        Set<String> rangePaths = config.getConfigurationSection("guiitem.rangegui").getKeys(false);
        Set<String> mainPaths = config.getConfigurationSection("guiitem.maingui").getKeys(false);
        for(String path : mainPaths)
        {
            if(config.getString("guiitem.maingui."+path+".type").equalsIgnoreCase("biome"))
            {
                loadBiomeItem(path);
            }
            if(config.getString("guiitem.maingui."+path+"type").equalsIgnoreCase("world"))
            {
                loadWorldItem(path);
            }
            if(config.getString("guiitem.maingui."+path+"type").equalsIgnoreCase("rangeselector"))
            {
                loadRangeSelectorItem(path);
            }
        }

        for(String path : rangePaths)
        {
            loadRangeItem(path);
        }

        int mainInvSize = config.getInt("gui.main.size");
        mainInv = Bukkit.createInventory(null, mainInvSize, "Wild");
        for(AbstractItem mainGuiItem : mainGuiItems)
        {
            mainInv.setItem(mainGuiItem.getInvSlot(), mainGuiItem.getItem());
        }

        int rangeInvSize = config.getInt("gui.range.size");
        rangeInv = Bukkit.createInventory(null, rangeInvSize, "Range Selector");
        for(RangeItem rangeItem : rangeItems)
        {
            rangeInv.setItem(rangeItem.getInvSlot(), rangeItem.getItem());
        }

        noPermissionMaterial = Material.valueOf(config.getString("default.nopermissionmaterial"));
    }

    private void loadRangeItem(String path)
    {
        int cost = getCost("guiitem.rangegui."+path);
        int maxRange = config.getInt("guiitem.rangegui."+path+".maxrange");
        int minRange = config.getInt("guiitem.rangegui."+path+".minrange");
        int invSlot = config.getInt("guiitem.rangegui."+path+".invslot");
        ItemStack item = loadItem("guiitem.rangegui."+path);
        rangeItems.add(new RangeItem(item, invSlot, ItemType.RANGE, maxRange, minRange, cost));
    }

    private void loadBiomeItem(String path)
    {
        int invSlot = config.getInt("guiitem.maingui."+path+".invslot");
        int cost = getCost("guiitem.maingui."+path);
        Biome target = Biome.valueOf(config.getString("guiitem.maingui."+path+".target"));
        ItemStack item = loadItem("guiitem.maingui."+path);
        BiomeItem biomeItem = new BiomeItem(item, invSlot, ItemType.BIOME, target, cost);
        biomeItems.add(biomeItem);
        mainGuiItems.add(biomeItem);
    }

    private void loadWorldItem(String path)
    {
        int invSlot = config.getInt("guiitem.maingui."+path+".invslot");
        int cost = getCost("guiitem.maingui."+path);
        String worldName = config.getString("guiitem.maingui."+path+".target");
        World target = Bukkit.getWorld(worldName);
        ItemStack item = loadItem("guiitem.maingui."+path);
        WorldItem worldItem = new WorldItem(item, invSlot, ItemType.WORLD, target, cost);
        worldItems.add(worldItem);
        mainGuiItems.add(worldItem);
    }

    private void loadRangeSelectorItem(String path)
    {
        ItemStack item = loadItem("guiitem.maingui."+path);
        int invSlot = config.getInt("guiitem.maingui."+path+".invslot");
        RangeSelectorItem rangeSelectorItem = new RangeSelectorItem(item, invSlot, ItemType.RANGESELECTOR);
        mainGuiItems.add(rangeSelectorItem);
    }

    private ItemStack loadItem(String path)
    {
        String materialData = config.getString(path+".material");
        Material material = Material.valueOf(materialData.split(":")[0]);
        String name = ChatColor.translateAlternateColorCodes('&', config.getString(path+".name"));
        List<String> lore = config.getStringList(path+".lore");
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

    private int getCost(String path)
    {
        Set<String> subPaths = config.getConfigurationSection(path).getKeys(false);
        if(subPaths.contains("cost"))
            return config.getInt(path+".cost");
        return 0;
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

    public Inventory getMainInv() {
        return mainInv;
    }

    public Inventory getRangeInv() {
        return rangeInv;
    }
}
