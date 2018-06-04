package me.drakonn.wild.portal;

import me.drakonn.wild.Wild;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPortal implements Listener
{
    private static List<AbstractPortal> portals = new ArrayList<>();
    private List<Location> portalLoc;
    private Material type;
    private String name;

    public AbstractPortal(String name, Location maxLox, Location minLoc, Material type) {
        Wild.getInstance().getServer().getPluginManager().registerEvents(this, Wild.getInstance());
        this.portalLoc = generatePortal(maxLox, minLoc);
        this.type = type;
        this.name = name;
        portals.add(this);
    }

    public static final AbstractPortal getPortal(String name) {
        return portals.stream().filter(portal -> portal.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    private List<Location> generatePortal(Location maxLoc, Location minLoc)
    {
        List<Location> portalLocs = new ArrayList<>();;

        int topBlockX = (minLoc.getBlockX() < maxLoc.getBlockX() ? maxLoc.getBlockX() : minLoc.getBlockX());
        int bottomBlockX = (minLoc.getBlockX() > maxLoc.getBlockX() ? maxLoc.getBlockX() : minLoc.getBlockX());

        int topBlockY = (minLoc.getBlockY() < maxLoc.getBlockY() ? maxLoc.getBlockY() : minLoc.getBlockY());
        int bottomBlockY = (minLoc.getBlockY() > maxLoc.getBlockY() ? maxLoc.getBlockY() : minLoc.getBlockY());

        int topBlockZ = (minLoc.getBlockZ() < maxLoc.getBlockZ() ? maxLoc.getBlockZ() : minLoc.getBlockZ());
        int bottomBlockZ = (minLoc.getBlockZ() > maxLoc.getBlockZ() ? maxLoc.getBlockZ() : minLoc.getBlockZ());

        for(int x = bottomBlockX; x <= topBlockX; x++)
        {
            for(int z = bottomBlockZ; z <= topBlockZ; z++)
            {
                for(int y = bottomBlockY; y <= topBlockY; y++)
                {
                    Location location = new Location(minLoc.getWorld(), x, y, z);
                    portalLocs.add(location);
                }
            }
        }

        return portalLocs;
    }

    public static List<AbstractPortal> getPortals() {
        return portals;
    }

    public static void setPortals(List<AbstractPortal> portals) {
        AbstractPortal.portals = portals;
    }

    public List<Location> getPortalLoc() {
        return portalLoc;
    }

    public void setPortalLoc(List<Location> portalLoc) {
        this.portalLoc = portalLoc;
    }

    public Material getType() {
        return type;
    }

    public void setType(Material type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
