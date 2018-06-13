package me.drakonn.wild.datamanager;

import me.drakonn.wild.Wild;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

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
    public static String targetOffline;
    public static List<String> help = new ArrayList<>();

    private Wild plugin;
    public MessageManager(Wild plugin) {
        this.plugin = plugin;
    }

    public void loadMessages()
    {
        FileConfiguration config = plugin.getConfig();
        teleportationStarted = ChatColor.translateAlternateColorCodes('&', config.getString("messages.teleportationstarted"));
        teleportationStarted = teleportationStarted.replaceAll("%tpdelay%", String.valueOf(ConfigManager.teleportDelay));
        teleported = ChatColor.translateAlternateColorCodes('&', config.getString("messages.teleported"));
        alreadyTeleporting = ChatColor.translateAlternateColorCodes('&', config.getString("messages.alreadyteleporting"));
        teleportationCancelled = ChatColor.translateAlternateColorCodes('&', config.getString("messages.teleportationcancelled"));
        onCooldown = ChatColor.translateAlternateColorCodes('&', config.getString("messages.oncooldown"));
        invalidCommand = ChatColor.translateAlternateColorCodes('&', config.getString("messages.invalidcommand"));
        noPermission = ChatColor.translateAlternateColorCodes('&', config.getString("messages.nopermission"));
        noLocationFound = ChatColor.translateAlternateColorCodes('&', config.getString("messages.nolocationfound"));
        insufficientFunds = ChatColor.translateAlternateColorCodes('&', config.getString("messages.insufficientfunds"));
        targetOffline = ChatColor.translateAlternateColorCodes('&', config.getString("messages.targetoffline"));
        setHelp();
    }

    private void setHelp()
    {
        help.add("§7----------§2§lWild§7----------");
        help.add("§b/Wild §8- §fTeleports player or opens gui");
        help.add("§b/Wild gui §8- §fOpens teleportation gui");
        help.add("§b/Wild (player) §8- §fRuns /wild on defined player");
        help.add("§b/Wild portal list §8- §fShows a list of portals");
        help.add("§b/Wild portal create (name) [type] §8- §fCreates a portal in current WorldEdit selection");
        help.add("§b/Wild portal delete (name) §8- §fDeletes the specified portal");
        help.add("§b/Wild portal rename (oldname) (newname) §8- §fRenames a portal");
        help.add("§b/Wild reload §8- §fReloads config file");
    }
}
