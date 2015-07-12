package me.Aubli.IPlayHub.Listener;

import java.util.ArrayList;
import java.util.List;

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
	
	if (invName.equals("Teleporters by World!") || invName.equals("Available Teleporters!")) {
	    event.setCancelled(true);
	    
	    if (event.getSlot() == -999) {
		eventPlayer.closeInventory();
		return;
	    }
	    
	    if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
		if (eventPlayer.hasPermission(IPlayHubPermissions.Teleport.getPermissionNode())) {
		    if (invName.equals("Teleporters by World!")) {
			WorldHub hub = HubManager.getManager().getHub(Bukkit.getWorld(event.getCurrentItem().getItemMeta().getDisplayName()));
			
			if (hub != null && hub.isEnabled()) {
			    Inventory hubInv = Bukkit.createInventory(eventPlayer, (int) (Math.ceil(hub.getTeleportPoints().size() / 9.0) * 9), "Available Teleporters!");
			    
			    for (HubPoint point : hub.getTeleportPoints()) {
				if (eventPlayer.hasPermission(point.getPermNode())) {
				    ItemStack pointItem = new ItemStack(Material.BOAT);
				    ItemMeta pointItemMeta = pointItem.getItemMeta();
				    pointItemMeta.setDisplayName(point.getName());
				    
				    List<String> lore = new ArrayList<String>();
				    lore.add("Teleport to " + point.getName() + " in " + hub.getWorld().getName() + "!");
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
			String worldName = event.getCurrentItem().getItemMeta().getLore().get(0).split("in ")[1].replace("!", "");
			
			WorldHub hub = HubManager.getManager().getHub(Bukkit.getWorld(worldName));
			if (hub != null && hub.isEnabled()) {
			    HubPoint tpPoint = hub.getTeleportPoint(event.getCurrentItem().getItemMeta().getDisplayName());
			    if (tpPoint != null) {
				eventPlayer.closeInventory();
				eventPlayer.teleport(tpPoint.getLocation(), TeleportCause.PLUGIN);
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
