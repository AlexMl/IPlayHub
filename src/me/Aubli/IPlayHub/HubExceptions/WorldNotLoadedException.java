package me.Aubli.IPlayHub.HubExceptions;

public class WorldNotLoadedException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public WorldNotLoadedException() {
	super("Hub world is not loaded by Bukkit!");
    }
    
}
