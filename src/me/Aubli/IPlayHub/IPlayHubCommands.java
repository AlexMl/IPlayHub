package me.Aubli.IPlayHub;

import me.Aubli.IPlayHub.Hub.HubManager;
import me.Aubli.IPlayHub.Hub.HubPoint;
import me.Aubli.IPlayHub.Hub.WorldHub;

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
		WorldHub hub = HubManager.getManager().getHub(playerSender.getWorld());
		if (hub != null) {
		    playerSender.teleport(hub.getSpawnPoint().getLocation(), TeleportCause.PLUGIN);
		} else {
		    printHelp(playerSender);
		}
		return true;
	    }
	    
	    if (args.length == 1) {
		if (args[0].equalsIgnoreCase("init")) {
		    if (playerSender.hasPermission("iplayhub.admin")) {
			try {
			    WorldHub hub = HubManager.getManager().registerHub(playerSender.getWorld(), playerSender.getLocation().clone());
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
		    WorldHub hub = HubManager.getManager().getHub(playerSender.getWorld());
		    if (hub != null) {
			playerSender.teleport(hub.getSpawnPoint().getLocation(), TeleportCause.PLUGIN);
		    } else {
			playerSender.sendMessage("No hub in this world!");// Message
		    }
		    return true;
		}
		
		if (args[0].equalsIgnoreCase("help") || args[0].equals("?")) {
		    printHelp(playerSender);
		    return true;
		}
		
		if (args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("tp")) {
		    // TODO tp gui
		    return true;
		}
		
		if (args[0].equalsIgnoreCase("tps")) {
		    if (playerSender.hasPermission("iplayhub.teleport")) {
			WorldHub hub = HubManager.getManager().getHub(playerSender.getWorld());
			
			if (hub != null) {
			    playerSender.sendMessage("Available Teleport Points:");
			    String tps = "";
			    for (HubPoint tpPoint : hub.getTeleportPoints()) {
				if (playerSender.hasPermission(tpPoint.getPermNode())) {
				    tps += tpPoint.getName() + ", ";
				}
			    }
			    if (tps.length() > 0) {
				playerSender.sendMessage(tps.substring(0, tps.length() - 2));
			    } else {
				playerSender.sendMessage("No available Teleport points for you!");
			    }
			} else {
			    playerSender.sendMessage("No hub in this world!");// Message
			}
			return true;
		    }
		} else {
		    commandDenied(playerSender);
		    return true;
		}
	    }
	    
	    if (args.length == 2) {
		if (args[0].equalsIgnoreCase("tpadd")) {
		    if (playerSender.hasPermission("iplayhub.admin")) {
			WorldHub hub = HubManager.getManager().getHub(playerSender.getWorld());
			if (hub != null) {
			    HubPoint point = hub.addTeleportPoint(playerSender.getLocation().clone(), args[1]);
			    hub.saveConfig();
			    if (point != null) {
				playerSender.sendMessage("Teleport point added!"); // Message
			    } else {
				playerSender.sendMessage("Point named " + args[1] + " alreadey exists!");// Message
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
		
		if (args[0].equalsIgnoreCase("tp")) {
		    if (playerSender.hasPermission("iplayhub.teleport")) {
			WorldHub hub = HubManager.getManager().getHub(playerSender.getWorld());
			
			if (hub != null) {
			    if (hub.getTeleportPoint(args[1]) != null) {
				playerSender.teleport(hub.getTeleportPoint(args[1]).getLocation(), TeleportCause.PLUGIN);
			    } else {
				playerSender.sendMessage("Point named " + args[1] + " does not exist!");// Message
			    }
			} else {
			    playerSender.sendMessage("No hub in this world!");// Message
			}
			return true;
		    } else {
			commandDenied(playerSender);
			return true;
		    }
		}
	    }
	    
	    if (args.length == 3) {
		if (args[0].equalsIgnoreCase("tpadd")) {
		    if (playerSender.hasPermission("iplayhub.admin")) {
			WorldHub hub = HubManager.getManager().getHub(playerSender.getWorld());
			if (hub != null) {
			    HubPoint point = hub.addTeleportPoint(playerSender.getLocation().clone(), args[1], args[2]);
			    hub.saveConfig();
			    if (point != null) {
				playerSender.sendMessage("Teleport point added!"); // Message
			    } else {
				playerSender.sendMessage("Point named " + args[1] + " alreadey exists!");// Message
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
	    }
	    
	}
	return true;
    }
    
    private void printHelp(Player player) {
	// TODO colors
	String version = IPlayHub.getHub().getDescription().getName() + " v" + IPlayHub.getHub().getDescription().getVersion() + " Help";
	int dashAmount = 53 - (1 + 2 + version.length());
	String dashs = "";
	for (int i = 0; i < Math.floor(dashAmount / 2); i++) {
	    dashs += "-";
	}
	
	player.sendMessage("|" + dashs + " " + version + " " + dashs);
	
	player.sendMessage("| /iplayhub");
	player.sendMessage("| /iplayhub init");
	player.sendMessage("| /iplayhub spawn");
	player.sendMessage("| /iplayhub tps");
	player.sendMessage("| /iplayhub tp");
	player.sendMessage("| /iplayhub tp [name]");
	player.sendMessage("| /iplayhub tpadd [name]");
	player.sendMessage("| /iplayhub tpadd [name] [permission]");
	
    }
    
    private void commandDenied(Player player) {
	player.sendMessage(ChatColor.DARK_RED + "You do not have enough Permissions to perform that command!");
    }
    
}
