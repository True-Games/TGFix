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

package tgfix.listeners;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import tgfix.Config;

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
		if (!isItemStackValid(event.getCurrentItem())) {
			event.setCurrentItem(new ItemStack(Material.STONE));
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!config.fix127enabled) {
			return;
		}
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			if (!isItemStackValid(player.getItemInHand())) {
				player.setItemInHand(new ItemStack(Material.STONE));
			}
		}
	}

	private boolean isItemStackValid(ItemStack item) {
		if (item == null) {
			return true;
		}
		try {
			Map<Enchantment, Integer> enchants = item.getEnchantments();
			if (enchants.size() > 5) {
				return false;
			}
			for (Entry<Enchantment, Integer> entry : enchants.entrySet()) {
				if (entry.getValue() > entry.getKey().getMaxLevel()) {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
