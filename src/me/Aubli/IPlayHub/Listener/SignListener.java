package me.Aubli.IPlayHub.Listener;

import me.Aubli.IPlayHub.IPlayHubPermissions;
import me.Aubli.IPlayHub.Hub.HubManager;
import me.Aubli.IPlayHub.Hub.WorldHub;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;


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
			    // TODO format sign
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
		// Message
		event.setCancelled(true);
		return;
	    }
	}
	
    }
}
