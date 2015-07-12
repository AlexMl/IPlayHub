package me.Aubli.IPlayHub.Listener;

import me.Aubli.IPlayHub.IPlayHubPermissions;
import me.Aubli.IPlayHub.Hub.HubManager;
import me.Aubli.IPlayHub.Hub.WorldHub;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class SignListener implements Listener {
    
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
	Player eventPlayer = event.getPlayer();
	
	if (event.getLine(0).equalsIgnoreCase("[iph]")) {
	    if (eventPlayer.hasPermission(IPlayHubPermissions.Admin.getPermissionNode())) {
		if (!event.getLine(1).isEmpty() && !event.getLine(2).isEmpty()) {
		    String worldName = event.getLine(1);
		    String tpName = event.getLine(2);
		    WorldHub hub = HubManager.getManager().getHub(Bukkit.getWorld(worldName));
		    
		    if (hub != null) {
			if (hub.getTeleportPoint(tpName) != null) {
			    event.setLine(0, "[IPH]");
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
		
		if (eventSign.getLine(0).equalsIgnoreCase("[iph]")) { // TODO prefix
		    event.setCancelled(true);
		    
		    if (eventPlayer.hasPermission(IPlayHubPermissions.Teleport.getPermissionNode())) {
			String worldName = eventSign.getLine(3);
			String tpName = eventSign.getLine(2);
			WorldHub hub = HubManager.getManager().getHub(Bukkit.getWorld(worldName));
			
			if (hub != null) {
			    if (hub.getTeleportPoint(tpName) != null) {
				if (hub.isEnabled()) {
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
}
