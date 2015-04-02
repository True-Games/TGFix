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

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import tgfix.Config;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.permission.ActorSelectorLimits;

public class WorldEditWand implements Listener {

	private Config config;

	private WorldEditPlugin worldedit;

	public WorldEditWand(Config config) {
		this.config = config;
		worldedit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
	}

	private String wandcommand = "//wand";
	private org.bukkit.Material wandmaterial = org.bukkit.Material.WOOD_AXE;
	private String wandname = ChatColor.LIGHT_PURPLE + "Selection wand";

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if (!config.customwandenabled) {
			return;
		}

		if (event.getMessage().trim().toLowerCase().startsWith(wandcommand)) {
			org.bukkit.inventory.ItemStack wand = new org.bukkit.inventory.ItemStack(wandmaterial);
			org.bukkit.inventory.meta.ItemMeta im = Bukkit.getItemFactory().getItemMeta(wandmaterial);
			im.setDisplayName(wandname);
			wand.setItemMeta(im);
			event.getPlayer().getInventory().addItem(wand);
			event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Wand given");
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPos1ByBreak(BlockBreakEvent event) {
		if (!config.customwandenabled) {
			return;
		}

		org.bukkit.entity.Player player = event.getPlayer();
		org.bukkit.inventory.ItemStack item = player.getItemInHand();
		if (item.getType() == wandmaterial && item.hasItemMeta() && wandname.equalsIgnoreCase(item.getItemMeta().getDisplayName())) {
			event.setCancelled(true);
			org.bukkit.block.Block block = event.getBlock();
			if (block != null) {
				Player weplayer = worldedit.wrapPlayer(player);
				LocalSession session = worldedit.getSession(player);
				Vector vector = new Vector(block.getX(), block.getY(), block.getZ());
		        if (!session.getRegionSelector(weplayer.getWorld()).selectPrimary(vector, ActorSelectorLimits.forActor(weplayer))) {
		            weplayer.printError("Position already set.");
		            return;
		        }
				session.getRegionSelector(weplayer.getWorld()).explainPrimarySelection(weplayer, session, vector);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPos1ByDamage(BlockDamageEvent event) {
		if (!config.customwandenabled) {
			return;
		}

		org.bukkit.entity.Player player = event.getPlayer();
		org.bukkit.inventory.ItemStack item = player.getItemInHand();
		if (item.getType() == wandmaterial && item.hasItemMeta() && wandname.equalsIgnoreCase(item.getItemMeta().getDisplayName())) {
			event.setCancelled(true);
			org.bukkit.block.Block block = event.getBlock();
			if (block != null) {
				Player weplayer = worldedit.wrapPlayer(player);
				LocalSession session = worldedit.getSession(player);
				Vector vector = new Vector(block.getX(), block.getY(), block.getZ());
		        if (!session.getRegionSelector(weplayer.getWorld()).selectPrimary(vector, ActorSelectorLimits.forActor(weplayer))) {
		            weplayer.printError("Position already set.");
		            return;
		        }
				session.getRegionSelector(weplayer.getWorld()).explainPrimarySelection(weplayer, session, vector);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPos2(PlayerInteractEvent event) {
		if (!config.customwandenabled) {
			return;
		}

		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		org.bukkit.entity.Player player = event.getPlayer();
		org.bukkit.inventory.ItemStack item = player.getItemInHand();
		if (item.getType() == wandmaterial && item.hasItemMeta() && wandname.equalsIgnoreCase(item.getItemMeta().getDisplayName())) {
			event.setCancelled(true);
			org.bukkit.block.Block block = event.getClickedBlock();
			if (block != null) {
				Player weplayer = worldedit.wrapPlayer(player);
				LocalSession session = worldedit.getSession(player);
				Vector vector = new Vector(block.getX(), block.getY(), block.getZ());
		        if (!session.getRegionSelector(weplayer.getWorld()).selectSecondary(vector, ActorSelectorLimits.forActor(weplayer))) {
		            weplayer.printError("Position already set.");
		            return;
		        }
				session.getRegionSelector(weplayer.getWorld()).explainSecondarySelection(weplayer, session, vector);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityAttack(EntityDamageByEntityEvent event) {
		if (!config.customwandenabled) {
			return;
		}

		org.bukkit.entity.Entity edamager = event.getDamager();
		if (edamager instanceof org.bukkit.entity.Player) {
			org.bukkit.inventory.ItemStack item = ((org.bukkit.entity.Player) edamager).getItemInHand();
			if (item.getType() == wandmaterial && item.hasItemMeta() && wandname.equalsIgnoreCase(item.getItemMeta().getDisplayName())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (!config.customwandenabled) {
			return;
		}

		Iterator<org.bukkit.inventory.ItemStack> dropsit = event.getDrops().iterator();
		while (dropsit.hasNext()) {
			org.bukkit.inventory.ItemStack item = dropsit.next();
			if (item.getType() == wandmaterial && item.hasItemMeta() && wandname.equalsIgnoreCase(item.getItemMeta().getDisplayName())) {
				dropsit.remove();
			}
		}
	}

}
