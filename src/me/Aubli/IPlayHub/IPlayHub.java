package me.Aubli.IPlayHub;

import org.bukkit.plugin.java.JavaPlugin;


public class IPlayHub extends JavaPlugin {
    
    private static IPlayHub instance;
    
    @Override
    public void onDisable() {
    }
    
    @Override
    public void onEnable() {
    }
    
    public static IPlayHub getHub() {
	return instance;
    }
}
