package me.Aubli.IPlayHub.Hub;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;


public class HubWorld {
    
    private HubConfig config;
    
    private Location hubSpawn;
    private List<Location> teleportLocations;
    
    public HubWorld(HubConfig config, Location spawnLoc) {
	this.config = config;
	this.hubSpawn = spawnLoc.clone();
	// save
    }
    
    public HubWorld(ConfigurationSection section) throws Exception {
	this.config = new HubConfig(section);
	// load
    }
    
    // TODO setter/getter & save/load
    
    @Override
    public String toString() {
	return getClass().getSimpleName() + "[World=" + this.config.getWorld().getName() + "]";
    }
}
