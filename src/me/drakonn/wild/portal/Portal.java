package me.drakonn.wild.portal;

import me.drakonn.wild.datamanager.ConfigManager;
import me.drakonn.wild.teleportation.TeleportationManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;


public class Portal extends AbstractPortal
{
    private TeleportationManager teleportationManager = new TeleportationManager();
    public Portal(String name, Location maxLoc, Location minLoc, Material type)
    {
        super(name, maxLoc, minLoc, type);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if(event.getFrom().getBlock().getLocation().equals(event.getTo().getBlock().getLocation()))
            return;

        if(!event.getTo().getWorld().equals(getPortalLoc().get(0).getWorld()))
            return;

        if(getPortalLoc().contains(event.getFrom().getBlock().getLocation()))
            return;

        if(!getPortalLoc().contains(event.getTo().getBlock().getLocation()))
            return;

        Player player = event.getPlayer();
        teleportationManager.teleportPlayer(player, ConfigManager.defaultWorld, null, ConfigManager.range, 100, ConfigManager.cost);
    }

}
