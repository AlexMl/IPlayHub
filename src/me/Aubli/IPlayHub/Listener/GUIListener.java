package me.Aubli.IPlayHub.Listener;

import java.util.ArrayList;
import java.util.List;

import me.Aubli.IPlayHub.IPlayHub;
import me.Aubli.IPlayHub.IPlayHubMessages;
import me.Aubli.IPlayHub.IPlayHubPermissions;
import me.Aubli.IPlayHub.Hub.HubManager;
import me.Aubli.IPlayHub.Hub.HubPoint;
import me.Aubli.IPlayHub.Hub.WorldHub;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class GUIListener implements Listener {
    
    @EventHandler
    public void onInventoryInteraction(InventoryClickEvent event) {
	Player eventPlayer = (Player) event.getWhoClicked();
	String invName = event.getInventory().getName();
	
	ItemStack eventItem = event.getCurrentItem();
	
	if (invName.startsWith(IPlayHub.getPluginPrefix())) {
	    event.setCancelled(true);
	    invName = invName.substring(IPlayHub.getPluginPrefix().length() + 1);
	    
	    if (event.getSlot() == -999) {
		eventPlayer.closeInventory();
		return;
	    }
	    
	    if (eventItem != null && eventItem.getType() != Material.AIR) {
		if (IPlayHubPermissions.hasPermission(eventPlayer, IPlayHubPermissions.Teleport)) {
		    if (invName.equals("Hubs")) {
			WorldHub hub = HubManager.getManager().getHub(eventItem.getItemMeta().getDisplayName());
			
			if (hub != null && hub.isEnabled()) {
			    Inventory hubInv = Bukkit.createInventory(eventPlayer, (int) (Math.ceil((hub.getTeleportPoints().length + (eventPlayer.hasPermission(hub.getSpawnPoint().getPermNode()) ? 1 : 0)) / 9.0) * 9), IPlayHub.getPluginPrefix() + " Hub Points!");
			    List<String> lore = new ArrayList<String>();
			    
			    if (eventPlayer.hasPermission(hub.getSpawnPoint().getPermNode())) {
				ItemStack spawn = new ItemStack(Material.MINECART);
				ItemMeta spawnMeta = spawn.getItemMeta();
				spawnMeta.setDisplayName("Spawn");
				
				lore.add(ChatColor.LIGHT_PURPLE + "Teleport to Spawn in " + ChatColor.GOLD + hub.getName() + ChatColor.LIGHT_PURPLE + "!");
				spawnMeta.setLore(lore);
				spawn.setItemMeta(spawnMeta);
				hubInv.addItem(spawn);
			    }
			    
			    for (HubPoint point : hub.getTeleportPoints()) {
				if (eventPlayer.hasPermission(point.getPermNode())) {
				    ItemStack pointItem = new ItemStack(Material.BOAT);
				    ItemMeta pointItemMeta = pointItem.getItemMeta();
				    pointItemMeta.setDisplayName(point.getName());
				    
				    lore.clear();
				    lore.add(ChatColor.LIGHT_PURPLE + "Teleport to " + ChatColor.YELLOW + point.getName() + ChatColor.LIGHT_PURPLE + " in " + ChatColor.GOLD + hub.getName() + ChatColor.LIGHT_PURPLE + "!");
				    pointItemMeta.setLore(lore);
				    pointItem.setItemMeta(pointItemMeta);
				    hubInv.addItem(pointItem);
				}
			    }
			    eventPlayer.closeInventory();
			    eventPlayer.openInventory(hubInv);
			    return;
			}
		    } else if (invName.equals("Hub Points!")) {
			String hubName = ChatColor.stripColor(eventItem.getItemMeta().getLore().get(0).split("in ")[1].replace("!", ""));
			
			WorldHub hub = HubManager.getManager().getHub(hubName);
			if (hub != null && hub.isEnabled()) {
			    HubPoint tpPoint = hub.getTeleportPoint(eventItem.getItemMeta().getDisplayName());
			    if (tpPoint != null) {
				IPlayHubMessages.sendMessage(eventPlayer, IPlayHubMessages.teleporting);
				eventPlayer.closeInventory();
				hub.teleport(tpPoint, eventPlayer);
				return;
			    } else if (eventItem.getItemMeta().getDisplayName().equals("Spawn") && eventItem.getType() == Material.MINECART) {
				IPlayHubMessages.sendMessage(eventPlayer, IPlayHubMessages.teleporting);
				eventPlayer.closeInventory();
				hub.teleport(hub.getSpawnPoint(), eventPlayer);
				return;
			    }
			}
		    }
		} else {
		    eventPlayer.closeInventory();
		    return;
		}
	    }
	}
    }
}
