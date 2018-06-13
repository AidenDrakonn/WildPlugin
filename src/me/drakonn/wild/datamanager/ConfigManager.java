package me.drakonn.wild.datamanager;

import me.drakonn.wild.Wild;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;

public class ConfigManager {
    public static World defaultWorld;
    public static int range;
    public static int cooldown;
    public static int teleportDelay;
    public static int cost;
    public static String accessTrue;
    public static String accessFalse;

    private Wild plugin;
    public ConfigManager(Wild plugin)
    {
        this.plugin = plugin;
    }

    public void loadData()
    {
        defaultWorld = Bukkit.getWorld(plugin.getConfig().getString("default.world"));
        cooldown = plugin.getConfig().getInt("default.cooldown");
        teleportDelay = plugin.getConfig().getInt("default.teleportdelay");
        cost = plugin.getConfig().getInt("default.cost");
        range = plugin.getConfig().getInt("default.range");
        accessTrue = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("access.true"));
        accessFalse = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("access.false"));
    }
}
