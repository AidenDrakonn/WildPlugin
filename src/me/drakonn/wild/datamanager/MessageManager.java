package me.drakonn.wild.datamanager;

import me.drakonn.wild.Wild;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class MessageManager
{
    public static String teleportationStarted;
    public static String teleported;
    public static String alreadyTeleporting;
    public static String teleportationCancelled;
    public static String onCooldown;
    public static String invalidCommand;
    public static String noPermission;
    public static String noLocationFound;
    public static String insufficientFunds;

    private Wild plugin;
    public MessageManager(Wild plugin) {
        this.plugin = plugin;
    }

    public void loadMessages()
    {
        FileConfiguration config = plugin.getConfig();
        teleportationStarted = ChatColor.translateAlternateColorCodes('&', config.getString("messages.teleportationstarted"));
        teleported = ChatColor.translateAlternateColorCodes('&', config.getString("messages.teleported"));
        alreadyTeleporting = ChatColor.translateAlternateColorCodes('&', config.getString("messages.alreadyteleporting"));
        teleportationCancelled = ChatColor.translateAlternateColorCodes('&', config.getString("messages.teleportationcancelled"));
        onCooldown = ChatColor.translateAlternateColorCodes('&', config.getString("messages.oncooldown"));
        invalidCommand = ChatColor.translateAlternateColorCodes('&', config.getString("messages.invalidcommand"));
        noPermission = ChatColor.translateAlternateColorCodes('&', config.getString("messages.nopermission"));
        noLocationFound = ChatColor.translateAlternateColorCodes('&', config.getString("messages.nolocationfound"));
        insufficientFunds = ChatColor.translateAlternateColorCodes('&', config.getString("messages.insufficientfunds"));
    }
}
