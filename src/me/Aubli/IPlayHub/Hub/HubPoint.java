package me.Aubli.IPlayHub.Hub;

import org.bukkit.Location;


public class HubPoint {
    
    private Location location;
    private String name;
    private String permNode;
    
    public HubPoint(Location location, String name) {
	this(location, name, "");
    }
    
    public HubPoint(Location location, String name, String permissionNode) {
	this.location = location.clone();
	this.name = name;
	this.permNode = permissionNode;
    }
    
    public Location getLocation() {
	return this.location;
    }
    
    public String getName() {
	return this.name;
    }
    
    public String getPermNode() {
	return this.permNode;
    }
    
    @Override
    public String toString() {
	return getClass().getSimpleName() + "[" + getName() + ", " + getLocation().getBlockX() + ":" + getLocation().getBlockY() + ":" + getLocation().getBlockZ() + ", " + getPermNode() + "]";
    }
}
