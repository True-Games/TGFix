package amrcci;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class VoidListener implements Listener {
	
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onPlayerFallInVoid(EntityDamageEvent e)
	{
		if (e.getCause() == DamageCause.VOID && e.getEntity() instanceof Player)
		{
			Player player = (Player) e.getEntity();
			if (!player.hasPermission("amrcci.ignorevoid"))
			{
				player.setGameMode(GameMode.SURVIVAL);
				player.damage(player.getHealth());
			}
		}
	}
	
}
