package me.drakonn.wild.portal;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;


public class Portal extends AbstractPortal
{
    public Portal(String name, Location maxLoc, Location minLoc, Material type)
    {
        super(name, maxLoc, minLoc, type);
    }

    @EventHandler
    public void onPlayerMove (PlayerMoveEvent event)
    {
        if(event.getFrom().getBlock().getLocation().equals(event.getTo().getBlock().getLocation()))
            return;

        
    }
}
