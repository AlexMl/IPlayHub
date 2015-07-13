package me.Aubli.IPlayHub.HubExceptions;

public class WorldAlreadyInitializedException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public WorldAlreadyInitializedException() {
	super("World is already initialized!");
    }
}
