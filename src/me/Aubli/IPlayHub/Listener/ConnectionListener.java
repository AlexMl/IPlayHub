package me.Aubli.IPlayHub.Listener;

import java.util.Random;

import me.Aubli.IPlayHub.IPlayHub;
import me.Aubli.IPlayHub.Hub.HubManager;
import me.Aubli.IPlayHub.Hub.WorldHub;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.meta.FireworkMeta;


public class ConnectionListener implements Listener {
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
	Player eventPlayer = event.getPlayer();
	
	if (IPlayHub.isJoinAtHub()) {
	    WorldHub hub = HubManager.getManager().getHub(IPlayHub.getMainWorld());
	    
	    if (hub != null) {
		eventPlayer.teleport(hub.getSpawnPoint().getLocation(), TeleportCause.PLUGIN);
		// eventPlayer.sendTitle("title", "subtitle"); // TODO Use but message from config with enable
	    } else {
		// TODO log
	    }
	}
	
	if (IPlayHub.isShootFirework()) {
	    shootRandomFirework(eventPlayer.getLocation());
	}
	
	eventPlayer.sendMessage(IPlayHub.getPluginPrefix() + " " + IPlayHub.getWelcomeMessage());
    }
    
    private void shootRandomFirework(Location location) {
	Random rand = new Random();
	for (int i = 0; i < 5; i++) {
	    Firework fw = (Firework) location.getWorld().spawnEntity(location.clone().add((rand.nextInt(2) - 1), -1, (rand.nextInt(2) - 1)), EntityType.FIREWORK);
	    FireworkMeta fwMeta = fw.getFireworkMeta();
	    
	    // Get the type
	    Type effectType;
	    switch (rand.nextInt(2) + 1) {
		case 1:
		    effectType = Type.BALL;
		    break;
		
		case 2:
		    effectType = Type.STAR;
		    break;
		
		default:
		    effectType = Type.BALL;
		    break;
	    }
	    
	    // Get our random colours
	    Color c1 = getColor(rand.nextInt(17) + 1);
	    Color c2 = getColor(rand.nextInt(17) + 1);
	    
	    // Create our effect with this
	    FireworkEffect effect = FireworkEffect.builder().flicker(rand.nextBoolean()).withColor(c1).withFade(c2).with(effectType).trail(rand.nextBoolean()).build();
	    
	    // Then apply the effect to the meta
	    fwMeta.addEffect(effect);
	    
	    // Generate some random power and set it
	    // fwMeta.setPower(0);
	    fw.setFireworkMeta(fwMeta);
	}
    }
    
    private Color getColor(int value) {
	
	switch (value) {
	    case 1:
		return Color.AQUA;
	    case 2:
		return Color.BLACK;
	    case 3:
		return Color.BLUE;
	    case 4:
		return Color.FUCHSIA;
	    case 5:
		return Color.GRAY;
	    case 6:
		return Color.GREEN;
	    case 7:
		return Color.LIME;
	    case 8:
		return Color.MAROON;
	    case 9:
		return Color.NAVY;
	    case 10:
		return Color.OLIVE;
	    case 11:
		return Color.ORANGE;
	    case 12:
		return Color.PURPLE;
	    case 13:
		return Color.RED;
	    case 14:
		return Color.SILVER;
	    case 15:
		return Color.TEAL;
	    case 16:
		return Color.WHITE;
	    case 17:
		return Color.YELLOW;
		
	    default:
		return Color.BLUE;
	}
    }
}
