package redsgreens.FlowerPower;

import java.util.Date;
import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

/**
 * FlowerPower for Bukkit
 *
 * @author redsgreens
 */
public class FlowerPower extends JavaPlugin {
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    private final FlowerPowerPlayerListener playerListener = new FlowerPowerPlayerListener(this);

    public String Name;
    public String Version;
    public FlowerPowerConfig Config;  
    public final HashMap<String, Date> EnabledPlayers = new HashMap<String, Date>();
    
	public PermissionHandler Permissions = null;

    public void onEnable() {
        Name = getDescription().getName();
        Version = getDescription().getVersion();

        Config = new FlowerPowerConfig(this);
    	
      	setupPermissions();

        getServer().getPluginManager().registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Monitor, this);

        System.out.println(this.Name + " v" + this.Version + " is enabled!" );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    // catches the /flowerpower command
    {
    	if(sender instanceof Player)
    	{
    		Player player = (Player) sender;
    		
    		if(cmd.getName().equalsIgnoreCase("flowerpower"))
    		{
    			if(isAuthorized(player, "throw"))
    			{
        			if(Config.RequireCommandToggle)
        			{
        				String playerName = player.getName();
        				if(EnabledPlayers.containsKey(playerName))
        				{
        					player.sendMessage("§c" + this.Name + " disabled.");
        					EnabledPlayers.remove(playerName);
        				}
        				else
        				{
        					player.sendMessage("§c" + this.Name + " enabled using " + Config.WandItem.name() + ".");
        					EnabledPlayers.put(playerName, null);
        				}
        				return true;
        			}
        			else if(Config.ShowErrorsInClient)
        			{
    					player.sendMessage("§cErr: " + this.Name + " command not required.");
    					return true;
        			}
    			}
    			else if(Config.ShowErrorsInClient)
    			{
					player.sendMessage("§cErr: You don't have " + this.Name + " permission.");
					return true;
    			}
    		}

    	}
    	
    	return false;
    }
    
    // return true if Player p has the permission perm
    public boolean isAuthorized(Player p, String perm){
    	boolean retval = p.isOp();

    	try{
    		if(Permissions != null)
    			  if (Permissions.has(p, "flowerpower." + perm))
    			      retval = true;
    	}
    	catch (Exception ex){}
    	
    	return retval;	
    }

    private void setupPermissions() {
    	try{
            Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");

            if (Permissions == null) {
                if (test != null) {
                    Permissions = ((Permissions)test).getHandler();
                	System.out.println(this.Name + ": " + test.getDescription().getName() + " " + test.getDescription().getVersion() + " found");
                }
            }
    	}
    	catch (Exception ex){}
    }

    public void onDisable() {
        System.out.println(this.Name + " v" + this.Version + " is disabled." );
    }
    public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return debugees.get(player);
        } else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }
}

