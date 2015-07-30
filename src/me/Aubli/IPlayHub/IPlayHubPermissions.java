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
    
    public static boolean hasPermission(Player player, IPlayHubPermissions perm) {
	return player.hasPermission(perm.getPermissionNode());
    }
    
    public static void deny(Player player) {
	IPlayHubMessages.sendMessage(player, IPlayHubMessages.no_permission);
    }
}
