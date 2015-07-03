package me.Aubli.IPlayHub;

import org.bukkit.plugin.java.JavaPlugin;
import org.util.Logger.PluginOutput;


public class IPlayHub extends JavaPlugin {
    
    private static IPlayHub instance;
    
    private static PluginOutput logger;
    
    @Override
    public void onDisable() {
	logger.log(getClass(), "Plugin disabled!", false);
    }
    
    @Override
    public void onEnable() {
	instance = this;
	
	logger = new PluginOutput(getHub(), true, 100); // Missing config
	
	logger.log(getClass(), "Plugin enabled!", false);
    }
    
    public static IPlayHub getHub() {
	return instance;
    }
    
    public static PluginOutput getPluginLogger() {
	return logger;
    }
}
