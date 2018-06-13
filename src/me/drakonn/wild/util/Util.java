package me.drakonn.wild.util;

import me.drakonn.wild.Wild;
import me.drakonn.wild.datamanager.ConfigManager;
import me.drakonn.wild.datamanager.ItemManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Util
{
    public static ItemStack createItem(final Material mat, final int amt, final int durability, final String name,
                                       final List<String> lore) {
        final ItemStack item = new ItemStack(mat, amt);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Util.color(name));
        meta.setLore(Util.color(lore));
        if (durability != 0)
            item.setDurability((short) durability);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(final Material mat, final String name, final List<String> lore) {
        return createItem(mat, 1, 0, name, lore);
    }

    public static String color(final String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> color(final List<String> list) {
        final List<String> colored = new ArrayList<String>();
        for (final String s : list)
            colored.add(color(s));
        return colored;
    }

    public static Economy setupEconomy(final Plugin p) {
        if (p.getServer().getPluginManager().getPlugin("Vault") == null)
            return null;
        final RegisteredServiceProvider<Economy> rsp = p.getServer().getServicesManager()
                .getRegistration(Economy.class);
        return rsp == null ? null : rsp.getProvider();
    }

    public static Location stringToLocation(final Plugin p, final String s) {
        if (p == null || s == null || s.isEmpty())
            return null;
        final String[] args = s.split(",");
        try {
            return new Location(p.getServer().getWorld(args[0].trim()), Double.parseDouble(args[1].trim()),
                    Double.parseDouble(args[2].trim()), Double.parseDouble(args[3].trim()),
                    (float) Double.parseDouble(args[4].trim()), (float) Double.parseDouble(args[5].trim()));
        } catch (final NullPointerException e) {
            return new Location(
                    p.getServer().getWorlds().stream().filter(w -> w.getEnvironment() == World.Environment.NORMAL).findFirst()
                            .get(),
                    Double.parseDouble(args[1].trim()), Double.parseDouble(args[2].trim()),
                    Double.parseDouble(args[3].trim()), (float) Double.parseDouble(args[4].trim()),
                    (float) Double.parseDouble(args[5].trim()));
        } catch (final NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public static String locationToString(final Location l) {
        try {
            return l.getWorld().getName() + "," + round(l.getX()) + "," + round(l.getY()) + "," + round(l.getZ()) + ","
                    + round(l.getYaw()) + "," + round(l.getPitch());
        } catch (final NullPointerException e) {
            return "";
        }
    }

    public static double round(final double num) {
        return (int) (num * 100) / 100.0;
    }

    public static File getFile(final Plugin p, final String name) {
        if (!p.getDataFolder().exists())
            p.getDataFolder().mkdirs();
        final File file = new File(p.getDataFolder(), name);
        try {
            return file.exists() ? file : file.createNewFile() ? file : null;
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String timeFromMillis(final long time) {
        final long seconds = time / 1000;
        final long minutes = seconds / 60;
        final long hours = minutes / 60;
        final long days = hours / 24;
        if (days >= 1L)
            return String.format("%d day(s), %d hour(s), %d min, %d sec", days, hours - days * 24, minutes - hours * 60,
                    seconds - minutes * 60);
        if (hours >= 1L)
            return String.format("%d hour(s), %d min", hours, minutes - hours * 60, seconds - minutes * 60);
        if (minutes >= 1L)
            return String.format("%d min, %d sec", minutes, seconds - minutes * 60);
        return String.format("%d sec", seconds);
    }

    public static Location getRandomLocation(World world, int maxRange)
    {
        int randomX = (int) (long) Math.round(((Math.random() * 2) - 1) * maxRange);
        int randomZ = (int) (long) Math.round(((Math.random() * 2) - 1) * maxRange);
        int y = world.getHighestBlockYAt(randomX, randomZ);
        Bukkit.broadcastMessage(Integer.toString(y));
        return new Location(world, randomX, y, randomZ);
    }

    public static boolean isValidLocation(Location location, int minRange)
    {
        Material blockType = location.getBlock().getType();
        Material blockBelowType = location.add(0,-1,0).getBlock().getType();
        if(isSafe(blockType) && isSafe(blockBelowType) && isInRange(minRange, location)
        && blockType.equals(Material.AIR) && !blockBelowType.equals(Material.AIR))
            return true;
        return false;
    }

    public static boolean isInRange(int minRange, Location location)
    {
        int x = location.getBlockX();
        int z = location.getBlockZ();
        if(x < 0)
            x = Math.abs(x);
        if(z < 0)
            z = Math.abs(z);
        if((x >= minRange || z >= minRange))
            return true;
        return false;
    }

    public static boolean isSafe(Material type)
    {
        if(Arrays.asList(Material.STATIONARY_WATER, Material.STATIONARY_LAVA, Material.LAVA, Material.WATER).contains(type))
            return false;
        return true;

    }

    public static ItemStack changeAccess(ItemStack item, String access)
    {
        List<String> lore = item.getItemMeta().getLore();
        List<String> newLore = new ArrayList<>();
        ItemMeta meta = item.getItemMeta();

        for(String string : lore)
        {
            string = string.replaceAll("%access%", access);
            newLore.add(string);
        }

        meta.setLore(newLore);
        item.setItemMeta(meta);
        if(access.equalsIgnoreCase(ConfigManager.accessFalse))
            item.setType(ItemManager.noPermissionMaterial);
        return item;
    }

    public static ItemStack setRangePlaceHolders(ItemStack item, Player player)
    {
        Bukkit.broadcastMessage("setting range placeholder for " + item.toString());
        List<String> lore = item.getItemMeta().getLore();
        List<String> newLore = new ArrayList<>();
        ItemMeta meta = item.getItemMeta();
        String minRange = "0";
        String maxRange = String.valueOf(ConfigManager.range);
        if(Wild.getInstance().maxRanges.containsKey(player.getUniqueId())) {
            maxRange = String.valueOf(Wild.getInstance().maxRanges.get(player.getUniqueId()));
            Bukkit.broadcastMessage("set maxrange to "+ String.valueOf(Wild.getInstance().maxRanges.get(player.getUniqueId())));
        }

        if(Wild.getInstance().minRanges.containsKey(player.getUniqueId())) {
            minRange = String.valueOf(Wild.getInstance().minRanges.get(player.getUniqueId()));
            Bukkit.broadcastMessage("set minrange to "+ String.valueOf(Wild.getInstance().minRanges.get(player.getUniqueId())));
        }

        for(String string : lore)
        {
            string = string.replaceAll("%currentmin%", minRange);
            string = string.replaceAll("%currentmax%", maxRange);
            newLore.add(string);
        }

        meta.setLore(newLore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack setPlaceHolders(ItemStack item, String cost, String biomeName, String worldName, String minRange, String maxRange)
    {
        List<String> lore = item.getItemMeta().getLore();
        List<String> newLore = new ArrayList<>();
        ItemMeta meta = item.getItemMeta();
        String target;
        target = biomeName;
        if(biomeName.isEmpty())
            target = worldName;

        if(cost.equalsIgnoreCase("0"))
            cost = "free";

        for(String string : lore)
        {
            string = string.replaceAll("%cost%", cost);
            string = string.replaceAll("%biome%", biomeName);
            string = string.replaceAll("%world%", worldName);
            string = string.replaceAll("%target%", target);
            string = string.replaceAll("%minrange%", minRange);
            string = string.replaceAll("%maxrange%", maxRange);
            newLore.add(string);
        }

        meta.setLore(newLore);
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isInt(final String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }



}
