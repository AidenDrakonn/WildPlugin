package me.drakonn.wild.teleportation;

import me.drakonn.wild.Wild;
import me.drakonn.wild.datamanager.ConfigManager;
import me.drakonn.wild.datamanager.MessageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TeleportationDelay implements Listener {

    private HashMap<UUID, Integer> waitingToTeleport = new HashMap<>();
    private List<UUID> didMove = new ArrayList<>();

    public boolean runDelay(Player player)
    {
        waitingToTeleport.put(player.getUniqueId(), ConfigManager.teleportDelay*2);
        new BukkitRunnable()
        {
            @Override
            public void run() {
            if(didMove.contains(player.getUniqueId())) {
                player.sendMessage(MessageManager.teleportationCancelled);
                cancel();
            }

            if(waitingToTeleport.get(player.getUniqueId()) == 0)
                cancel();

            waitingToTeleport.put(player.getUniqueId(), waitingToTeleport.get(player.getUniqueId()) - 1);
            }
        }.runTaskTimerAsynchronously(Wild.getInstance(), 1, 10);

        waitingToTeleport.remove(player.getUniqueId());
        if(didMove.contains(player.getUniqueId()))
            return true;
        else
            return false;
    }

    @EventHandler
    public void onMoveEvent(PlayerMoveEvent event)
    {
        if(event.getFrom().getBlock().getLocation().equals(event.getTo().getBlock().getLocation()))
            return;

        if(!waitingToTeleport.containsKey(event.getPlayer().getUniqueId()))
            return;

        didMove.add(event.getPlayer().getUniqueId());
        new BukkitRunnable()
        {
            @Override
            public void run() {
            didMove.remove(event.getPlayer().getUniqueId());
            }
        }.runTaskLater(Wild.getInstance(), 20);

    }
}