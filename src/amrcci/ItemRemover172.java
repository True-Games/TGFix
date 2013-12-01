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

package amrcci;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemRemover172 implements Listener {
	
	private Config config;
	public ItemRemover172(Config config)
	{
		this.config = config;
	}
	
	private HashSet<Integer> badids = new HashSet<Integer>(Arrays.asList(8, 9, 10, 11, 26, 34, 36, 43, 51, 55, 59, 60, 62, 63, 64, 68, 71, 74, 75, 83, 90, 92, 93, 94, 99, 100, 104, 105, 115, 117, 118, 119, 120, 124, 125, 127, 132, 140, 141, 142, 144, 149, 150));
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.LOWEST,ignoreCancelled=true)
	public void onInventoryOpen(InventoryOpenEvent e)
	{
		if (!config.itemremover172enabled) {return;}
		
		Inventory inv = e.getInventory();
		if (inv.getType() == InventoryType.CHEST)
		{
			for (int i = 0; i < inv.getSize() ; i++)
			{
				ItemStack item = inv.getItem(i);
				if (item != null && badids.contains(item.getTypeId()))
				{
					inv.setItem(i, new ItemStack(Material.STONE));
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.LOWEST,ignoreCancelled=true)
	public void onInventoryBreak(BlockBreakEvent e)
	{
		if (!config.itemremover172enabled) {return;}
		
		Collection<ItemStack> blockdrops = e.getBlock().getDrops();
		Iterator<ItemStack> dropsit = blockdrops.iterator();
		while (dropsit.hasNext())
		{
			ItemStack drop = dropsit.next();
			if (badids.contains(drop.getTypeId()))
			{
				dropsit.remove();
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.LOWEST,ignoreCancelled=true)
	public void onItemPickup(PlayerPickupItemEvent e)
	{
		if (!config.itemremover172enabled) {return;}
		
		ItemStack item = e.getItem().getItemStack();
		if (badids.contains(item.getTypeId()))
		{
			e.setCancelled(true);
			e.getItem().remove();
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.LOWEST,ignoreCancelled=true)
	public void onJoin(PlayerJoinEvent e)
	{
		if (!config.itemremover172enabled) {return;}
		
		Inventory inv = e.getPlayer().getInventory();
		for (int i = 0; i < inv.getSize() ; i++)
		{
			ItemStack item = inv.getItem(i);
			if (item != null && badids.contains(item.getTypeId()))
			{
				inv.setItem(i, new ItemStack(Material.STONE));
			}
		}
	}

}
