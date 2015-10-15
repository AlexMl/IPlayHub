package me.Aubli.IPlayHub;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public enum IPlayHubMessages {
    
    no_permission(ChatColor.DARK_RED + "You do not have enough Permissions for that!"),
    no_hub_in_world(ChatColor.RED + "There is no Hub available in " + ChatColor.GOLD + "%s" + ChatColor.RED + " !"),
    no_hub_with_name(ChatColor.RED + "There is no Hub named " + ChatColor.GOLD + "%s" + ChatColor.RED + "!"),
    no_teleport_points(ChatColor.RED + "There are no teleport positions available for you!"),
    hub_created(ChatColor.GREEN + "Hub " + ChatColor.GOLD + "%s" + ChatColor.GREEN + " in world " + ChatColor.GOLD + "%s" + ChatColor.GREEN + " successfully created!"),
    hub_disabled(ChatColor.RED + "The Hub is currently offline!"),
    hub_created_error(ChatColor.RED + "Hub " + ChatColor.GOLD + "%s" + ChatColor.RED + " in world " + ChatColor.GOLD + "%s" + ChatColor.RED + " could not be registered! %s"),
    available_points(ChatColor.GOLD + "Your available teleport positions:"),
    wrong_sign_layout(ChatColor.RED + "The sign has not the correct layout!"),
    teleport_added(ChatColor.GREEN + "Teleport point " + ChatColor.GOLD + "%s" + ChatColor.GREEN + " added!"),
    teleport_already_exists(ChatColor.RED + "Teleport point " + ChatColor.GOLD + "%s" + ChatColor.RED + " already exists!"),
    teleport_does_not_exist(ChatColor.RED + "Teleport point " + ChatColor.GOLD + "%s" + ChatColor.RED + " does not exist!"),
    teleport_not_available(ChatColor.RED + "Teleport point " + ChatColor.GOLD + "%s" + ChatColor.RED + " is not available!"),
    teleporting(ChatColor.GREEN + "Teleporting to " + ChatColor.GOLD + "%s" + ChatColor.GREEN + " ..."),
    teleporting_in(ChatColor.GREEN + "Teleporting to " + ChatColor.GOLD + "%s" + ChatColor.GREEN + " in " + ChatColor.GOLD + "%s" + ChatColor.GREEN + " seconds ..."),
    pvp_denied(ChatColor.RED + "PvP is disabled in this region!"),
    config_saved(ChatColor.GREEN + "Configuration saved successfully!"),
    config_reloaded(ChatColor.GREEN + "Configuration successfully reloaded!"), ;
    
    private String message;
    
    private IPlayHubMessages(String message) {
	this.message = message;
    }
    
    public String getMessage() {
	return this.message;
    }
    
    public static void sendMessage(Player player, IPlayHubMessages message) {
	player.sendMessage(IPlayHub.getPluginPrefix() + " " + message.getMessage());
    }
    
    public static void sendMessage(Player player, IPlayHubMessages message, Object... args) {
	player.sendMessage(IPlayHub.getPluginPrefix() + " " + String.format(message.getMessage(), args));
    }
}
