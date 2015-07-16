package me.Aubli.IPlayHub.Hub;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

import me.Aubli.IPlayHub.IPlayHub;
import me.Aubli.IPlayHub.HubExceptions.WorldNotLoadedException;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class HubConfig {
    
    private ConfigurationSection configSection;
    
    private boolean enabled;			// Enable hub in world
    private World world;			// World, settings apply to
    private String hubName;			// name of hub
    
    private boolean playerVsPlayer;		// Enable pvp in world
    private boolean starving;			// Enable hunger in world
    private boolean weatherChanges;		// Enable weather change in world
    
    public HubConfig(String hubName, World world, ConfigurationSection configSection) {
	
	this.configSection = configSection;
	this.world = world;
	
	if (configSection.get("enabled") != null) {
	    loadSettings(configSection);
	} else {
	    setDefaultSettings();
	    saveSettings();
	}
    }
    
    public HubConfig(ConfigurationSection configSection) throws Exception {
	if (configSection.get("enabled") != null) {
	    this.configSection = configSection;
	    loadSettings(configSection);
	} else {
	    throw new Exception("Config section not initialized!");
	}
	
	if (getWorld() == null) {
	    throw new WorldNotLoadedException();
	}
    }
    
    private void setDefaultSettings() {
	this.enabled = true;
	this.playerVsPlayer = false;
	this.starving = false;
	this.weatherChanges = true;
    }
    
    private void loadSettings(ConfigurationSection config) {
	this.enabled = config.getBoolean("enabled");
	this.hubName = config.getString("name");
	this.world = Bukkit.getWorld(UUID.fromString(config.getString("world")));
	
	this.playerVsPlayer = config.getBoolean("settings.pvp");
	this.starving = config.getBoolean("settings.hunger");
	this.weatherChanges = config.getBoolean("settings.weatherActivity");
    }
    
    private void saveSettings() {
	FileConfiguration configConfiguration = YamlConfiguration.loadConfiguration(IPlayHub.getHub().getWorldFile());
	ConfigurationSection configSection = configConfiguration.getConfigurationSection("worlds." + getWorld().getName());
	configSection.set("enabled", isEnabled());
	configSection.set("name", getHubName());
	configSection.set("world", getWorld().getUID().toString());
	
	configSection.set("settings.pvp", isPlayerVsPlayer());
	configSection.set("settings.hunger", isStarving());
	configSection.set("settings.weatherActivity", isWeatherChanges());
	
	try {
	    configConfiguration.save(IPlayHub.getHub().getWorldFile());
	} catch (IOException e) {
	    IPlayHub.getPluginLogger().log(getClass(), Level.WARNING, "Error while saving hubsettings for hub " + getHubName() + " in world " + getWorld().getName() + "!", true, false, e);
	}
    }
    
    public World getWorld() {
	return this.world;
    }
    
    public String getHubName() {
	return this.hubName;
    }
    
    public ConfigurationSection getConfigSection() {
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
}
