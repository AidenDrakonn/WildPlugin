package me.drakonn.wild.gui;

import me.drakonn.wild.Wild;
import me.drakonn.wild.datamanager.ConfigManager;
import me.drakonn.wild.datamanager.ItemManager;
import me.drakonn.wild.gui.inventory.RangeInventory;
import me.drakonn.wild.gui.item.AbstractItem;
import me.drakonn.wild.gui.item.BiomeItem;
import me.drakonn.wild.gui.item.ItemType;
import me.drakonn.wild.gui.item.WorldItem;
import me.drakonn.wild.teleportation.TeleportationManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MainInvHandler implements Listener
{
    private RangeInventory rangeInventory = new RangeInventory();
    private TeleportationManager teleportationManager = new TeleportationManager();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if(!(event.getWhoClicked() instanceof Player))
            return;

        if(event.getClickedInventory() == null)
            return;

        if(!event.getClickedInventory().getTitle().equals(ItemManager.mainInvName))
            return;

        if(event.getClickedInventory().getSize() != ItemManager.mainInvSize)
            return;

        Player player = (Player)event.getWhoClicked();

        event.setCancelled(true);
        player.updateInventory();

        ItemStack clickedItem = event.getCurrentItem();

        if(clickedItem == null || clickedItem.getType().equals(Material.AIR))
            return;

        if(clickedItem.getType().equals(ItemManager.noPermissionMaterial))
            return;

        AbstractItem abstractItem = AbstractItem.getAbstractItem(clickedItem);

        if(abstractItem.getType().equals(ItemType.RANGESELECTOR)) {
            rangeInventory.openInventory(player);
            return;
        }

        int minRange = getMin(player);
        int maxRange = getMax(player);

        if(abstractItem.getType().equals(ItemType.WORLD)) {
            WorldItem worldItem = (WorldItem)abstractItem;
            teleportationManager.teleportPlayer(player, worldItem.getTarget(), null, maxRange, minRange, worldItem.getCost());
            player.closeInventory();
            return;
        }

        if(abstractItem.getType().equals(ItemType.BIOME)) {
            BiomeItem biomeItem = (BiomeItem) abstractItem;
            teleportationManager.teleportPlayer(player, ConfigManager.defaultWorld, biomeItem.getTarget(), maxRange, minRange, biomeItem.getCost());
            player.closeInventory();
            return;
        }

        System.out.println("[RandomTP] Potential error in main gui");
    }

    private int getMax(Player player)
    {
        if(Wild.getInstance().maxRanges.containsKey(player.getUniqueId()))
            return Wild.getInstance().maxRanges.get(player.getUniqueId());
        return ConfigManager.range;
    }

    private int getMin(Player player)
    {
        if(Wild.getInstance().minRanges.containsKey(player.getUniqueId()))
            return Wild.getInstance().minRanges.get(player.getUniqueId());
        return 100;
    }
}
