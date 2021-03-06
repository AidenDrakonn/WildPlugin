package me.drakonn.wild.gui;

import me.drakonn.wild.Wild;
import me.drakonn.wild.datamanager.ItemManager;
import me.drakonn.wild.gui.inventory.MainInventory;
import me.drakonn.wild.gui.item.AbstractItem;
import me.drakonn.wild.gui.item.ItemType;
import me.drakonn.wild.gui.item.RangeItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class RangeInvHandler implements Listener
{
    private MainInventory mainInventory = new MainInventory();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if(!(event.getWhoClicked() instanceof Player))
            return;

        if(event.getClickedInventory() == null)
            return;

        if(!event.getClickedInventory().getTitle().equals(ItemManager.rangeInvName))
            return;

        if(event.getClickedInventory().getSize() != ItemManager.rangeInvSize)
            return;

        Player player = (Player)event.getWhoClicked();

        event.setCancelled(true);
        player.updateInventory();

        ItemStack clickedItem = event.getCurrentItem();

        if(clickedItem == null || clickedItem.getType().equals(Material.AIR))
            return;

        if(clickedItem.getType().equals(ItemManager.noPermissionMaterial))
            return;

        if(!(AbstractItem.getAbstractItem(clickedItem).getType().equals(ItemType.RANGE)))
            return;

        RangeItem rangeItem = (RangeItem)AbstractItem.getAbstractItem(clickedItem);
        Wild.getInstance().minRanges.put(player.getUniqueId(), rangeItem.getMinRange());
        Wild.getInstance().maxRanges.put(player.getUniqueId(), rangeItem.getMaxRange());

        new BukkitRunnable()
        {
        public void run() {
            mainInventory.openInventory(player);
        }
        }.runTaskLater(Wild.getInstance(), 3);
    }
}
