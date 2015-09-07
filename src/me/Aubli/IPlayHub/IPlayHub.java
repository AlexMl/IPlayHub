package me.Aubli.IPlayHub;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;

import me.Aubli.IPlayHub.Hub.HubManager;
import me.Aubli.IPlayHub.Listener.ConnectionListener;
import me.Aubli.IPlayHub.Listener.EntityListener;
import me.Aubli.IPlayHub.Listener.GUIListener;
import me.Aubli.IPlayHub.Listener.SignListener;
import me.Aubli.IPlayHub.Listener.WeatherListener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.util.InsertComment.CommentUtil;
import org.util.Logger.PluginOutput;


public class IPlayHub extends JavaPlugin {
    
    /*TODO:
     * -donator things
     * -add message for delayed tps
     */
    
    private static IPlayHub instance;
    
    private static PluginOutput logger;
    
    private static final String prefix = ChatColor.YELLOW + "[" + ChatColor.AQUA + "IPH" + ChatColor.YELLOW + "]" + ChatColor.RESET;
    
    private static ItemStack welcomeBook;
    
    private boolean debugMode;
    private int logLevel;
    private int loadDelay;
    
    // Config values
    private static boolean joinAtHub;
    private static boolean shootFirework;
    private static boolean giveBook;
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
	logger = new PluginOutput(getHub(), IPlayHub.this.debugMode, IPlayHub.this.logLevel);
	
	if (isServerReload()) {
	    HubManager.getManager();
	    
	    getCommand("iplayhub").setExecutor(new IPlayHubCommands());
	    
	    registerListeners();
	    logger.log(getClass(), "Plugin enabled!", false);
	} else {
	    Bukkit.getScheduler().runTaskLater(getHub(), new Runnable() {
		
		@Override
		public void run() {
		    HubManager.getManager();
		    
		    getCommand("iplayhub").setExecutor(new IPlayHubCommands());
		    
		    registerListeners();
		    logger.log(getClass(), "Plugin enabled!", false);
		}
	    }, this.loadDelay * 20L);
	}
    }
    
    private boolean isServerReload() {
	StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
	
	for (StackTraceElement ste : stackTrace) {
	    if (ste.toString().contains("Bukkit.reload")) { // INFO: Magic String
		return true;
	    }
	}
	
	return false;
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
    
    public static boolean isShootFirework() {
	return shootFirework;
    }
    
    public static boolean giveWelcomeBook() {
	return giveBook;
    }
    
    public static World getMainWorld() {
	return mainWorld;
    }
    
    public static String getWelcomeMessage() {
	return welcomeMessage;
    }
    
    public static ItemStack getWelcomeBook() {
	return welcomeBook;
    }
    
    public void loadConfig() {
	
	try {
	    getWorldFile().getParentFile().mkdirs();
	    getWorldFile().createNewFile();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
	getConfig().options().header("\nThis is the configuration file of IPlayHub.\nEvery configuration option has a detailed description.\nFor Questions or ideas visit https://www.github.com/AlexMl/IPlayHub/\n\n");
	
	getConfig().addDefault("config.debugMode", false);
	getConfig().addDefault("config.logLevel", Level.INFO.intValue());
	getConfig().addDefault("config.configLoadDelay", 0);
	
	getConfig().addDefault("config.mainWorld", Bukkit.getWorlds().get(0).getName());
	getConfig().addDefault("config.joinAtHub", true);
	getConfig().addDefault("config.shootFireworkAtJoin", true);
	getConfig().addDefault("config.welcomeMessage", ChatColor.BOLD + "" + ChatColor.GOLD + "WELCOME!");
	getConfig().addDefault("config.giveWelcomeBook", false);
	
	this.debugMode = getConfig().getBoolean("config.debugMode");
	this.logLevel = getConfig().getInt("config.logLevel");
	this.loadDelay = getConfig().getInt("config.configLoadDelay");
	
	mainWorld = Bukkit.getWorld(getConfig().getString("config.mainWorld"));
	joinAtHub = getConfig().getBoolean("config.joinAtHub");
	shootFirework = getConfig().getBoolean("config.shootFireworkAtJoin");
	welcomeMessage = getConfig().getString("config.welcomeMessage");
	giveBook = getConfig().getBoolean("config.giveWelcomeBook");
	
	getConfig().options().copyDefaults(true);
	getConfig().options().copyHeader(true);
	saveConfig();
	
	insertComments();
	
	welcomeBook = new ItemStack(Material.WRITTEN_BOOK);
	BookMeta meta = (BookMeta) welcomeBook.getItemMeta();
	
	File bookFile = new File(getDataFolder(), "bookmeta.yml");
	FileConfiguration bookConfig = YamlConfiguration.loadConfiguration(bookFile);
	
	try {
	    bookFile.createNewFile();
	    
	    bookConfig.addDefault("book.Title", "BookTitle");
	    bookConfig.addDefault("book.Author", "BookAuthor");
	    
	    bookConfig.addDefault("book.pages.1", "Enter text here. This is the first page!");
	    bookConfig.addDefault("book.pages.2", "You can go up to 50 pages with 256 characters each. This is the second page of the book!");
	    bookConfig.addDefault("book.pages.3", "...");
	    
	    bookConfig.options().copyDefaults(true);
	    bookConfig.save(bookFile);
	} catch (IOException e) {
	    getPluginLogger().log(getClass(), Level.WARNING, "Error while saving bookconfig: " + e.getMessage(), true, false, e);
	}
	
	String title = bookConfig.getString("book.Title");
	String author = bookConfig.getString("book.Author");
	
	List<String> bookContent = new ArrayList<String>();
	for (Entry<String, Object> content : bookConfig.getConfigurationSection("book.pages").getValues(true).entrySet()) {
	    bookContent.add(content.getValue().toString());
	}
	
	meta.setTitle(title);
	meta.setAuthor(author);
	meta.setPages(bookContent);
	welcomeBook.setItemMeta(meta);
    }
    
    private void insertComments() {
	File configFile = new File(getDataFolder(), "config.yml");
	
	CommentUtil commentUtil = new CommentUtil(configFile);
	commentUtil.addComment("debugMode", "Development settings for IplayHub. Set to false for normal use!");
	commentUtil.addComment("configLoadDelay", "WorldManager plugins often load worlds only after the server has finished. If IPlayHub is using this worlds the plugin will encounter problems.#You can set a delay (in seconds) to avoid those issues.");
	commentUtil.addComment("mainWorld", "The name of the world which is used as 'spawn' world.");
	commentUtil.addComment("joinAtHub", "If true, the player will be teleported to the spawn location of the hub from the mainWorld every time he joins the server.");
	commentUtil.addComment("shootFireworkAtJoin", "If true, random firework will be shot above the player.");
	commentUtil.addComment("welcomeMessage", "Message that the player receives on join!");
	commentUtil.addComment("giveWelcomeBook", "If true, the player receives a custom book.#The book can be set in the bookmeta.yml file!");
	commentUtil.writeComments();
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
