package me.drakonn.wild.datamanager;

import me.drakonn.wild.Wild;
import me.drakonn.wild.portal.Portal;
import me.drakonn.wild.util.Util;
import org.bukkit.Location;
import org.bukkit.Material;
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
        Set<String> portalNames = portalcfg.getConfigurationSection("portal").getKeys(false);
        for(String portalName : portalNames)
        {
            String minLocAsString = portalcfg.getString("portal."+portalName+".minloc");
            Location minLoc = Util.stringToLocation(plugin, minLocAsString);
            String maxLocAsString = portalcfg.getString("portal."+portalName+".maxloc");
            Location maxLoc = Util.stringToLocation(plugin, maxLocAsString);
            Material type = Material.valueOf(portalcfg.getString("portal."+portalName+".type"));

            new Portal(portalName, minLoc, maxLoc, type);
        }
    }

    public void saveData()
    {

    }

}
