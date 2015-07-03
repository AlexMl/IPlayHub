package me.Aubli.IPlayHub.Hub;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.MemorySection;


public class HubConfig {
    
    private MemorySection configSection;
    
    private boolean enabled;			// Enable hub in world
    private World world;			// World settings apply to
    
    private boolean playerVsPlayer;		// Enable pvp in world
    private boolean starving;			// Enable hunger in world
    private boolean weatherChanges;		// Enable weather change in world
    private boolean shootFireworks;		// shoot fireworks on join
    
    public HubConfig(World world, MemorySection configSection) {
	
	this.configSection = configSection;
	this.world = world;
	
	if (configSection.get("enabled") != null) {
	    loadSettings(configSection);
	} else {
	    setDefaultSettings();
	    saveSettings(configSection);
	}
    }
    
    public HubConfig(MemorySection configSection) throws Exception {
	if (configSection.get("enabled") != null) {
	    this.configSection = configSection;
	    loadSettings(configSection);
	} else {
	    throw new Exception();
	}
    }
    
    private void setDefaultSettings() {
	this.enabled = true;
	this.playerVsPlayer = false;
	this.starving = false;
	this.weatherChanges = true;
	this.shootFireworks = true;
    }
    
    private void loadSettings(MemorySection config) {
	this.enabled = config.getBoolean("enabled");
	this.world = Bukkit.getWorld(UUID.fromString(config.getString("world")));
	
	this.playerVsPlayer = config.getBoolean("settings.pvp");
	this.starving = config.getBoolean("settings.hunger");
	this.weatherChanges = config.getBoolean("settings.weatherActivity");
	// TODO fireworks
    }
    
    private void saveSettings(MemorySection config) {
	config.set("enabled", isEnabled());
	config.set("world", getWorld().getUID().toString());
	
	config.set("settings.pvp", isPlayerVsPlayer());
	config.set("settings.hunger", isStarving());
	config.set("settings.weatherActivity", isWeatherChanges());
	// TODO fireworks
    }
    
    public World getWorld() {
	return this.world;
    }
    
    public MemorySection getConfigSection() {
	return this.configSection;
    }
    
    public boolean isEnabled() {
	return this.enabled;
    }
    
    public boolean isPlayerVsPlayer() {
	return this.playerVsPlayer;
    }
    
    public boolean isStarving() {
	return this.starving;
    }
    
    public boolean isWeatherChanges() {
	return this.weatherChanges;
    }
    
    public boolean isShootFireworks() {
	return this.shootFireworks;
    }
    
}
