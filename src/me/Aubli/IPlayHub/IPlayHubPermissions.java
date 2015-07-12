package me.Aubli.IPlayHub;

public enum IPlayHubPermissions {
    
    Admin("iplayhub.admin"),
    Teleport("iplayhub.teleport");
    
    private String permission;
    
    private IPlayHubPermissions(String permNode) {
	this.permission = permNode;
    }
    
    public String getPermissionNode() {
	return this.permission;
    }
}
