package me.drakonn.wild.datamanager;

import me.drakonn.wild.Wild;
import me.drakonn.wild.portal.AbstractPortal;
import me.drakonn.wild.portal.Portal;
import me.drakonn.wild.util.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class FileManager
{
    private File portalfile;
    private FileConfiguration portalcfg;

    private Wild plugin;
    public FileManager(Wild plugin)
    {
        this.plugin = plugin;
    }

    public void setupFile()
    {
        if(!plugin.getDataFolder().exists())
        {
            plugin.getDataFolder().mkdir();
        }

        portalfile = new File(plugin.getDataFolder(), "portals.yml");
        if(!portalfile.exists())
        {
            try
            {
                portalfile.createNewFile();
            }

            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        portalcfg = YamlConfiguration.loadConfiguration(portalfile);
    }

    public void loadData()
    {
        ConfigurationSection portalSection = portalcfg.getConfigurationSection("portal");
        for(String portalName : portalSection.getKeys(false))
        {
            ConfigurationSection section = portalSection.getConfigurationSection(portalName);
            Location minLoc = Util.stringToLocation(plugin, section.getString("minloc"));
            Location maxLoc = Util.stringToLocation(plugin, section.getString("maxloc"));
            Material type = Material.valueOf(section.getString("type"));

            new Portal(portalName, minLoc, maxLoc, type);
        }
    }

    public void saveData()
    {
        ConfigurationSection topSection = portalcfg.createSection("portal");
        for(AbstractPortal portal : AbstractPortal.getPortals())
        {
            ConfigurationSection section = topSection.createSection(portal.getName());
            String minLocAsString = Util.locationToString(portal.getMinLoc());
            String maxLocAsString = Util.locationToString(portal.getMaxLoc());
            Material type = portal.getType();
            section.set("minloc", minLocAsString);
            section.set("maxloc", maxLocAsString);
            section.set("type", type.toString());
        }
    }

}
