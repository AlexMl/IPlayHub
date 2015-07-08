package me.Aubli.IPlayHub;

import me.Aubli.IPlayHub.Hub.HubManager;
import me.Aubli.IPlayHub.Hub.HubWorld;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class IPlayHubCommands implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
	
	if (!(sender instanceof Player)) {
	    sender.sendMessage("This commands are only for players!");
	    return true;
	}
	
	if (cmd.getName().equalsIgnoreCase("iplayhub")) {
	    
	    Player playerSender = (Player) sender;
	    
	    if (args.length == 0) {
		HubWorld hub = HubManager.getManager().getHub(playerSender.getWorld());
		if (hub != null) {
		    playerSender.teleport(hub.getSpawnLocation(), TeleportCause.PLUGIN);
		} else {
		    printHelp(playerSender);
		}
		return true;
	    }
	    
	    if (args.length == 1) {
		if (args[0].equalsIgnoreCase("init")) {
		    if (playerSender.hasPermission("iplayhub.admin")) {
			try {
			    HubWorld hub = HubManager.getManager().registerHub(playerSender.getWorld(), playerSender.getLocation().clone());
			    playerSender.sendMessage(hub.toString());// Message
			} catch (Exception e) {
			    playerSender.sendMessage("Error: " + e.getMessage());// Message
			}
		    } else {
			commandDenied(playerSender);
		    }
		    return true;
		}
		
		if (args[0].equalsIgnoreCase("spawn")) {
		    HubWorld hub = HubManager.getManager().getHub(playerSender.getWorld());
		    if (hub != null) {
			playerSender.teleport(hub.getSpawnLocation(), TeleportCause.PLUGIN);
		    } else {
			playerSender.sendMessage("No hub in this world!");// Message
		    }
		    return true;
		}
		
		if (args[0].equalsIgnoreCase("tpadd")) {
		    if (playerSender.hasPermission("iplayhub.admin")) {
			HubWorld hub = HubManager.getManager().getHub(playerSender.getWorld());
			if (hub != null) {
			    boolean success = hub.addTeleportPoint(playerSender.getLocation().clone());
			    if (success) {
				playerSender.sendMessage("Teleport point added!"); // Message
			    } else {
				playerSender.sendMessage("Error on adding point!");// Message
			    }
			    return true;
			} else {
			    playerSender.sendMessage("There is no hub in this world!"); // Message
			    return true;
			}
		    } else {
			commandDenied(playerSender);
			return true;
		    }
		}
		
		if (args[0].equalsIgnoreCase("help") || args[0].equals("?")) {
		    printHelp(playerSender);
		    return true;
		}
		
		if (args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("tp")) {
		    // TODO tp gui
		    return true;
		}
	    }
	    
	}
	return true;
    }
    
    private void printHelp(Player player) {
	// TODO colors
	String version = IPlayHub.getHub().getDescription().getName() + "v" + IPlayHub.getHub().getDescription().getVersion() + " Help";
	int dashAmount = 53 - (1 + 2 + version.length());
	String dashs = "";
	for (int i = 0; i < Math.floor(dashAmount / 2); i++) {
	    dashs += "-";
	}
	
	player.sendMessage("|" + dashs + " " + version + " " + dashs);
	
	player.sendMessage("| /iplayhub");
	player.sendMessage("| /iplayhub init");
	player.sendMessage("| /iplayhub spawn");
	player.sendMessage("| /iplayhub tpadd [name]");
	player.sendMessage("| /iplayhub teleport");
    }
    
    private void commandDenied(Player player) {
	player.sendMessage(ChatColor.DARK_RED + "You do not have enough Permissions to perform that command!");
    }
}
