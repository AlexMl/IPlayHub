package me.Aubli.IPlayHub.Hub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import me.Aubli.IPlayHub.IPlayHub;
import me.Aubli.IPlayHub.HubExceptions.HubAlreadyExistsException;
import me.Aubli.IPlayHub.HubExceptions.WorldAlreadyInitializedException;
import me.Aubli.IPlayHub.HubExceptions.WorldNotLoadedException;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.util.FileConverter.Converter;
import org.util.FileConverter.Converter.FileType;


public class HubManager {
    
    private static HubManager instance;
    
    private List<WorldHub> hubList;
    
    private HubManager() {
	instance = this;
	initialize();
    }
    
    public static HubManager getManager() {
	if (!isInitialized()) {
	    new HubManager();
	}
	return instance;
    }
    
    public static boolean isInitialized() {
	return instance != null;
    }
    
    private void initialize() {
	// Run the file converter
	Converter hubConverter = new Converter();
	String newVersion = "0.0.30";
	
	if (hubConverter.needsConvertion(FileType.world_config, newVersion)) {
	    // Move spawn into own subconfiguration
	    Map<String, String> changeMap = new HashMap<String, String>();
	    changeMap.put("location.spawn", "location.spawn.location");
	    changeMap.put("location.spawnPermission", "location.spawn.permission");
	    
	    Map<String, Object> valueMap = new HashMap<String, Object>();
	    valueMap.put("location.spawn.delay", 0);
	    
	    hubConverter.convertPaths(FileType.world_config, newVersion, false, changeMap);
	    hubConverter.addValues(FileType.world_config, newVersion, true, valueMap);
	}
	
	// Load the hubs from config
	FileConfiguration config = YamlConfiguration.loadConfiguration(IPlayHub.getHub().getWorldFile());
	
	this.hubList = new ArrayList<WorldHub>();
	
	if (config.get("worlds") != null) {
	    
	    for (String world : config.getConfigurationSection("worlds").getKeys(false)) {
		try {
		    WorldHub worldHub = new WorldHub(config.getConfigurationSection("worlds." + world));
		    this.hubList.add(worldHub);
		    IPlayHub.getPluginLogger().log(getClass(), Level.INFO, "Loaded " + worldHub.getName() + " in World " + worldHub.getWorld().getName(), true, false);
		} catch (WorldNotLoadedException e) {
		    IPlayHub.getPluginLogger().log(getClass(), Level.WARNING, "Worldhub in world " + world + " can not be loaded! " + e.getMessage(), true, false, e);
		} catch (Exception e) {
		    IPlayHub.getPluginLogger().log(getClass(), Level.SEVERE, "Worldhub in world " + world + " can not be loaded! " + e.getMessage(), true, false, e);
		}
	    }
	    
	}
	
	try {
	    config.save(IPlayHub.getHub().getWorldFile());
	} catch (IOException e) {
	    IPlayHub.getPluginLogger().log(getClass(), Level.WARNING, "Error while saving worldfile!", true, false, e);
	}
	IPlayHub.getPluginLogger().log(getClass(), Level.FINER, "HubManager finished loading " + this.hubList.size() + " hubs out of " + config.getConfigurationSection("worlds").getKeys(false).size() + " possible", true, true);
    }
    
    public void reloadHubs() {
	initialize();
    }
    
    public WorldHub registerHub(String hubName, World world, Location spawnLocation) throws Exception {
	if (getHub(world) == null) {
	    if (getHub(hubName) == null) {
		WorldHub hub = new WorldHub(new HubConfig(hubName, world, createSection(world)), spawnLocation.clone());
		this.hubList.add(hub);
		IPlayHub.getPluginLogger().log(getClass(), Level.INFO, hubName + " registered in " + world.getName() + "(" + world.getUID().toString() + "). Spawn is at " + spawnLocation.getX() + ":" + spawnLocation.getY() + ":" + spawnLocation.getZ(), true, true);
		return hub;
	    } else {
		Exception e = new HubAlreadyExistsException();
		IPlayHub.getPluginLogger().log(getClass(), Level.WARNING, "Can not register new Hub with name " + hubName + " in world " + world.getName() + "! " + e.getMessage(), true, false, e);
		throw e;
	    }
	} else {
	    Exception e = new WorldAlreadyInitializedException();
	    IPlayHub.getPluginLogger().log(getClass(), Level.WARNING, "Can not register new Hub with name " + hubName + " in world " + world.getName() + "! " + e.getMessage(), true, false, e);
	    throw e;
	}
    }
    
    private ConfigurationSection createSection(World world) throws Exception {
	FileConfiguration config = YamlConfiguration.loadConfiguration(IPlayHub.getHub().getWorldFile());
	
	if (config.get("worlds." + world.getName()) == null) {
	    ConfigurationSection section = config.createSection("worlds." + world.getName());
	    config.save(IPlayHub.getHub().getWorldFile());
	    IPlayHub.getPluginLogger().log(getClass(), Level.FINER, "Created ConfigurationSection 'worlds." + world.getName() + "'", true, true);
	    return section;
	} else {
	    Exception e = new WorldAlreadyInitializedException();
	    IPlayHub.getPluginLogger().log(getClass(), Level.WARNING, "Can not create config section. " + world.getName() + " already exists!", true, false, e);
	    throw e;
	}
    }
    
    public WorldHub getHub(World world) {
	for (WorldHub hub : getWorldHubs()) {
	    if (hub.getWorld().equals(world)) {
		return hub;
	    }
	}
	return null;
    }
    
    public WorldHub getHub(String hubName) {
	for (WorldHub hub : getWorldHubs()) {
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
