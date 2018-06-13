package me.drakonn.wild.command.portal;

import me.drakonn.wild.datamanager.MessageManager;
import org.bukkit.entity.Player;

public class PortalCommand {

    private PortalList portalList = new PortalList();
    private PortalCreate portalCreate = new PortalCreate();
    private PortalDelete portalDelete = new PortalDelete();
    private PortalRename portalRename = new PortalRename();

    public void runCommand(Player player, String[] args)
    {
        if(args[1].equalsIgnoreCase("list"))
        {
            portalList.runCommand(player, args);
            return;
        }

        if(args[1].equalsIgnoreCase("create"))
        {
            portalCreate.runCommand(player, args);
            return;
        }

        if(args[1].equalsIgnoreCase("delete"))
        {
            portalDelete.runCommand(player, args);
            return;
        }

        if(args[1].equalsIgnoreCase("rename"))
        {
            portalRename.runCommand(player, args);
            return;
        }

        player.sendMessage(MessageManager.invalidCommand);
    }
}
