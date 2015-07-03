package me.Aubli.IPlayHub.Hub;

import java.util.List;

import org.bukkit.Location;


public class HubWorld {
    
    private HubConfig config;
    
    private Location hubSpawn;
    private List<Location> teleportLocations;
    
    public HubWorld(HubConfig config, Location spawnLoc) {
	this.config = config;
	this.hubSpawn = spawnLoc.clone();
    }
    
    // TODO setter/getter & save/load
}
