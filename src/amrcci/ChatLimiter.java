package amrcci;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatLimiter implements Listener {

	private Main main;
	private File configfile;
	public ChatLimiter(Main main)
	{
		this.main = main;
		this.configfile = new File(main.getDataFolder(),"chatlimiterconfig.yml");
	}
	
	private ConcurrentHashMap<String, Long> playerspeaktime = new ConcurrentHashMap<String, Long>();
	private ConcurrentHashMap<String, Integer> playerspeakcount = new ConcurrentHashMap<String, Integer>();
	
	private boolean enabled = true;
	private int msecdiff = 5000;
	private int maxmessagecount = 120;
	public void loadConfig()
	{
		FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);
		enabled = config.getBoolean("enabled",enabled);
		msecdiff = config.getInt("msecdiff",msecdiff);
		maxmessagecount = config.getInt("maxmessagecount",maxmessagecount);
		config.set("enabled",enabled);
		config.set("msecdiff",msecdiff);
		config.set("maxmessagecount",maxmessagecount);
		try {config.save(configfile);} catch (IOException e) {}
	}
	
	@EventHandler(priority=EventPriority.HIGH,ignoreCancelled=true)
	public void onChat(AsyncPlayerChatEvent e)
	{
		if (!enabled) {return;}
		
		final String playername = e.getPlayer().getName();
		if (playerspeaktime.containsKey(playername))
		{
			if (System.currentTimeMillis()-playerspeaktime.get(playername) < msecdiff)
			{
				e.getPlayer().sendMessage(ChatColor.RED+"Можно говорить только раз в 5 секунд");
				e.setCancelled(true);
				return;
			} else
			{
				playerspeaktime.remove(playername);
			}
		} else
		{
			playerspeaktime.put(playername, System.currentTimeMillis());
		}
		if (playerspeakcount.containsKey(playername))
		{
			if (playerspeakcount.get(playername) > maxmessagecount)
			{
				e.getPlayer().sendMessage(ChatColor.RED+"Вы исчерпали свой лимит сообщений на этот час");
				e.setCancelled(true);
				return;
			} else
			{
				playerspeakcount.put(playername, playerspeakcount.get(playername)+1);
			}
		} else
		{
			playerspeakcount.put(playername, 1);
			Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable()
			{
				public void run()
				{
					playerspeakcount.remove(playername);
				}
			} ,20*60*60);
		}
	}

	
	
}
