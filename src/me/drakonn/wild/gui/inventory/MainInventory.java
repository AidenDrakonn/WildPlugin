package me.drakonn.wild.gui.inventory;

import me.drakonn.wild.datamanager.ItemManager;
import me.drakonn.wild.gui.item.AbstractItem;
import me.drakonn.wild.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MainInventory
{
    public void openInventory(Player player)
    {
        List<AbstractItem> guiItems = ItemManager.getMainGuiItems();
        Inventory inv = Bukkit.createInventory(null, ItemManager.mainInvSize, ItemManager.mainInvName);

        for(AbstractItem guiItem : guiItems)
        {
            ItemStack item = guiItem.getItem();
            if(!player.hasPermission(guiItem.getPermission()))
                item = Util.changeAccess(item, "false");

            if(player.hasPermission(guiItem.getPermission()))
                item = Util.changeAccess(item, "true");

            Util.setRangePlaceHolders(item, player);

            inv.setItem(guiItem.getInvSlot(), item);
        }

        player.openInventory(inv);
    }
}
