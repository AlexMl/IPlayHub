package me.Aubli.IPlayHub.Hub;

import me.Aubli.IPlayHub.IPlayHubPermissions;

import org.bukkit.Location;


public class HubPoint implements Comparable<HubPoint> {
    
    private Location location;
    private String name;
    private String permNode;
    
    public HubPoint(Location location, String name) {
	this(location, name, IPlayHubPermissions.Teleport.getPermissionNode());
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
    public int compareTo(HubPoint o) {
	return getName().compareToIgnoreCase(o.getName());
    }
    
    @Override
    public boolean equals(Object obj) {
	if (obj instanceof HubPoint) {
	    HubPoint other = (HubPoint) obj;
	    return (other.getName().equals(getName())) && (other.getLocation().equals(getLocation()));
	}
	return false;
    }
    
    @Override
    public String toString() {
	return getClass().getSimpleName() + "[" + getName() + ", " + getLocation().getBlockX() + ":" + getLocation().getBlockY() + ":" + getLocation().getBlockZ() + ", " + getPermNode() + "]";
    }
}
