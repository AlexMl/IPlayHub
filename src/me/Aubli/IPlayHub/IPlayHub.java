package me.Aubli.IPlayHub;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import me.Aubli.IPlayHub.Hub.HubManager;
import me.Aubli.IPlayHub.Listener.ConnectionListener;
import me.Aubli.IPlayHub.Listener.EntityListener;
import me.Aubli.IPlayHub.Listener.GUIListener;
import me.Aubli.IPlayHub.Listener.SignListener;
import me.Aubli.IPlayHub.Listener.WeatherListener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.util.Logger.PluginOutput;


public class IPlayHub extends JavaPlugin {
    
    /*TODO:
     * -messages
     * -tps permissions check
     * -add spawn to tp list
     * -donater things
     * -welcome message
     */
    
    private static IPlayHub instance;
    
    private static PluginOutput logger;
    
    private static final String prefix = ChatColor.YELLOW + "[" + ChatColor.AQUA + "IPH" + ChatColor.YELLOW + "]" + ChatColor.RESET;
    
    private boolean debugMode;
    private int logLevel;
    
    // Config values
    private static boolean joinAtHub;
    private static World mainWorld;
    private static String welcomeMessage;
    
    @Override
    public void onDisable() {
	logger.log(getClass(), "Plugin disabled!", false);
    }
    
    @Override
    public void onEnable() {
	instance = this;
	loadConfig();
	
	logger = new PluginOutput(getHub(), this.debugMode, this.logLevel);
	
	new HubManager();
	
	getCommand("iplayhub").setExecutor(new IPlayHubCommands());
	
	registerListeners();
	logger.log(getClass(), "Plugin enabled!", false);
    }
    
    public static IPlayHub getHub() {
	return instance;
    }
    
    public static PluginOutput getPluginLogger() {
	return logger;
    }
    
    public static String getPluginPrefix() {
	return prefix;
    }
    
    public File getWorldFile() {
	return new File(getDataFolder(), "world-config.yml");
    }
    
    public static boolean isJoinAtHub() {
	return joinAtHub;
    }
    
    public static World getMainWorld() {
	return mainWorld;
    }
    
    public static String getWelcomeMessage() {
	return welcomeMessage;
    }
    
    private void loadConfig() {
	
	try {
	    getWorldFile().getParentFile().mkdirs();
	    getWorldFile().createNewFile();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
	getConfig().addDefault("config.debugMode", false);
	getConfig().addDefault("config.logLevel", Level.INFO.intValue());
	
	getConfig().addDefault("config.mainWorld", Bukkit.getWorlds().get(0).getName());
	getConfig().addDefault("config.joinAtHub", true);
	getConfig().addDefault("config.welcomeMessage", ChatColor.BOLD + "" + ChatColor.GOLD + "WELCOME!");
	
	this.debugMode = getConfig().getBoolean("config.debugMode");
	this.logLevel = getConfig().getInt("config.logLevel");
	
	mainWorld = Bukkit.getWorld(getConfig().getString("config.mainWorld"));
	joinAtHub = getConfig().getBoolean("config.joinAtHub");
	welcomeMessage = getConfig().getString("config.welcomeMessage");
	
	getConfig().options().copyDefaults(true);
	saveConfig();
    }
    
    private void registerListeners() {
	PluginManager pm = Bukkit.getPluginManager();
	pm.registerEvents(new EntityListener(), this);
	pm.registerEvents(new WeatherListener(), this);
	pm.registerEvents(new ConnectionListener(), this);
	pm.registerEvents(new SignListener(), this);
	pm.registerEvents(new GUIListener(), this);
    }
}
