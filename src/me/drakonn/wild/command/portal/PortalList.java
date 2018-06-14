package me.drakonn.wild.command.portal;

import me.drakonn.wild.portal.AbstractPortal;
import org.bukkit.entity.Player;

public class PortalList {

    public void runCommand(Player player, String[] args)
    {
        player.sendMessage("§7-----§bPortals§7-----");
        int i = 0;
        for(AbstractPortal portal : AbstractPortal.getPortals())
        {
            i++;
            String name = portal.getName();
            player.sendMessage("§b"+Integer.toString(i) + " §7- §f" + name);
        }
        player.sendMessage("§7------------------");
    }
}
