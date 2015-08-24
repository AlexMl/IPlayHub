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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
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
		    hub.teleport(hub.getSpawnPoint(), playerSender);
		} else {
		    printHelp(playerSender);
		}
		return true;
	    }
	    
	    if (args.length == 1) {
		
		if (args[0].equalsIgnoreCase("spawn")) {
		    WorldHub hub = HubManager.getManager().getHub(playerSender.getWorld());
		    if (hub != null) {
			hub.teleport(hub.getSpawnPoint(), playerSender);
		    } else {
			IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.no_hub_in_world, playerSender.getWorld().getName());
		    }
		    return true;
		}
		
		if (args[0].equalsIgnoreCase("help") || args[0].equals("?")) {
		    printHelp(playerSender);
		    return true;
		}
		
		if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
		    if (IPlayHubPermissions.hasPermission(playerSender, IPlayHubPermissions.Admin)) {
			HubManager.getManager().reloadHubs();
			IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.config_reloaded);
		    } else {
			commandDenied(playerSender);
		    }
		    return true;
		}
		
		if (args[0].equalsIgnoreCase("tp")) {
		    if (IPlayHubPermissions.hasPermission(playerSender, IPlayHubPermissions.Teleport)) {
			Inventory hubInv = Bukkit.createInventory(playerSender, (int) (Math.ceil(HubManager.getManager().getWorldHubs().length / 9.0) * 9), IPlayHub.getPluginPrefix() + " Hubs");
			
			for (WorldHub hub : HubManager.getManager().getWorldHubs()) {
			    if (hub.getApplicableHubPoints(playerSender).length > 0) {
				ItemStack hubItem = new ItemStack(Material.CHEST);
				ItemMeta hubItemMeta = hubItem.getItemMeta();
				hubItemMeta.setDisplayName(hub.getName());
				
				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.LIGHT_PURPLE + "Teleport Points of hub " + ChatColor.GOLD + hub.getName() + ChatColor.LIGHT_PURPLE + "!");
				hubItemMeta.setLore(lore);
				hubItem.setItemMeta(hubItemMeta);
				hubInv.addItem(hubItem);
			    }
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
		    if (IPlayHubPermissions.hasPermission(playerSender, IPlayHubPermissions.Teleport)) {
			WorldHub hub = HubManager.getManager().getHub(playerSender.getWorld());
			
			if (hub != null) {
			    IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.available_points);
			    String tps = "";
			    for (HubPoint tpPoint : hub.getTeleportPoints()) {
				if (playerSender.hasPermission(tpPoint.getPermNode())) {
				    tps += ChatColor.RED + tpPoint.getName() + ChatColor.BLACK + ", ";// TODO do it nicer?
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
		    if (IPlayHubPermissions.hasPermission(playerSender, IPlayHubPermissions.Admin)) {
			try {
			    WorldHub hub = HubManager.getManager().registerHub(args[1], playerSender.getWorld(), playerSender.getLocation().clone());
			    IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.hub_created, args[1], hub.getWorld().getName());
			} catch (Exception e) {
			    IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.hub_created_error, args[1], playerSender.getWorld().getName(), e.getMessage());
			    playerSender.sendMessage("Error: " + e.getMessage()); // TODO remove
			}
		    } else {
			commandDenied(playerSender);
		    }
		    return true;
		}
		
		if (args[0].equalsIgnoreCase("tpadd")) {
		    if (IPlayHubPermissions.hasPermission(playerSender, IPlayHubPermissions.Admin)) {
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
		
		if (args[0].equalsIgnoreCase("tpedit")) {
		    if (IPlayHubPermissions.hasPermission(playerSender, IPlayHubPermissions.Admin)) {
			WorldHub hub = HubManager.getManager().getHub(playerSender.getWorld());
			
			if (hub != null) {
			    HubPoint tpPoint = hub.getTeleportPoint(args[1]);
			    if (tpPoint != null) {
				
				ItemStack tpEditBook = new ItemStack(Material.BOOK_AND_QUILL);
				BookMeta meta = (BookMeta) tpEditBook.getItemMeta();
				
				meta.setAuthor(IPlayHub.getPluginPrefix());
				meta.setTitle(hub.getName() + ":" + tpPoint.getName());
				
				meta.setPages("You can edit the Teleport " + ChatColor.DARK_GREEN + "'" + tpPoint.getName() + "'" + ChatColor.RESET + " using this Book.\n\nEach page contains a different Option:\n\n2. Teleport Name\n3. Teleport Permission\n4. Teleport Delay\n\nSign and Close it if you finished. The book name is not important!", "Teleport Name:\n" + tpPoint.getName(), "Permission:\n" + tpPoint.getPermNode(), "Delay (in Seconds):\n" + tpPoint.getDelay());
				
				tpEditBook.setItemMeta(meta);
				playerSender.getInventory().addItem(tpEditBook);
				
			    } else {
				IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.teleport_does_not_exist, args[1]);
			    }
			} else {
			    IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.no_hub_in_world, playerSender.getWorld().getName());
			}
		    } else {
			commandDenied(playerSender);
			return true;
		    }
		}
		
		// INFO: world/hub specific
		if (args[0].equalsIgnoreCase("tp")) {
		    if (IPlayHubPermissions.hasPermission(playerSender, IPlayHubPermissions.Teleport)) {
			WorldHub hub = HubManager.getManager().getHub(playerSender.getWorld());
			
			if (hub != null) {
			    HubPoint tpPoint = hub.getTeleportPoint(args[1]);
			    if (tpPoint != null) {
				if (playerSender.hasPermission(tpPoint.getPermNode())) {
				    IPlayHubMessages.sendMessage(playerSender, IPlayHubMessages.teleporting);
				    hub.teleport(tpPoint, playerSender);
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
		    if (IPlayHubPermissions.hasPermission(playerSender, IPlayHubPermissions.Admin)) {
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
	player.sendMessage(ChatColor.YELLOW + "|" + ChatColor.AQUA + " /iplayhub help");
	player.sendMessage(ChatColor.YELLOW + "|" + ChatColor.AQUA + " /iplayhub reload");
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
