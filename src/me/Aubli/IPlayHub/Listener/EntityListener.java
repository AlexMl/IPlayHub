package me.Aubli.IPlayHub.Listener;

import me.Aubli.IPlayHub.Hub.HubManager;
import me.Aubli.IPlayHub.Hub.HubWorld;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;


public class EntityListener implements Listener {
    
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
	
	if (event.getDamager() instanceof Player) {
	    if (event.getEntity() instanceof Player) {
		
		HubWorld hub = HubManager.getManager().getHub(event.getEntity().getWorld());
		
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
    
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
	
	HubWorld hub = HubManager.getManager().getHub(event.getEntity().getWorld());
	
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
