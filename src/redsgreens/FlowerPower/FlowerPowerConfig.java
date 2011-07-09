package redsgreens.FlowerPower;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import org.bukkit.Material;
import org.yaml.snakeyaml.Yaml;

public class FlowerPowerConfig {
    private final FlowerPower plugin;

    public Material WandItem = Material.RED_ROSE;
	public Boolean ShowErrorsInClient = true;
	public Double CoolDownInterval = 1D;
	public Boolean RequireCommandToggle = true;
	public Boolean TakeItemFromPlayer = false;
	public Double FireballYieldMultiplier = 1D;
	
    public FlowerPowerConfig(FlowerPower instance) {
        plugin = instance;
        
        LoadConfig();
    }

	@SuppressWarnings("unchecked")
	public void LoadConfig()
	{
		try
		{
			// create the data folder if it doesn't exist
			File folder = plugin.getDataFolder();
	    	if(!folder.exists())
	    		folder.mkdirs();
    	
	    	// create a stock config file if it doesn't exist
	    	File configFile = new File(folder, "config.yml");
			if (!configFile.exists()){
				configFile.createNewFile();
				InputStream res = FlowerPower.class.getResourceAsStream("/config.yml");
				FileWriter tx = new FileWriter(configFile);
				for (int i = 0; (i = res.read()) > 0;) tx.write(i);
				tx.flush();
				tx.close();
				res.close();
			}

			// create an empty config
			HashMap<String, Object> configMap = new HashMap<String, Object>();
			
			BufferedReader rx = new BufferedReader(new FileReader(configFile));
			Yaml yaml = new Yaml();
			
			try{
				configMap = (HashMap<String,Object>)yaml.load(rx);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage());
			}
			finally
			{
				rx.close();
			}

			if(configMap.containsKey("ShowErrorsInClient"))
				ShowErrorsInClient = (Boolean)configMap.get("ShowErrorsInClient");
			System.out.println(plugin.Name + ": ShowErrorsInClient=" + ShowErrorsInClient.toString());

			if(configMap.containsKey("RequireCommandToggle"))
				RequireCommandToggle = (Boolean)configMap.get("RequireCommandToggle");
			System.out.println(plugin.Name + ": RequireCommandToggle=" + RequireCommandToggle.toString());

			if(configMap.containsKey("CoolDownInterval"))
			{
				String str = configMap.get("CoolDownInterval").toString();
				CoolDownInterval = Double.parseDouble(str);
				if(CoolDownInterval < 0)
					CoolDownInterval = 1D;
			}
			System.out.println(plugin.Name + ": CoolDownInterval=" + CoolDownInterval.toString() + " seconds");

			if(configMap.containsKey("TakeItemFromPlayer"))
				TakeItemFromPlayer = (Boolean)configMap.get("TakeItemFromPlayer");
			System.out.println(plugin.Name + ": TakeItemFromPlayer=" + TakeItemFromPlayer.toString());

			if(configMap.containsKey("FireballYieldMultiplier"))
			{
				String str = configMap.get("FireballYieldMultiplier").toString();
				FireballYieldMultiplier = Double.parseDouble(str);
				if(FireballYieldMultiplier < 0)
					FireballYieldMultiplier = 1D;
			}
			System.out.println(plugin.Name + ": FireballYieldMultiplier=" + FireballYieldMultiplier.toString());
			
			if(configMap.containsKey("WandItem"))
			{
				String wiStr = configMap.get("WandItem").toString();
				Material wi = Material.matchMaterial(wiStr);
				if(wi != null)
					WandItem = wi;
			}
			System.out.println(plugin.Name + ": WandItem=" + WandItem.name().toLowerCase());

		}
		catch (Exception ex){
			System.out.println(ex.getStackTrace());
		}
	}

}
