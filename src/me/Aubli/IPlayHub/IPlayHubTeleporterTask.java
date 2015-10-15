package me.Aubli.IPlayHub;

import me.Aubli.IPlayHub.Hub.HubPoint;
import me.Aubli.IPlayHub.Hub.WorldHub;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;


public class IPlayHubTeleporterTask extends BukkitRunnable {
    
    private Player player;
    private WorldHub hub;
    private HubPoint teleportPoint;
    private TeleportPlayerListener playerListener;
    
    public IPlayHubTeleporterTask(Player player, WorldHub hub, HubPoint hubPoint) {
	this.player = player;
	this.hub = hub;
	this.teleportPoint = hubPoint;
	this.playerListener = new TeleportPlayerListener();
	
	if (getTeleportPoint().getDelay() > 0) {
	    IPlayHubMessages.sendMessage(player, IPlayHubMessages.teleporting_in, getTeleportPoint().getName(), getTeleportPoint().getDelay());
	} else {
	    IPlayHubMessages.sendMessage(player, IPlayHubMessages.teleporting, getTeleportPoint().getName());
	}
	
	IPlayHub.getHub().getServer().getPluginManager().registerEvents(this.playerListener, IPlayHub.getHub());
	this.runTaskLater(IPlayHub.getHub(), getTeleportPoint().getDelay() * 20L);
    }
    
    @Override
    public void run() {
	IPlayHubTeleporterTask.this.player.teleport(getTeleportPoint().getLocation(), TeleportCause.PLUGIN);
	HandlerList.unregisterAll(this.playerListener);
    }
    
    public Player getPlayer() {
	return this.player;
    }
    
    public WorldHub getHub() {
	return this.hub;
    }
    
    public HubPoint getTeleportPoint() {
	return this.teleportPoint;
    }
    
    private class TeleportPlayerListener implements Listener {
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
	    System.out.println(event.getTo().toString());
	    if (event.getPlayer().equals(getPlayer())) {
		
	    }
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
	    System.out.println(event.getClickedBlock().toString());
	    if (event.getPlayer().equals(getPlayer())) {
		
	    }
	}
	
    }
    
}
