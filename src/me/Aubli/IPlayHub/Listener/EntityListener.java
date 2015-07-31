package me.Aubli.IPlayHub.Listener;

import me.Aubli.IPlayHub.Hub.HubManager;
import me.Aubli.IPlayHub.Hub.WorldHub;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;


public class EntityListener implements Listener {
    
    private boolean isPvPDamage = false; // INFO: ugly as hell
    
    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(EntityDamageByEntityEvent event) {
	
	if (event.getDamager() instanceof Player) {
	    if (event.getEntity() instanceof Player) {
		
		WorldHub hub = HubManager.getManager().getHub(event.getEntity().getWorld());
		
		this.isPvPDamage = true;
		
		if (hub != null) {
		    if (hub.isEnabled()) {
			if (!hub.getConfig().isPlayerVsPlayer()) {
			    // TODO maybe log or message
			    System.out.println("denie pvp");
			    event.setCancelled(true);
			    return;
			}
		    }
		}
		
	    }
	}
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageEvent event) {
	
	if (event.getEntity() instanceof Player) {
	    
	    if (!this.isPvPDamage) {
		WorldHub hub = HubManager.getManager().getHub(event.getEntity().getWorld());
		
		if (hub != null) {
		    if (hub.isEnabled()) {
			if (!hub.getConfig().isAllowDamage()) {
			    // TODO maybe log or message
			    System.out.println("denie damage");
			    event.setCancelled(true);
			    return;
			}
		    }
		}
	    } else {
		this.isPvPDamage = false;
	    }
	}
    }
    
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
	
	WorldHub hub = HubManager.getManager().getHub(event.getEntity().getWorld());
	
	if (hub != null) {
	    if (hub.isEnabled()) {
		if (!hub.getConfig().isStarving()) {
		    System.out.println("denie starving");
		    event.setCancelled(true);
		    return;
		}
	    }
	}
	
    }
    
}
