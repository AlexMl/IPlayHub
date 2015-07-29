package me.Aubli.IPlayHub.Listener;

import me.Aubli.IPlayHub.IPlayHub;
import me.Aubli.IPlayHub.IPlayHubMessages;
import me.Aubli.IPlayHub.IPlayHubPermissions;
import me.Aubli.IPlayHub.Hub.HubManager;
import me.Aubli.IPlayHub.Hub.HubPoint;
import me.Aubli.IPlayHub.Hub.WorldHub;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class SignListener implements Listener {
    
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
	Player eventPlayer = event.getPlayer();
	
	if (checkPrefix(event.getLine(0))) {
	    if (eventPlayer.hasPermission(IPlayHubPermissions.Admin.getPermissionNode())) {
		if (!event.getLine(1).isEmpty()) {
		    String hubName = event.getLine(1);
		    String tpName = event.getLine(2);
		    WorldHub hub = HubManager.getManager().getHub(hubName);
		    
		    if (hub != null) {
			HubPoint tpPoint = event.getLine(2).isEmpty() ? hub.getSpawnPoint() : hub.getTeleportPoint(tpName);
			
			if (tpPoint != null) {
			    event.setLine(0, ChatColor.GOLD + "[" + ChatColor.AQUA + "IPH" + ChatColor.GOLD + "]");
			    event.setLine(1, "Hub Teleport");
			    event.setLine(2, ChatColor.DARK_GREEN + tpPoint.getName());
			    event.setLine(3, hubName);
			} else {
			    IPlayHubMessages.sendMessage(eventPlayer, IPlayHubMessages.teleport_does_not_exist, tpName);
			}
		    } else {
			IPlayHubMessages.sendMessage(eventPlayer, IPlayHubMessages.no_hub_with_name, hubName);
		    }
		} else {
		    IPlayHubMessages.sendMessage(eventPlayer, IPlayHubMessages.wrong_sign_layout);
		}
	    } else {
		IPlayHubPermissions.deny(eventPlayer);
		event.setCancelled(true);
		return;
	    }
	}
	
    }
    
    @EventHandler
    public void onSignInteract(PlayerInteractEvent event) {
	
	Player eventPlayer = event.getPlayer();
	
	if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
	    if (event.getClickedBlock().getState() instanceof Sign) {
		Sign eventSign = (Sign) event.getClickedBlock().getState();
		
		if (checkPrefix(eventSign.getLine(0))) {
		    event.setCancelled(true);
		    
		    // TODO Maybe update sign
		    if (eventPlayer.hasPermission(IPlayHubPermissions.Teleport.getPermissionNode())) {
			String hubName = ChatColor.stripColor(eventSign.getLine(3));
			String tpName = ChatColor.stripColor(eventSign.getLine(2));
			WorldHub hub = HubManager.getManager().getHub(hubName);
			
			if (hub != null) {
			    HubPoint hubPoint = tpName.equals(hub.getSpawnPoint().getName()) ? hub.getSpawnPoint() : hub.getTeleportPoint(tpName);
			    if (hubPoint != null) {
				if (hub.isEnabled()) {
				    if (eventPlayer.hasPermission(hubPoint.getPermNode())) {
					IPlayHubMessages.sendMessage(eventPlayer, IPlayHubMessages.teleporting);
					eventPlayer.teleport(hubPoint.getLocation(), TeleportCause.PLUGIN);
				    } else {
					IPlayHubMessages.sendMessage(eventPlayer, IPlayHubMessages.no_permission);
				    }
				    return;
				} else {
				    IPlayHubMessages.sendMessage(eventPlayer, IPlayHubMessages.hub_disabled);
				}
			    } else {
				IPlayHubMessages.sendMessage(eventPlayer, IPlayHubMessages.teleport_does_not_exist, tpName);
			    }
			} else {
			    IPlayHubMessages.sendMessage(eventPlayer, IPlayHubMessages.no_hub_with_name, hubName);
			}
		    } else {
			IPlayHubPermissions.deny(eventPlayer);
			return;
		    }
		}
	    }
	}
    }
    
    @EventHandler
    public void onSignBreak(BlockBreakEvent event) {
	if (event.getBlock().getState() instanceof Sign) {
	    Sign eventSign = (Sign) event.getBlock().getState();
	    
	    if (checkPrefix(eventSign.getLine(0))) {
		if (!event.getPlayer().hasPermission(IPlayHubPermissions.Admin.getPermissionNode())) {
		    event.setCancelled(true);
		    IPlayHubPermissions.deny(event.getPlayer());
		    return;
		}
	    }
	}
    }
    
    private boolean checkPrefix(String signLine) {
	return ChatColor.stripColor(signLine).equalsIgnoreCase(ChatColor.stripColor(IPlayHub.getPluginPrefix()));
    }
}
