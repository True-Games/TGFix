package amrcci;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerList {
	
	
	public ConcurrentHashMap<String, String> plnames = new  ConcurrentHashMap<String, String>();

	private File amrcciconfig = new File("plugins/AMRCCI/playerlist.yml");
	
	public void lpllist()
	{
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(amrcciconfig);
		plnames.clear();
		for (String name : cfg.getStringList("players"))
		{
			plnames.put(name.toLowerCase(), name);
		}
		spllist();
	}
	
	public void spllist()
	{
		FileConfiguration cfg = new YamlConfiguration();
		cfg.set("players", new ArrayList<String>(plnames.values()));
		try {cfg.save(amrcciconfig);} catch (IOException e) {e.printStackTrace();}
	}
	
}
