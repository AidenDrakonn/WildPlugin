package me.drakonn.wild.teleportation;

import me.drakonn.wild.Wild;
import me.drakonn.wild.datamanager.ConfigManager;
import me.drakonn.wild.datamanager.MessageManager;
import me.drakonn.wild.util.Util;
import net.milkbowl.vault.economy.Economy;
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
    private TeleportationDelay teleportationDelay = Wild.getInstance().teleportationDelay;
    private TeleportationCooldown teleportationCooldown = Wild.getInstance().teleportationCooldown;


    public void teleportPlayer(Player player, World worldTarget, Biome biomeTarget, int maxRange, int minRange, int cost)
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
        }.runTaskTimerAsynchronously(Wild.getInstance(), 1, 2);

        if(locations.get(player.getUniqueId()) == null)
            return;

        boolean runDelay = teleportationDelay.runDelay(player);
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(runDelay)
                    return;

                if(Wild.getEconomy() != null)
                {
                    Economy economy = Wild.getEconomy();
                    double balance = economy.getBalance(player);
                    if(balance < cost)
                    {
                        player.sendMessage(MessageManager.insufficientFunds);
                        return;
                    }

                    economy.withdrawPlayer(player, cost);
                }

                player.teleport(locations.get(player.getUniqueId()));
                player.sendMessage(MessageManager.teleported);
                teleportationCooldown.runCooldown(player);
            }
        }.runTaskLaterAsynchronously(Wild.getInstance(), ConfigManager.teleportDelay*20+5);
    }
}
