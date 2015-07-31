package me.Aubli.IPlayHub.Listener;

import java.util.ArrayList;
import java.util.List;

import me.Aubli.IPlayHub.IPlayHubMessages;
import me.Aubli.IPlayHub.IPlayHubPermissions;
import me.Aubli.IPlayHub.Hub.HubManager;
import me.Aubli.IPlayHub.Hub.HubPoint;
import me.Aubli.IPlayHub.Hub.WorldHub;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class GUIListener implements Listener {
    
    @EventHandler
    public void onInventoryInteraction(InventoryClickEvent event) {
	Player eventPlayer = (Player) event.getWhoClicked();
	String invName = event.getInventory().getName();
	
	ItemStack eventItem = event.getCurrentItem();
	
	if (invName.equals("Teleporters by Hub!") || invName.equals("Available Teleporters!")) {
	    event.setCancelled(true);
	    
	    if (event.getSlot() == -999) {
		eventPlayer.closeInventory();
		return;
	    }
	    
	    if (eventItem != null && eventItem.getType() != Material.AIR) {
		if (IPlayHubPermissions.hasPermission(eventPlayer, IPlayHubPermissions.Teleport)) {
		    if (invName.equals("Teleporters by Hub!")) {
			WorldHub hub = HubManager.getManager().getHub(eventItem.getItemMeta().getDisplayName());
			
			if (hub != null && hub.isEnabled()) {
			    Inventory hubInv = Bukkit.createInventory(eventPlayer, (int) (Math.ceil((hub.getTeleportPoints().length + 1) / 9.0) * 9), "Available Teleporters!");
			    
			    ItemStack spawn = new ItemStack(Material.MINECART);
			    ItemMeta spawnMeta = spawn.getItemMeta();
			    spawnMeta.setDisplayName("Spawn");
			    
			    List<String> lore = new ArrayList<String>();
			    lore.add("Teleport to Hub in " + hub.getName() + "!");
			    spawnMeta.setLore(lore);
			    spawn.setItemMeta(spawnMeta);
			    hubInv.addItem(spawn);
			    
			    for (HubPoint point : hub.getTeleportPoints()) {
				if (eventPlayer.hasPermission(point.getPermNode())) {
				    ItemStack pointItem = new ItemStack(Material.BOAT);
				    ItemMeta pointItemMeta = pointItem.getItemMeta();
				    pointItemMeta.setDisplayName(point.getName());
				    
				    lore.clear();
				    lore.add("Teleport to " + point.getName() + " in " + hub.getName() + "!");
				    pointItemMeta.setLore(lore);
				    pointItem.setItemMeta(pointItemMeta);
				    hubInv.addItem(pointItem);
				}
			    }
			    eventPlayer.closeInventory();
			    eventPlayer.openInventory(hubInv);
			    return;
			}
		    } else if (invName.equals("Available Teleporters!")) {
			String hubName = eventItem.getItemMeta().getLore().get(0).split("in ")[1].replace("!", "");
			
			WorldHub hub = HubManager.getManager().getHub(hubName);
			if (hub != null && hub.isEnabled()) {
			    HubPoint tpPoint = hub.getTeleportPoint(eventItem.getItemMeta().getDisplayName());
			    if (tpPoint != null) {
				IPlayHubMessages.sendMessage(eventPlayer, IPlayHubMessages.teleporting);
				eventPlayer.closeInventory();
				eventPlayer.teleport(tpPoint.getLocation(), TeleportCause.PLUGIN);
				return;
			    } else if (eventItem.getItemMeta().getDisplayName().equals("Spawn") && eventItem.getType() == Material.MINECART) {
				IPlayHubMessages.sendMessage(eventPlayer, IPlayHubMessages.teleporting);
				eventPlayer.closeInventory();
				eventPlayer.teleport(hub.getSpawnPoint().getLocation(), TeleportCause.PLUGIN);
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
