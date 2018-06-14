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
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ItemManager
{
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
        config = plugin.getItemConfig();
    }

    public void loadItems()
    {
        rangeItems.clear();
        mainGuiItems.clear();
        ConfigurationSection mainGui = config.getConfigurationSection("maingui");
        ConfigurationSection rangeGui = config.getConfigurationSection("rangegui");
        for(String path : mainGui.getKeys(false))
        {
            if(path.equalsIgnoreCase("standardlore"))
                continue;

            ConfigurationSection section = mainGui.getConfigurationSection(path);
            if (section.getString("type").equalsIgnoreCase("biome"))
                loadBiomeItem(section);

            if (section.getString("type").equalsIgnoreCase("world"))
                loadWorldItem(section);

            if (section.getString("type").equalsIgnoreCase("rangeselector"))
                loadRangeSelectorItem(section);
        }

        for(String path : rangeGui.getKeys(false))
        {
            if(path.equalsIgnoreCase("standardlore"))
                continue;

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
        int invSlot = getInvSlot(section);
        String permission = getPermission(section);
        ItemStack item = loadItem(section);
        item = Util.setPlaceHolders(item, String.valueOf(cost), "", ConfigManager.defaultWorld.getWorldType().toString(), String.valueOf(minRange), String.valueOf(maxRange));
        rangeItems.add(new RangeItem(item, invSlot, ItemType.RANGE, maxRange, minRange, cost, permission));
    }

    private void loadBiomeItem(ConfigurationSection section)
    {
        String targetname = getTargetName(section);
        int invSlot = getInvSlot(section);
        int cost = getCost(section);
        String permission = getPermission(section);
        Biome target = getBiome(section);
        ItemStack item = loadItem(section);
        item = Util.setPlaceHolders(item, String.valueOf(cost), targetname, ConfigManager.defaultWorld.getWorldType().toString(), "0", String.valueOf(ConfigManager.range));
        BiomeItem biomeItem = new BiomeItem(item, invSlot, ItemType.BIOME, target, cost, permission);
        mainGuiItems.add(biomeItem);
    }

    private void loadWorldItem(ConfigurationSection section)
    {
        String targetname = getTargetName(section);
        int invSlot = getInvSlot(section);
        int cost = getCost(section);
        String permission = getPermission(section);
        World target = getWorld(section);
        ItemStack item = loadItem(section);
        item = Util.setPlaceHolders(item, String.valueOf(cost), "", targetname, "0", String.valueOf(ConfigManager.range));
        WorldItem worldItem = new WorldItem(item, invSlot, ItemType.WORLD, target, cost, permission);
        mainGuiItems.add(worldItem);
    }

    private void loadRangeSelectorItem(ConfigurationSection section)
    {
        int invSlot = getInvSlot(section);
        ItemStack item = loadItem(section);
        item = Util.setPlaceHolders(item, "0", "", "", "0", String.valueOf(ConfigManager.range));
        RangeSelectorItem rangeSelectorItem = new RangeSelectorItem(item, invSlot, ItemType.RANGESELECTOR);
        mainGuiItems.add(rangeSelectorItem);
    }

    private ItemStack loadItem(ConfigurationSection section)
    {
        if(section.getKeys(false).containsAll(Arrays.asList("material", "name"))) {
            String materialData = section.getString("material");
            Material material;
            String materialID = materialData.split(":")[0];
            if(Util.isInt(materialID))
                material = Material.getMaterial(Integer.valueOf(materialID));
            else material = Material.getMaterial(materialID);

            if(material == null) {
                System.out.println("[WILD] ERROR - Material for " + section.getName() +" invalid, set to stone");
                material = Material.STONE;
            }
            String name = ChatColor.translateAlternateColorCodes('&', section.getString("name"));
            List<String> lore = getlore(section);
            ItemStack item = Util.createItem(material, name, lore);
            if (materialData.split(":").length == 2) {
                item.setDurability(Short.valueOf(materialData.split(":")[1]));
            }

            return item;
        }

        System.out.println("[WILD] ERROR - " + section.getName() + " Did not contain a material or item name, has been set to stone");
        return new ItemStack(Material.STONE);
    }

    private List<String> getlore(ConfigurationSection section)
    {
        List<String> lore;
        List<String> newLore = new ArrayList<>();
        if(section.getKeys(false).contains("lore"))
            lore = section.getStringList("lore");
        else
        {
            if(section.getParent().getKeys(false).contains("standardlore")) {
                lore = section.getParent().getStringList("standardlore");
                System.out.println("[WILD] No lore found for " + section.getName() + " setting to standard lore");
            }
            else
            {
                System.out.println("[WILD] ERROR - No lore found for " + section.getName() + " please set in config");
                lore = Arrays.asList("&c&l(!) &cerror in config");
            }
        }

        for(String loreString : lore)
        {
            loreString = ChatColor.translateAlternateColorCodes('&', loreString);
            newLore.add(loreString);
        }

        return lore;
    }

    private String getTargetName(ConfigurationSection section)
    {
        if(section.getKeys(false).contains("targetname"))
            return ChatColor.translateAlternateColorCodes('&', section.getString("targetname"));
        System.out.println("[WILD] No targetname set for " + section.getName() + " setting to " + section.getName());
        return section.getName();
    }

    private int getCost(ConfigurationSection section)
    {
        if(section.getKeys(false).contains("cost"))
            return section.getInt("cost");
        System.out.println("[WILD] No cost set for " + section.getName() + " setting cost as 0");
        return 0;
    }

    private int getInvSlot(ConfigurationSection section)
    {
        if(section.getKeys(false).contains("invslot"))
        {
            return section.getInt("invslot");
        }

        System.out.println("[WILD] ERROR - No invslot set for " + section.getName() + " setting to 0");
        return 0;
    }

    private String getPermission(ConfigurationSection section)
    {
        if(section.getKeys(false).contains("permission"))
            return section.getString("permission");

        System.out.println("[WILD] No permission set for " + section.getName() + " setting to wild.gui." +section.getName());
        return "wild.gui."+section.getName();
    }

    private World getWorld(ConfigurationSection section)
    {
        if(section.getKeys(false).contains("target")){
            World world = Bukkit.getWorld(section.getString("target"));
            if(world != null)
                return world;
        }
        System.out.println("[WILD] ERROR - No target world found for " + section.getName() + " setting to default world");
        return ConfigManager.defaultWorld;
    }

    private Biome getBiome(ConfigurationSection section)
    {
        if(section.getKeys(false).contains("target")){
            Biome biome = Biome.valueOf(section.getString("target"));
            if(biome != null)
                return biome;
        }
        System.out.println("[WILD] ERROR - No target biome found for " + section.getName() + " setting to forest");
        return Biome.FOREST;
    }


    public static List<RangeItem> getRangeItems() {
        return rangeItems;
    }

    public static List<AbstractItem> getMainGuiItems() {
        return mainGuiItems;
    }
}
