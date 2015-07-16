package me.Aubli.IPlayHub.HubExceptions;

public class HubAlreadyExistsException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public HubAlreadyExistsException() {
	super("Hub with the given name already exists!");
    }
}
