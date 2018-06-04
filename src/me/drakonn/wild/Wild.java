package me.drakonn.wild;

import me.clip.placeholderapi.PlaceholderAPI;
import me.drakonn.wild.command.Command;
import me.drakonn.wild.util.Util;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

public class Wild extends JavaPlugin
{
    private static Economy economy;
    private static PlaceholderAPI placeholderAPI;
    private static Wild instance;

    public void onEnable()
    {
        loadEconomy();
        loadPlaceholderApi();
        saveDefaultConfig();
        registerListeners();
        getCommand("wild").setExecutor(new Command());
        instance = this;
    }

    private void registerListeners()
    {

    }

    private void loadEconomy()
    {
        if(getServer().getPluginManager().getPlugin("Vault") != null)
            economy = Util.setupEconomy(this);
    }

    private void loadPlaceholderApi()
    {
        if(getServer().getPluginManager().getPlugin("PlaceHolderAPI") != null)
            placeholderAPI = 
    }


}
