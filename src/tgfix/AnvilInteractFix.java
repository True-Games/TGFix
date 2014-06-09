/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */

package tgfix;

import org.bukkit.ChatColor;
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
			player.sendMessage(ChatColor.RED+"You don't have enough permissions to do this");
		}
	}

}
