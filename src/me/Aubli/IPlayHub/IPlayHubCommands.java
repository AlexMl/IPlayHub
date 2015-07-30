package me.Aubli.IPlayHub;

import java.util.ArrayList;
import java.util.List;

import me.Aubli.IPlayHub.Hub.HubManager;
import me.Aubli.IPlayHub.Hub.HubPoint;
import me.Aubli.IPlayHub.Hub.WorldHub;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


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
		
		if (args[0].equalsIgnoreCase("spawn")) {
		    WorldHub hub = HubManager.getManager().getHub(playerSender.getWorld());
		    if (hub != null) {
			playerSender.teleport(hub.getSpawnPoint().getLocation(), TeleportCause.PLUGIN);
		    } else {
			IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.no_hub_in_world, playerSender.getWorld().getName());
		    }
		    return true;
		}
		
		if (args[0].equalsIgnoreCase("help") || args[0].equals("?")) {
		    printHelp(playerSender);
		    return true;
		}
		
		if (args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("tp")) {
		    if (playerSender.hasPermission(IPlayHubPermissions.Teleport.getPermissionNode())) {
			Inventory hubInv = Bukkit.createInventory(playerSender, (int) (Math.ceil(HubManager.getManager().getWorldHubs().length / 9.0) * 9), "Teleporters by Hub!");
			
			for (WorldHub hub : HubManager.getManager().getWorldHubs()) {
			    ItemStack hubItem = new ItemStack(Material.CHEST);
			    ItemMeta hubItemMeta = hubItem.getItemMeta();
			    hubItemMeta.setDisplayName(hub.getName());
			    
			    List<String> lore = new ArrayList<String>();
			    lore.add("Teleport Points of hub " + hub.getName() + "!");
			    hubItemMeta.setLore(lore);
			    hubItem.setItemMeta(hubItemMeta);
			    hubInv.addItem(hubItem);
			}
			playerSender.closeInventory();
			playerSender.openInventory(hubInv);
			return true;
		    } else {
			commandDenied(playerSender);
		    }
		    return true;
		}
		
		if (args[0].equalsIgnoreCase("tps")) {
		    if (playerSender.hasPermission(IPlayHubPermissions.Teleport.getPermissionNode())) {
			WorldHub hub = HubManager.getManager().getHub(playerSender.getWorld());
			
			if (hub != null) {
			    IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.available_points);
			    String tps = "";
			    for (HubPoint tpPoint : hub.getTeleportPoints()) {
				if (playerSender.hasPermission(tpPoint.getPermNode())) {
				    tps += tpPoint.getName() + ", ";// TODO do it nicer, color?
				}
			    }
			    if (tps.length() > 0) {
				playerSender.sendMessage(tps.substring(0, tps.length() - 2));
			    } else {
				IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.no_teleport_points);
			    }
			} else {
			    IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.no_hub_in_world, playerSender.getWorld().getName());
			}
			return true;
		    } else {
			commandDenied(playerSender);
			return true;
		    }
		}
		
		printHelp(playerSender);
	    }
	    
	    if (args.length == 2) {
		
		if (args[0].equalsIgnoreCase("init")) {
		    if (playerSender.hasPermission(IPlayHubPermissions.Admin.getPermissionNode())) {
			try {
			    WorldHub hub = HubManager.getManager().registerHub(args[1], playerSender.getWorld(), playerSender.getLocation().clone());
			    IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.hub_created, args[1], hub.getWorld().getName());
			} catch (Exception e) {
			    IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.hub_created_error, args[1], playerSender.getWorld().getName(), e.getMessage());
			    playerSender.sendMessage("Error: " + e.getMessage());
			}
		    } else {
			commandDenied(playerSender);
		    }
		    return true;
		}
		
		if (args[0].equalsIgnoreCase("tpadd")) {
		    if (playerSender.hasPermission(IPlayHubPermissions.Admin.getPermissionNode())) {
			WorldHub hub = HubManager.getManager().getHub(playerSender.getWorld());
			if (hub != null) {
			    HubPoint point = hub.addTeleportPoint(playerSender.getLocation().clone(), args[1]);
			    hub.saveConfig();
			    if (point != null) {
				IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.teleport_added, point.getName());
			    } else {
				IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.teleport_already_exists, args[1]);
			    }
			    return true;
			} else {
			    IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.no_hub_in_world, playerSender.getWorld().getName());
			    return true;
			}
		    } else {
			commandDenied(playerSender);
			return true;
		    }
		}
		
		// INFO: world/hub specific
		if (args[0].equalsIgnoreCase("tp")) {
		    if (playerSender.hasPermission(IPlayHubPermissions.Teleport.getPermissionNode())) {
			WorldHub hub = HubManager.getManager().getHub(playerSender.getWorld());
			
			if (hub != null) {
			    HubPoint tpPoint = hub.getTeleportPoint(args[1]);
			    if (tpPoint != null) {
				if (playerSender.hasPermission(tpPoint.getPermNode())) {
				    IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.teleporting);
				    playerSender.teleport(tpPoint.getLocation(), TeleportCause.PLUGIN);
				} else {
				    commandDenied(playerSender);
				}
			    } else {
				IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.teleport_does_not_exist, args[1]);
			    }
			} else {
			    IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.no_hub_in_world, playerSender.getWorld().getName());
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
		    if (playerSender.hasPermission(IPlayHubPermissions.Admin.getPermissionNode())) {
			WorldHub hub = HubManager.getManager().getHub(playerSender.getWorld());
			if (hub != null) {
			    HubPoint point = hub.addTeleportPoint(playerSender.getLocation().clone(), args[1], args[2]);
			    hub.saveConfig();
			    if (point != null) {
				IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.teleport_added, args[1]);
			    } else {
				IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.teleport_already_exists, args[1]);
			    }
			    return true;
			} else {
			    IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.no_hub_in_world, playerSender.getWorld().getName());
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
	String version = IPlayHub.getHub().getDescription().getName() + " v" + IPlayHub.getHub().getDescription().getVersion() + " Help";
	int dashAmount = 53 - (1 + 2 + version.length());
	
	String dashs = "";
	for (int i = 0; i < Math.floor(dashAmount / 2); i++) {
	    dashs += "-";
	}
	
	player.sendMessage(ChatColor.YELLOW + "|" + ChatColor.AQUA + dashs + " " + ChatColor.YELLOW + version + " " + ChatColor.AQUA + dashs);
	
	player.sendMessage(ChatColor.YELLOW + "|" + ChatColor.AQUA + " /iplayhub");
	player.sendMessage(ChatColor.YELLOW + "|" + ChatColor.AQUA + " /iplayhub init [HubName]");
	player.sendMessage(ChatColor.YELLOW + "|" + ChatColor.AQUA + " /iplayhub spawn");
	player.sendMessage(ChatColor.YELLOW + "|" + ChatColor.AQUA + " /iplayhub tps");
	player.sendMessage(ChatColor.YELLOW + "|" + ChatColor.AQUA + " /iplayhub tp");
	player.sendMessage(ChatColor.YELLOW + "|" + ChatColor.AQUA + " /iplayhub tp [name]");
	player.sendMessage(ChatColor.YELLOW + "|" + ChatColor.AQUA + " /iplayhub tpadd [name]");
	player.sendMessage(ChatColor.YELLOW + "|" + ChatColor.AQUA + " /iplayhub tpadd [name] [permission]");
	
    }
    
    private void commandDenied(Player player) {
	IPlayHubPermissions.deny(player);
    }
    
}
