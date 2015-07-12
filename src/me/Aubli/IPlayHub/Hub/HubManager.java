package me.Aubli.IPlayHub.Hub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.Aubli.IPlayHub.IPlayHub;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class HubManager {
    
    private static HubManager instance;
    
    private List<WorldHub> hubList;
    
    public HubManager() {
	instance = this;
	initialize();
    }
    
    private void initialize() {
	FileConfiguration config = YamlConfiguration.loadConfiguration(IPlayHub.getHub().getWorldFile());
	
	this.hubList = new ArrayList<WorldHub>();
	
	if (config.get("worlds") != null) {
	    
	    for (String worlds : config.getConfigurationSection("worlds").getKeys(false)) {
		try {
		    WorldHub worldHub = new WorldHub(config.getConfigurationSection("worlds." + worlds));
		    this.hubList.add(worldHub);
		} catch (Exception e) {
		    // TODO logger
		    e.printStackTrace();
		}
	    }
	    
	}
	
	try {
	    config.save(IPlayHub.getHub().getWorldFile());
	} catch (IOException e) {
	    e.printStackTrace();
	    // TODO logger
	}
	
    }
    
    public WorldHub registerHub(World world, Location spawnLocation) throws Exception {
	WorldHub hub = new WorldHub(new HubConfig(world, createSection(world)), spawnLocation.clone());
	this.hubList.add(hub);
	return hub;
    }
    
    private ConfigurationSection createSection(World world) throws Exception {
	FileConfiguration config = YamlConfiguration.loadConfiguration(IPlayHub.getHub().getWorldFile());
	
	if (config.get("worlds." + world.getName()) == null) {
	    ConfigurationSection section = config.createSection("worlds." + world.getName());
	    config.save(IPlayHub.getHub().getWorldFile());
	    return section;
	} else {
	    // TODO logger
	    throw new Exception("Only one hub per World");
	}
    }
    
    public static HubManager getManager() {
	return instance;
    }
    
    public WorldHub getHub(World world) {
	for (WorldHub hub : this.hubList) {
	    if (hub.getWorld().equals(world)) {
		return hub;
	    }
	}
	return null;
    }
    
    public WorldHub[] getWorldHubs() {
	WorldHub[] hubs = new WorldHub[this.hubList.size()];
	
	for (WorldHub hub : this.hubList) {
	    hubs[this.hubList.indexOf(hub)] = hub;
	}
	return hubs;
    }
}
