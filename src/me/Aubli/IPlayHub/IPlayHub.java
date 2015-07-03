package me.Aubli.IPlayHub;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;
import org.util.Logger.PluginOutput;


public class IPlayHub extends JavaPlugin {
    
    private static IPlayHub instance;
    
    private static PluginOutput logger;
    
    private boolean debugMode;
    private int logLevel;
    
    @Override
    public void onDisable() {
	logger.log(getClass(), "Plugin disabled!", false);
    }
    
    @Override
    public void onEnable() {
	instance = this;
	loadConfig();
	
	logger = new PluginOutput(getHub(), this.debugMode, this.logLevel);
	
	logger.log(getClass(), "Plugin enabled!", false);
    }
    
    public static IPlayHub getHub() {
	return instance;
    }
    
    public static PluginOutput getPluginLogger() {
	return logger;
    }
    
    private void loadConfig() {
	getConfig().addDefault("config.debugMode", false);
	getConfig().addDefault("config.logLevel", Level.INFO.intValue());
	
	this.debugMode = getConfig().getBoolean("config.debugMode");
	this.logLevel = getConfig().getInt("config.logLevel");
	
	getConfig().options().copyDefaults(true);
	saveConfig();
    }
}
