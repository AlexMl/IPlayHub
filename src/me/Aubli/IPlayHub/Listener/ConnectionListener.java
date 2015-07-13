package me.Aubli.IPlayHub.Listener;

import me.Aubli.IPlayHub.IPlayHub;
import me.Aubli.IPlayHub.Hub.HubManager;
import me.Aubli.IPlayHub.Hub.WorldHub;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class ConnectionListener implements Listener {
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
	Player eventPlayer = event.getPlayer();
	
	if (IPlayHub.isJoinAtHub()) {
	    WorldHub hub = HubManager.getManager().getHub(IPlayHub.getMainWorld());
	    
	    if (hub != null) {
		eventPlayer.teleport(hub.getSpawnPoint().getLocation(), TeleportCause.PLUGIN);
	    } else {
		// TODO log
	    }
	}
	eventPlayer.sendMessage(IPlayHub.getPluginPrefix() + " " + IPlayHub.getWelcomeMessage());
    }
}
