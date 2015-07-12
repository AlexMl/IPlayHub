package me.Aubli.IPlayHub;

import org.bukkit.entity.Player;


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
    
    public static void deny(Player player) {
	IPlayHubMessages.sendMessage(player, IPlayHubMessages.no_permission);
    }
}
