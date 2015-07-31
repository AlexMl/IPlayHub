package me.Aubli.IPlayHub.Hub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import me.Aubli.IPlayHub.IPlayHub;
import me.Aubli.IPlayHub.IPlayHubPermissions;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class WorldHub {
    
    private HubConfig config;
    
    private HubPoint hubSpawn;
    private List<HubPoint> teleportLocations;
    
    public WorldHub(HubConfig config, Location spawnLoc) {
	this.config = config;
	this.hubSpawn = new HubPoint(spawnLoc.clone(), "SPAWN");
	this.teleportLocations = new ArrayList<HubPoint>();
	saveConfig();
    }
    
    public WorldHub(ConfigurationSection section) throws Exception {
	this.config = new HubConfig(section);
	loadConfig();
	System.out.println(getName() + " is loaded!");
    }
    
    private void loadConfig() {
	ConfigurationSection config = getConfig().getConfigSection();
	
	this.hubSpawn = new HubPoint(parseLocation(config.getString("location.spawn")), "SPAWN");
	
	ConfigurationSection tpSection = config.getConfigurationSection("location.teleport");
	this.teleportLocations = new ArrayList<HubPoint>();
	
	if (tpSection != null) {
	    for (String sectionKey : tpSection.getValues(false).keySet()) {
		HubPoint point = new HubPoint(parseLocation(tpSection.getString(sectionKey + ".location")), sectionKey, tpSection.getString(sectionKey + ".permission"));
		this.teleportLocations.add(point);
	    }
	}
    }
    
    public void saveConfig() {
	FileConfiguration configConfiguration = YamlConfiguration.loadConfiguration(IPlayHub.getHub().getWorldFile());
	ConfigurationSection configSection = configConfiguration.getConfigurationSection("worlds." + getConfig().getWorld().getName());
	configSection.set("location.spawn", getLocationString(getSpawnPoint()));
	
	for (HubPoint tpPoint : getTeleportPoints()) {
	    configSection.set("location.teleport." + tpPoint.getName() + ".location", getLocationString(tpPoint));
	    configSection.set("location.teleport." + tpPoint.getName() + ".permission", tpPoint.getPermNode());
	}
	
	try {
	    configConfiguration.save(IPlayHub.getHub().getWorldFile());
	} catch (IOException e) {
	    IPlayHub.getPluginLogger().log(getClass(), Level.WARNING, "Error while saving worldconfiguration for hub " + getName() + " in world " + getWorld().getName() + "!", true, false, e);
	}
    }
    
    private String getLocationString(HubPoint loc) {
	return getLocationString(loc.getLocation());
    }
    
    private String getLocationString(Location loc) {
	return loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
    }
    
    private Location parseLocation(String locationString) {
	String[] locationArray = locationString.split(",");
	return new Location(getConfig().getWorld(), Double.parseDouble(locationArray[0]), Double.parseDouble(locationArray[1]), Double.parseDouble(locationArray[2]), Float.parseFloat(locationArray[3]), Float.parseFloat(locationArray[4]));
    }
    
    public World getWorld() {
	return getConfig().getWorld();
    }
    
    public String getName() {
	return getConfig().getHubName();
    }
    
    public HubPoint getSpawnPoint() {
	return this.hubSpawn;
    }
    
    public HubPoint[] getTeleportPoints() {
	HubPoint[] points = new HubPoint[this.teleportLocations.size()];
	
	for (HubPoint point : this.teleportLocations) {
	    points[this.teleportLocations.indexOf(point)] = point;
	}
	
	Arrays.sort(points);
	return points;
    }
    
    public HubPoint getTeleportPoint(String name) {
	for (HubPoint hp : getTeleportPoints()) {
	    if (hp.getName().equals(name)) {
		return hp;
	    }
	}
	return null;
    }
    
    public HubConfig getConfig() {
	return this.config;
    }
    
    public boolean isEnabled() {
	return getConfig().isEnabled();
    }
    
    public HubPoint addTeleportPoint(Location location, String name) {
	return addTeleportPoint(location, name, IPlayHubPermissions.Teleport.getPermissionNode());
    }
    
    public HubPoint addTeleportPoint(Location location, String name, String permissionNode) {
	if (getTeleportPoint(name) == null) {
	    HubPoint point = new HubPoint(location, name, permissionNode);
	    this.teleportLocations.add(point);
	    return point;
	} else {
	    return null;
	}
    }
    
    @Override
    public String toString() {
	return getClass().getSimpleName() + "[World=" + this.config.getWorld().getName() + "]";
    }
}
