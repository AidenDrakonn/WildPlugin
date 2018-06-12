package me.drakonn.wild.gui.inventory;

import me.drakonn.wild.datamanager.ConfigManager;
import me.drakonn.wild.datamanager.ItemManager;
import me.drakonn.wild.gui.item.RangeItem;
import me.drakonn.wild.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RangeInventory {

    public void openInventory(Player player)
    {
        List<RangeItem> rangeItems = ItemManager.getRangeItems();
        Inventory inv = Bukkit.createInventory(null, ItemManager.rangeInvSize, ItemManager.rangeInvName);

        for(RangeItem rangeItem : rangeItems)
        {
            ItemStack item = rangeItem.getItem();
            if(!player.hasPermission(rangeItem.getPermission()))
                item = Util.changeAccess(item, "false");

            if(player.hasPermission(rangeItem.getPermission()))
                item = Util.changeAccess(item, "true");

            inv.setItem(rangeItem.getInvSlot(), item);

        }
    }

}
