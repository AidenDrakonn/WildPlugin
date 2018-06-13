package me.drakonn.wild.command;

import me.drakonn.wild.Wild;
import me.drakonn.wild.command.portal.PortalCommand;
import me.drakonn.wild.datamanager.ConfigManager;
import me.drakonn.wild.datamanager.MessageManager;
import me.drakonn.wild.gui.inventory.MainInventory;
import me.drakonn.wild.teleportation.TeleportationManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor {

    private ConsoleCommand consoleCommand = new ConsoleCommand();
    private MainInventory mainInventory = new MainInventory();
    private TeleportationManager teleportationManager = new TeleportationManager();
    private PortalCommand portalCommand = new PortalCommand();

    private int range = ConfigManager.range;
    private World defaultWorld = ConfigManager.defaultWorld;
    private int cost = ConfigManager.cost;

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args)
    {
        if(!command.getLabel().equalsIgnoreCase("wild"))
            return true;
            
        if(!(commandSender instanceof Player)) {
            consoleCommand.runCommand(args);
            return true;
        }

        Player player = (Player)commandSender;

        if(Wild.getInstance().minRanges.containsKey(player.getUniqueId()))
            Wild.getInstance().minRanges.remove(player.getUniqueId());

        if(Wild.getInstance().maxRanges.containsKey(player.getUniqueId()))
            Wild.getInstance().maxRanges.remove(player.getUniqueId());

        if(args.length == 0) {
            teleportCommand(player);
            return true;
        }

        if(args[0].equalsIgnoreCase("portal")) {
            portalCommand.runCommand(player, args);
            return true;
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("help"))
        {
            runHelp(player);
            return true;
        }

        if(args.length == 1)
        {
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                String message = MessageManager.targetOffline;
                message = message.replaceAll("%player%", args[0]);
                player.sendMessage(message);
                return true;
            }
            teleportCommand(player);
        }


        return true;
    }

    private void teleportCommand(Player player)
    {
        if(Wild.getInstance().teleporting.contains(player.getUniqueId())) {
            player.sendMessage(MessageManager.alreadyTeleporting);
            return;
        }

        if(player.hasPermission("wild.gui")) {
            Bukkit.broadcastMessage("Opening gui");
            mainInventory.openInventory(player);
            return;
        }

        if(player.hasPermission("wild.teleport")) {
            teleportationManager.teleportPlayer(player, defaultWorld, null, range, 1000, cost);
            Bukkit.broadcastMessage("teleporting");
        }

        player.sendMessage(MessageManager.noPermission);
    }

    private void runHelp(Player player)
    {
        for(String string : MessageManager.help)
        {
            player.sendMessage(string);
        }
    }
}
