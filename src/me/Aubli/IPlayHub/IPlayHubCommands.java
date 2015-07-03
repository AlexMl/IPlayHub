package me.Aubli.IPlayHub;

import me.Aubli.IPlayHub.Hub.HubManager;
import me.Aubli.IPlayHub.Hub.HubWorld;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class IPlayHubCommands implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
	// TODO catch sender instanceOf ConsoleSender
	if (cmd.getName().equalsIgnoreCase("ihub")) {
	    
	    Player playerSender = (Player) sender;
	    
	    try {
		HubWorld hub = HubManager.getManager().registerHub(playerSender.getWorld(), playerSender.getLocation().clone());
		playerSender.sendMessage(hub.toString());
	    } catch (Exception e) {
		e.printStackTrace();
		playerSender.sendMessage(e.getMessage());
	    }
	    
	}
	return true;
    }
}
