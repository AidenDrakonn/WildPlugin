package me.drakonn.wild.teleportation;

import me.drakonn.wild.Wild;
import me.drakonn.wild.datamanager.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class TeleportationCooldown {

    public HashMap<UUID, Double> coolDown = new HashMap<>();

    public void runCooldown(Player player)
    {
        UUID uuid = player.getUniqueId();
        coolDown.put(uuid, (double) ConfigManager.cooldown*2);
        new BukkitRunnable()
        {
            @Override
            public void run() {
                coolDown.put(uuid, coolDown.get(uuid) - 0.5);
            }
        }.runTaskTimerAsynchronously(Wild.getInstance(),  0,10);
    }
}
