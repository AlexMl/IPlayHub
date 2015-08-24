package me.Aubli.IPlayHub.Hub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import me.Aubli.IPlayHub.IPlayHub;
import me.Aubli.IPlayHub.IPlayHubMessages;
import me.Aubli.IPlayHub.IPlayHubPermissions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


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
	
	this.hubSpawn = new HubPoint(parseLocation(config.getString("location.spawn")), "SPAWN", config.getString("location.spawnPermission"));
	
	ConfigurationSection tpSection = config.getConfigurationSection("location.teleport");
	this.teleportLocations = new ArrayList<HubPoint>();
	
	if (tpSection != null) {
	    for (String sectionKey : tpSection.getValues(false).keySet()) {
		HubPoint point = new HubPoint(parseLocation(tpSection.getString(sectionKey + ".location")), sectionKey, tpSection.getInt(sectionKey + ".delay"), tpSection.getString(sectionKey + ".permission"));
		this.teleportLocations.add(point);
	    }
	}
    }
    
    public void saveConfig() {
	FileConfiguration configConfiguration = YamlConfiguration.loadConfiguration(IPlayHub.getHub().getWorldFile());
	ConfigurationSection configSection = configConfiguration.getConfigurationSection("worlds." + getConfig().getWorld().getName());
	configSection.set("location.spawn", getLocationString(getSpawnPoint()));
	configSection.set("location.spawnPermission", getSpawnPoint().getPermNode());
	
	configSection.set("location.teleport", null);
	
	for (HubPoint tpPoint : getTeleportPoints()) {
	    configSection.set("location.teleport." + tpPoint.getName() + ".location", getLocationString(tpPoint));
	    configSection.set("location.teleport." + tpPoint.getName() + ".permission", tpPoint.getPermNode());
	    configSection.set("location.teleport." + tpPoint.getName() + ".delay", tpPoint.getDelay());
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
    
    public HubPoint[] getApplicableHubPoints(Player player) {
	List<HubPoint> allowedPoints = new ArrayList<HubPoint>();
	
	for (HubPoint point : this.teleportLocations) {
	    if (player.hasPermission(point.getPermNode())) {
		allowedPoints.add(point);
	    }
	}
	
	if (player.hasPermission(getSpawnPoint().getPermNode())) {
	    allowedPoints.add(getSpawnPoint());
	}
	
	HubPoint[] hubPoints = new HubPoint[allowedPoints.size()];
	for (HubPoint point : allowedPoints) {
	    hubPoints[allowedPoints.indexOf(point)] = point;
	}
	
	Arrays.sort(hubPoints);
	return hubPoints;
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
    
    public boolean teleport(final HubPoint point, final Player player) {
	if (player.hasPermission(point.getPermNode())) {
	    if (point != null && isEnabled()) {
		Bukkit.getScheduler().runTaskLater(IPlayHub.getHub(), new Runnable() {
		    
		    @Override
		    public void run() {
			player.teleport(point.getLocation(), TeleportCause.PLUGIN);
		    }
		}, point.getDelay() * 20L);
		return true;
	    } else {
		IPlayHubMessages.sendMessage(player, IPlayHubMessages.teleport_not_available);
	    }
	} else {
	    IPlayHubPermissions.deny(player);
	}
	return false;
    }
    
    @Override
    public String toString() {
	return getClass().getSimpleName() + "[World=" + this.config.getWorld().getName() + "]";
    }
}
