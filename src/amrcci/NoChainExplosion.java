package amrcci;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class NoChainExplosion implements Listener {
	
	@EventHandler(priority=EventPriority.HIGH,ignoreCancelled=true)
	public void onEntityExplode(EntityDamageByEntityEvent e)
	{
		if (e.getCause() == DamageCause.ENTITY_EXPLOSION || e.getCause() == DamageCause.BLOCK_EXPLOSION)
		{
			if (e.getEntity() instanceof Minecart || e.getEntity() instanceof Creeper)
			{
				e.setCancelled(true);
			}
		}
	}
}
