package me.drakonn.wild.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args)
    {
        return true;
    }
}