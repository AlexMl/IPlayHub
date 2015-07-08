package me.Aubli.IPlayHub.Listener;

import me.Aubli.IPlayHub.Hub.HubManager;
import me.Aubli.IPlayHub.Hub.WorldHub;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;


public class WeatherListener implements Listener {
    
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
	
	WorldHub hub = HubManager.getManager().getHub(event.getWorld());
	
	if (hub != null) {
	    if (hub.isEnabled()) {
		if (!hub.getConfig().isWeatherChanges()) {
		    System.out.println("denie Weather change");
		    event.setCancelled(true);
		    return;
		}
	    }
	}
	
    }
}
