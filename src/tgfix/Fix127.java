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

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class Fix127 implements Listener {

	private Config config;
	public Fix127(Config config) {
		this.config = config;
	}

	@EventHandler(ignoreCancelled = true)
	public void onClick(InventoryClickEvent event) {
		if (!config.fix127enabled) {
			return;
		}
		checkItemStack(event.getWhoClicked(), event.getCurrentItem());
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!config.fix127enabled) {
			return;
		}
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			checkItemStack(player, player.getItemInHand());
		}
	}

	private void checkItemStack(HumanEntity humanEntity, ItemStack item) {
		if (item == null) {
			return;
		}
		for (int level : item.getEnchantments().values()) {
			if (level > config.fix127maxlevel) {
				item.setType(Material.STONE);
				Bukkit.getBanList(Type.NAME).addBan(humanEntity.getName(), "127", null, "TGFix");
				return;
			}
		}
	}

}
