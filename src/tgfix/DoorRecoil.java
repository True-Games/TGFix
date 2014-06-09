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

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DoorRecoil implements Listener {

	private Config config;

	public DoorRecoil(Config config) {
		this.config = config;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerOpenDoor(PlayerInteractEvent e) {
		if (!config.doorrecoilenabled) {
			return;
		}

		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && isDoor(e.getClickedBlock()) && e.isCancelled()) {
			Player player = e.getPlayer();
			if (player.isSprinting()) {
				player.setVelocity(player.getLocation().getDirection().multiply(-2.3D).setY(0));
			} else {
				player.setVelocity(player.getLocation().getDirection().multiply(-1.2D).setY(0));
			}
		}
	}

	private boolean isDoor(Block b) {
		return b.getType() == Material.WOODEN_DOOR;
	}

}
