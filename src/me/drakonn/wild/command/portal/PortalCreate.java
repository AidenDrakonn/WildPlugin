package me.drakonn.wild.command.portal;

import com.sk89q.worldedit.bukkit.selections.Selection;
import me.drakonn.wild.Wild;
import me.drakonn.wild.portal.Portal;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class PortalCreate {

    public void runCommand(Player player, String[] args)
    {
        if(args.length != 3 && args.length != 4)
            return;

        String name = args[2];
        if(Wild.getInstance().worldEdit == null)
        {
            player.sendMessage("§7[§bWild§7] §fWorldedit must be installed to create portals");
            return;
        }

        Selection selection = Wild.getInstance().getWorldEdit().getSelection(player);
        if(selection == null)
        {
            player.sendMessage("§7[§bWild§7] §fNo worldedit selection");
            return;
        }

        if(selection.getWidth() == 1 || selection.getHeight() == 1 || selection.getLength() == 1)
        {
            Material material = Material.ENDER_PORTAL;
            if(args.length == 4 && isMaterial(args[3]))
            {
                material = getMaterial(args[3]);
            }
            Location minLoc = selection.getMinimumPoint();
            Location maxLoc = selection.getMaximumPoint();
            Portal portal = new Portal(name, maxLoc, minLoc, material);

            for(Location location : portal.getPortalLoc())
            {
                location.getBlock().setType(material);
            }
        }
        else
        {
            player.sendMessage("§7[§bWild§7] §fPortals must only be one block wide!");
            return;
        }

        player.sendMessage("§7[§bWild§7] §fPortal has been created!");
    }

    private boolean isMaterial(String string)
    {
        return Arrays.asList("water", "netherportal", "nether", "endportal", "endgateway", "end").contains(string.toLowerCase());
    }

    private Material getMaterial(String string)
    {
        if(string.equalsIgnoreCase("water"))
            return Material.WATER;

        if(string.equalsIgnoreCase("netherportal") || string.equalsIgnoreCase("nether"))
            return Material.PORTAL;

        if(string.equalsIgnoreCase("end") || string.equalsIgnoreCase("endportal"))
            return Material.ENDER_PORTAL;

        if(string.equalsIgnoreCase("endgateway"))
        {
            String version = Wild.getInstance().getServer().getVersion();
            if(version.contains("1.9") || version.contains("1.10") || version.contains("1.11") || version.contains("1.12"))
                return Material.END_GATEWAY;
            else
                return Material.ENDER_PORTAL;
        }

        return Material.ENDER_PORTAL;
    }
}
