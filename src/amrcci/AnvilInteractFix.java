package amrcci;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;

public class AnvilInteractFix implements Listener {

	private Config config;
	public AnvilInteractFix(Config config) {
		this.config = config;
	}
	
	@EventHandler(priority=EventPriority.HIGH,ignoreCancelled=true)
	public void onInteract(PlayerInteractEvent event) {
		if (!config.anvilinteractfixenabled) {
			return;
		}
		
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		
		if (event.getPlayer().getItemInHand().getType() != Material.ANVIL) {
			return;
		}
		
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		ApplicableRegionSet ars = WGBukkit.getRegionManager(block.getWorld()).getApplicableRegions(block.getLocation()); 
		if (!ars.canBuild(WGBukkit.getPlugin().wrapPlayer(player))) {
			event.setCancelled(true);
		}
	}

}
