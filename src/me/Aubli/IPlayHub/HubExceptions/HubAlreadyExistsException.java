package me.Aubli.IPlayHub.HubExceptions;

public class HubAlreadyExistsException extends HubException {
    
    private static final long serialVersionUID = 1L;
    
    public HubAlreadyExistsException() {
	super("Hub with the given name already exists!");
    }
}
