package redsgreens.FlowerPower;

import java.util.Date;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.entity.*;

/**
 * Handle events for all Player related events
 * @author redsgreens
 */
public class FlowerPowerPlayerListener extends PlayerListener {
    private final FlowerPower plugin;

    public FlowerPowerPlayerListener(FlowerPower instance) {
        plugin = instance;
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event)
    // catch player click events
    {
		// return if the event is not a left-click action
		Action action = event.getAction();
		if(action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK) return;

		Player player = event.getPlayer();
		// return if the player doesn't have a red flower in hand
		if(player.getItemInHand().getType() != plugin.Config.WandItem) return;

		// return if the player doesn't have permission
		if(!plugin.isAuthorized(player, "throw")) return;
		
		String playerName = player.getName();
		if(!plugin.Config.RequireCommandToggle || plugin.EnabledPlayers.containsKey(playerName))
		{
			Boolean throwFireball = false;
			Date lastUse = plugin.EnabledPlayers.get(playerName);
			Date now = new Date();
			
			if(!plugin.Config.RequireCommandToggle && !plugin.EnabledPlayers.containsKey(playerName))
				throwFireball = true;
			else if(lastUse == null)
				throwFireball = true;
			else if(now.getTime() - lastUse.getTime() > plugin.Config.CoolDownInterval * 1000)
				throwFireball = true;
			
			if(throwFireball)
			{
				if(plugin.EnabledPlayers.containsKey(playerName))
					plugin.EnabledPlayers.remove(playerName);
				plugin.EnabledPlayers.put(playerName, now);
				
				Location playerLoc = player.getLocation();
				Location loc = playerLoc.add(playerLoc.getDirection().normalize().multiply(3).toLocation(player.getWorld(), playerLoc.getYaw(), playerLoc.getPitch())).add(0, 1D, 0);
				player.getWorld().spawn(loc, Fireball.class);
			}
		}
    }
}

