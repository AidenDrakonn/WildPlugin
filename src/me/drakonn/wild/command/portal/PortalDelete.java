package me.drakonn.wild.command.portal;

import me.drakonn.wild.portal.AbstractPortal;
import me.drakonn.wild.portal.Portal;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PortalDelete {

    public void runCommand(Player player, String[] args)
    {
        String targetPortal = args[2];

        AbstractPortal abstractPortal = AbstractPortal.getPortal(targetPortal);
        if(abstractPortal == null)
        {
            player.sendMessage("§7[§bWild§7] §fThe target portal does not exist, do /wild portal list to see all portals");
            return;
        }

        Portal portal = (Portal)abstractPortal;
        Material material = portal.getType();
        for(Location location : portal.getPortalLoc())
        {
            if(location.getBlock().getType().equals(material))
            {
                location.getBlock().setType(Material.AIR);
            }
        }

        AbstractPortal.getPortals().remove(abstractPortal);
        player.sendMessage("§7[§bWild§7] §fTarget portal has been removed");
    }
}
