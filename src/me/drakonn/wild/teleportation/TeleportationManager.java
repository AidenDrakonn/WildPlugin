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
    private TeleportationDelay teleportationDelay = Wild.getInstance().teleportationDelay;
    private TeleportationCooldown teleportationCooldown = Wild.getInstance().teleportationCooldown;


    public void teleportPlayer(Player player, World worldTarget, Biome biomeTarget, int maxRange, int minRange, int cost)
    {
        player.sendMessage(MessageManager.teleportationStarted);
        Wild.getInstance().teleporting.add(player.getUniqueId());

        new BukkitRunnable()
        {
            int i;
            @Override
            public void run()
            {
                i++;
                Location location = Util.getRandomLocation(worldTarget, maxRange);
                Biome biome = location.getBlock().getBiome();

                if(i >= 220)
                {
                    player.sendMessage(MessageManager.noLocationFound);
                    cancel();
                }

                if(!Util.isValidLocation(location, minRange))
                    return;

                if(biomeTarget != null && !biome.equals(biomeTarget))
                    return;

                if(Wild.getInstance().getEconomy() != null) {
                    Economy economy = Wild.getInstance().getEconomy();
                    double balance = economy.getBalance(player);
                    if (balance <= cost) {
                        player.sendMessage(MessageManager.insufficientFunds);
                        Wild.getInstance().teleporting.remove(player.getUniqueId());
                        cancel();
                    }
                }

                boolean runDelay = teleportationDelay.runDelay(player);
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                    if(!runDelay)
                        cancel();

                    if(Wild.getInstance().getEconomy() != null)
                    {
                        Economy economy = Wild.getInstance().getEconomy();
                        economy.withdrawPlayer(player, cost);
                    }

                    player.teleport(location);
                    Wild.getInstance().teleporting.remove(player.getUniqueId());
                    player.sendMessage(getTeleportedMessage(location));
                    teleportationCooldown.runCooldown(player);
                    cancel();
                    }
                }.runTaskLaterAsynchronously(Wild.getInstance(), (ConfigManager.teleportDelay*20)+5);

                if(!runDelay) {
                    Wild.getInstance().teleporting.remove(player.getUniqueId());
                    cancel();
                }
            }
        }.runTaskTimer(Wild.getInstance(), 1, 1);
    }

    private String getTeleportedMessage(Location location)
    {
        String message = MessageManager.teleported;
        message = message.replaceAll("%x%", Integer.toString(location.getBlockX()));
        message = message.replaceAll("%y%", Integer.toString(location.getBlockY()));
        message = message.replaceAll("%z%", Integer.toString(location.getBlockZ()));
        return message;
    }
}
