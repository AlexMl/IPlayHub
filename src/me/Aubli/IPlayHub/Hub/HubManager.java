package me.Aubli.IPlayHub.Hub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import me.Aubli.IPlayHub.IPlayHub;
import me.Aubli.IPlayHub.HubExceptions.WorldAlreadyInitializedException;
import me.Aubli.IPlayHub.HubExceptions.WorldNotLoadedException;

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
		} catch (WorldNotLoadedException e) {
		    // TODO logger
		    e.printStackTrace();
		} catch (Exception e) {
		    // TODO: handle exception
		    e.printStackTrace();
		}
	    }
	    
	}
	
	try {
	    config.save(IPlayHub.getHub().getWorldFile());
	} catch (IOException e) {
	    IPlayHub.getPluginLogger().log(getClass(), Level.WARNING, "Error while saving worldfile!", true, false, e);
	}
	
    }
    
    public WorldHub registerHub(String hubName, World world, Location spawnLocation) throws Exception {
	if (getHub(world) == null && getHub(hubName) == null) {
	    WorldHub hub = new WorldHub(new HubConfig(hubName, world, createSection(world)), spawnLocation.clone());
	    this.hubList.add(hub);
	    return hub;
	} else {
	    throw new WorldAlreadyInitializedException();
	    // TODO logger
	}
	
    }
    
    private ConfigurationSection createSection(World world) throws Exception {
	FileConfiguration config = YamlConfiguration.loadConfiguration(IPlayHub.getHub().getWorldFile());
	
	if (config.get("worlds." + world.getName()) == null) {
	    ConfigurationSection section = config.createSection("worlds." + world.getName());
	    config.save(IPlayHub.getHub().getWorldFile());
	    return section;
	} else {
	    throw new WorldAlreadyInitializedException();
	    // TODO logger
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
    
    public WorldHub getHub(String hubName) {
	for (WorldHub hub : this.hubList) {
	    if (hub.getName().equals(hubName)) {
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
