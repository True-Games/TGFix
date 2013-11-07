package amrcci;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DoorRecoil implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerOpenDoor(PlayerInteractEvent e) 
	{
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && isDoor(e.getClickedBlock()) && e.isCancelled())
		{
			Player player = e.getPlayer();
			if (player.isSprinting()) 
			{
				player.setVelocity(player.getLocation().getDirection().multiply(-2.3D).setY(0));
			} else
			{
				player.setVelocity(player.getLocation().getDirection().multiply(-1.2D).setY(0));
			}
		}
	}
	
	private boolean isDoor(Block b)
	{
		return b.getType() == Material.WOODEN_DOOR || b.getType() == Material.IRON_DOOR;
	}

}
