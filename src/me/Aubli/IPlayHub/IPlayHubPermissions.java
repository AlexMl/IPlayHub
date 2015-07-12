package me.Aubli.IPlayHub;

import org.bukkit.ChatColor;
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
	player.sendMessage(ChatColor.DARK_RED + "You do not have enough Permissions to do that!");
    }
}
