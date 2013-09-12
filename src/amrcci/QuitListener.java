package amrcci;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {
	
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onPlayerQuit(PlayerQuitEvent e)
	{		
		if (e.getPlayer().hasPermission("amrcci.ignoregm")) {return;}
			
		e.getPlayer().setGameMode(GameMode.SURVIVAL);
	}

}
