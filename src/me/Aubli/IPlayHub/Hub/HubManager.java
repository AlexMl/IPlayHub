package me.Aubli.IPlayHub.Hub;

import java.util.List;

import org.bukkit.World;


public class HubManager {
    
    private static HubManager instance;
    
    private List<HubWorld> hubList;
    
    public HubManager() {
	instance = this;
	initialize();
    }
    
    private void initialize() {
	
    }
    
    public HubWorld getHub(World world) {
	
    }
}
