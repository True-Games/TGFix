package amrcci;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class Main  extends JavaPlugin {


	private PlayerList playerlist;
	private Commands commands;
	private NamesRestrict nr;
	private QuitListener ql;

	
	@Override
	public void onEnable()
	{
		playerlist = new PlayerList();
		playerlist.lpllist();
		commands = new Commands(playerlist);
		getCommand("amrcci").setExecutor(commands);
		nr = new NamesRestrict(playerlist, this);
		getServer().getPluginManager().registerEvents(nr, this);
		ql = new QuitListener();
		getServer().getPluginManager().registerEvents(ql, this);
	}
	
	@Override
	public void onDisable()
	{
		playerlist.spllist();
		HandlerList.unregisterAll(this);
		nr = null;
		ql = null;
		commands = null;
		playerlist = null;
	}
		
}

