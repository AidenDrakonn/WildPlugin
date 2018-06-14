package me.drakonn.wild.command.portal;

import me.drakonn.wild.datamanager.MessageManager;
import me.drakonn.wild.portal.AbstractPortal;
import me.drakonn.wild.portal.Portal;
import org.bukkit.entity.Player;

public class PortalRename {

    public void runCommand(Player player, String[] args)
    {
        if(args.length < 4)
        {
            player.sendMessage(MessageManager.invalidCommand);
            return;
        }

        String targetPortal = args[2];
        String newName = args[3];

        AbstractPortal abstractPortal = AbstractPortal.getPortal(targetPortal);
        if(abstractPortal == null)
        {
            player.sendMessage("§7[§bWild§7] §fThe target portal does not exist, do /wild portal list to see all portals");
            return;
        }
        Portal portal = (Portal)abstractPortal;
        portal.setName(newName);
        player.sendMessage("§7[§bWild§7] §fSet name of §b" + targetPortal + "§f to §b" + newName);
    }
}
