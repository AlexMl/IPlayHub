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


public class HubWorld {
    
    private HubConfig config;
    
    private Location hubSpawn;
    private List<Location> teleportLocations;
    
    public HubWorld(HubConfig config, Location spawnLoc) {
	this.config = config;
	this.hubSpawn = spawnLoc.clone();
	this.teleportLocations = new ArrayList<Location>();
	saveConfig();
    }
    
    public HubWorld(ConfigurationSection section) throws Exception {
	this.config = new HubConfig(section);
	loadConfig();
    }
    
    private void loadConfig() {
	ConfigurationSection config = getConfig().getConfigSection();
	
	String[] locationString = config.getString("location.spawn").split(",");
	this.hubSpawn = new Location(getConfig().getWorld(), Integer.parseInt(locationString[0]), Integer.parseInt(locationString[1]), Integer.parseInt(locationString[2]));
	
	this.teleportLocations = new ArrayList<Location>();
	
	for (String listEntry : config.getStringList("location.teleport")) {
	    String[] locationListString = listEntry.split(",");
	    this.teleportLocations.add(new Location(getConfig().getWorld(), Integer.parseInt(locationListString[0]), Integer.parseInt(locationListString[1]), Integer.parseInt(locationListString[2])));
	}
    }
    
    public void saveConfig() {
	FileConfiguration configConfiguration = YamlConfiguration.loadConfiguration(IPlayHub.getHub().getWorldFile());
	ConfigurationSection configSection = configConfiguration.getConfigurationSection("worlds." + getConfig().getWorld().getName());
	configSection.set("location.spawn", getLocationString(getSpawnLocation()));
	
	List<String> locationStringList = new ArrayList<String>();
	
	for (Location loc : getTeleportPoints()) {
	    locationStringList.add(getLocationString(loc));
	}
	configSection.set("location.teleport", locationStringList);
	
	try {
	    configConfiguration.save(IPlayHub.getHub().getWorldFile());
	} catch (IOException e) {
	    e.printStackTrace();
	    // TODO log
	}
    }
    
    private String getLocationString(Location loc) {
	return loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }
    
    public World getWorld() {
	return getConfig().getWorld();
    }
    
    public Location getSpawnLocation() {
	return this.hubSpawn.clone();
    }
    
    public List<Location> getTeleportPoints() {
	return this.teleportLocations;
    }
    
    public HubConfig getConfig() {
	return this.config;
    }
    
    public boolean isEnabled() {
	return getConfig().isEnabled();
    }
    
    public boolean addTeleportPoint(Location location) {
	return this.teleportLocations.add(location);
    }
    
    @Override
    public String toString() {
	return getClass().getSimpleName() + "[World=" + this.config.getWorld().getName() + "]";
    }
}
