package me.drakonn.wild.teleportation;

import me.drakonn.wild.Wild;
import me.drakonn.wild.datamanager.MessageManager;
import me.drakonn.wild.util.Util;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class TeleportationManager
{
    private HashMap<UUID, Location> locations = new HashMap<>();
    private TeleportationDelay teleportationDelay = new TeleportationDelay();
    private TeleportationCooldown teleportationCooldown = new TeleportationCooldown();


    public void teleportPlayer(Player player, World worldTarget, Biome biomeTarget, int maxRange, int minRange)
    {
        player.sendMessage(MessageManager.teleportationStarted);

        new BukkitRunnable()
        {
            int i;
            @Override
            public void run()
            {
                i++;
                Location testLoc = Util.getRandomLocation(worldTarget, maxRange);
                Biome biome = testLoc.getBlock().getBiome();

                if(Util.isValidLocation(testLoc, minRange))
                {
                    if(biomeTarget == null || biome.equals(biomeTarget)) {
                        locations.put(player.getUniqueId(), testLoc);
                        cancel();
                    }
                }

                if(i >= 300)
                {
                    player.sendMessage(MessageManager.noLocationFound);
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(Wild.getInstance(), 1, 1);

        if(locations.get(player.getUniqueId()) == null)
            return;

        boolean runDelay = teleportationDelay.runDelay(player);
        if(!runDelay)
        {
            player.sendMessage(MessageManager.teleportationCancelled);
            return;
        }

        player.teleport(locations.get(player.getUniqueId()));
        player.sendMessage(MessageManager.teleported);
        teleportationCooldown.runCooldown(player);
    }
}
