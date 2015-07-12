package me.Aubli.IPlayHub.Listener;

import me.Aubli.IPlayHub.IPlayHub;
import me.Aubli.IPlayHub.IPlayHubPermissions;
import me.Aubli.IPlayHub.Hub.HubManager;
import me.Aubli.IPlayHub.Hub.WorldHub;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class SignListener implements Listener {
    
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
	Player eventPlayer = event.getPlayer();
	
	if (ChatColor.stripColor(event.getLine(0)).equalsIgnoreCase(ChatColor.stripColor(IPlayHub.getPluginPrefix()))) {
	    if (eventPlayer.hasPermission(IPlayHubPermissions.Admin.getPermissionNode())) {
		if (!event.getLine(1).isEmpty() && !event.getLine(2).isEmpty()) {
		    String worldName = event.getLine(1);
		    String tpName = event.getLine(2);
		    WorldHub hub = HubManager.getManager().getHub(Bukkit.getWorld(worldName));
		    
		    if (hub != null) {
			if (hub.getTeleportPoint(tpName) != null) { // TODO colors
			    event.setLine(0, IPlayHub.getPluginPrefix());
			    event.setLine(1, "Hub Teleport");
			    event.setLine(2, tpName);
			    event.setLine(3, worldName);
			    // Message
			} else {
			    // Message
			}
		    } else {
			// Message
		    }
		} else {
		    // Message
		}
	    } else {
		IPlayHubPermissions.deny(eventPlayer);
		event.setCancelled(true);
		return;
	    }
	}
	
    }
    
    @EventHandler
    public void onSignInteract(PlayerInteractEvent event) {
	
	Player eventPlayer = event.getPlayer();
	
	if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
	    if (event.getClickedBlock().getState() instanceof Sign) {
		Sign eventSign = (Sign) event.getClickedBlock().getState();
		
		if (ChatColor.stripColor(eventSign.getLine(0)).equalsIgnoreCase(ChatColor.stripColor(IPlayHub.getPluginPrefix()))) { // TODO prefix
		    event.setCancelled(true);
		    
		    if (eventPlayer.hasPermission(IPlayHubPermissions.Teleport.getPermissionNode())) {
			String worldName = eventSign.getLine(3);
			String tpName = eventSign.getLine(2);
			WorldHub hub = HubManager.getManager().getHub(Bukkit.getWorld(worldName));
			
			if (hub != null) {
			    if (hub.getTeleportPoint(tpName) != null) {
				if (hub.isEnabled()) {
				    // INFO not checked permissions from hubPoint
				    eventPlayer.teleport(hub.getTeleportPoint(tpName).getLocation(), TeleportCause.PLUGIN);
				    return;
				} else {
				    // Message
				}
			    } else {
				// Message
			    }
			} else {
			    // Message
			}
		    } else {
			IPlayHubPermissions.deny(eventPlayer);
			return;
		    }
		}
	    }
	}
    }
    
    @EventHandler
    public void onSignBreak(BlockBreakEvent event) {
	if (event.getBlock().getState() instanceof Sign) {
	    Sign eventSign = (Sign) event.getBlock().getState();
	    
	    if (eventSign.getLine(0).equalsIgnoreCase("[iph]")) { // TODO prefix
		if (!event.getPlayer().hasPermission(IPlayHubPermissions.Admin.getPermissionNode())) {
		    event.setCancelled(true);
		    IPlayHubPermissions.deny(event.getPlayer());
		    return;
		}
	    }
	}
    }
}
