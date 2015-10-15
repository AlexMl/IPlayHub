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
import org.bukkit.event.player.PlayerEditBookEvent;
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
		
		if (invName.equals("Hub Points!")) {
		    eventPlayer.openInventory(getHubSwitchGUI(eventPlayer));
		}
		return;
	    }
	    
	    if (eventItem != null && eventItem.getType() != Material.AIR) {
		if (IPlayHubPermissions.hasPermission(eventPlayer, IPlayHubPermissions.Teleport)) {
		    if (invName.equals("Hubs")) {
			WorldHub hub = HubManager.getManager().getHub(eventItem.getItemMeta().getDisplayName());
			
			if (hub != null && hub.isEnabled()) {
			    Inventory hubInv = Bukkit.createInventory(eventPlayer, (int) (Math.ceil((hub.getTeleportPoints().length + (eventPlayer.hasPermission(hub.getSpawnPoint().getPermNode()) ? 1 : 0) + 1) / 9.0) * 9), IPlayHub.getPluginPrefix() + " Hub Points!");
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
			    
			    ItemStack backSign = new ItemStack(Material.SIGN);
			    ItemMeta backSignMeta = backSign.getItemMeta();
			    backSignMeta.setDisplayName(ChatColor.GREEN + "Back");
			    
			    lore.clear();
			    lore.add(ChatColor.LIGHT_PURPLE + "Click to go one inventory up!");
			    backSignMeta.setLore(lore);
			    backSign.setItemMeta(backSignMeta);
			    hubInv.addItem(backSign);
			    
			    eventPlayer.closeInventory();
			    eventPlayer.openInventory(hubInv);
			    return;
			}
		    } else if (invName.equals("Hub Points!")) {
			
			eventPlayer.closeInventory();
			
			if (eventItem.getType() == Material.SIGN) {
			    eventPlayer.openInventory(getHubSwitchGUI(eventPlayer));
			    return;
			}
			
			String hubName = ChatColor.stripColor(eventItem.getItemMeta().getLore().get(0).split("in ")[1].replace("!", ""));
			
			WorldHub hub = HubManager.getManager().getHub(hubName);
			if (hub != null && hub.isEnabled()) {
			    HubPoint tpPoint = hub.getTeleportPoint(eventItem.getItemMeta().getDisplayName());
			    if (tpPoint != null) {
				hub.teleport(tpPoint, eventPlayer);
				return;
			    } else if (eventItem.getItemMeta().getDisplayName().equals("Spawn") && eventItem.getType() == Material.MINECART) {
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
    
    public static Inventory getHubSwitchGUI(Player holder) {
	Inventory inv = Bukkit.createInventory(holder, (int) (Math.ceil(HubManager.getManager().getWorldHubs().length / 9.0) * 9), IPlayHub.getPluginPrefix() + " Hubs");
	
	for (WorldHub hub : HubManager.getManager().getWorldHubs()) {
	    if (hub.getApplicableHubPoints(holder).length > 0) {
		ItemStack hubItem = new ItemStack(Material.CHEST);
		ItemMeta hubItemMeta = hubItem.getItemMeta();
		hubItemMeta.setDisplayName(hub.getName());
		
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.LIGHT_PURPLE + "Teleport Points of hub " + ChatColor.GOLD + hub.getName() + ChatColor.LIGHT_PURPLE + "!");
		hubItemMeta.setLore(lore);
		hubItem.setItemMeta(hubItemMeta);
		inv.addItem(hubItem);
	    }
	}
	return inv;
    }
    
    @EventHandler
    public void onBookSign(PlayerEditBookEvent event) {
	if (event.isSigning()) {
	    Player eventPlayer = event.getPlayer();
	    
	    if (IPlayHubPermissions.hasPermission(eventPlayer, IPlayHubPermissions.Admin)) {
		String tpName = event.getPreviousBookMeta().getTitle().split(":")[1];
		String hubName = event.getPreviousBookMeta().getTitle().split(":")[0];
		
		WorldHub hub = HubManager.getManager().getHub(hubName);
		
		if (event.getPreviousBookMeta().getAuthor().equals(IPlayHub.getPluginPrefix()) && hub != null) {
		    HubPoint tpPoint = hub.getTeleportPoint(tpName);
		    
		    if (tpPoint != null) {
			List<String> pages = event.getNewBookMeta().getPages();
			
			try {
			    String nameString = ChatColor.stripColor(pages.get(1)).replace("\n", "").replace("\r", "");
			    String permissionString = ChatColor.stripColor(pages.get(2)).replace("\n", "").replace("\r", "");
			    String delayString = ChatColor.stripColor(pages.get(3)).replace("\n", "").replace("\r", "");
			    
			    String newTPName = nameString.split(":")[1];
			    String newTPPermission = permissionString.split(":")[1];
			    int newTPDelay = Integer.parseInt(delayString.split(":")[1]);
			    
			    // INFO signs will not change
			    tpPoint.setName(newTPName);
			    tpPoint.setPermNode(newTPPermission);
			    tpPoint.setDelay(newTPDelay);
			    hub.saveConfig();
			    
			    eventPlayer.getInventory().clear(event.getSlot());
			    IPlayHubMessages.sendMessage(eventPlayer, IPlayHubMessages.config_saved);
			    return;
			} catch (Exception e) {
			    e.printStackTrace();
			    event.setCancelled(true);
			}
		    }
		}
		
	    } else {
		IPlayHubPermissions.deny(eventPlayer);
		event.setCancelled(true);
		eventPlayer.getInventory().clear(event.getSlot());
		return;
	    }
	}
    }
    
}
