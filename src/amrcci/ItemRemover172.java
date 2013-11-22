package amrcci;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.xephi.authme.events.RestoreInventoryEvent;

public class ItemRemover172 implements Listener {
	
	private HashSet<Integer> badids = new HashSet<Integer>(Arrays.asList(8, 9, 10, 11, 26, 34, 36, 43, 51, 55, 59, 60, 62, 63, 64, 68, 71, 74, 75, 83, 90, 92, 93, 94, 99, 100, 104, 105, 115, 117, 118, 119, 120, 124, 125, 127, 132, 140, 141, 142, 144));
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.HIGH,ignoreCancelled=true)
	public void onInventoryOpen(InventoryOpenEvent e)
	{
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
	@EventHandler(priority=EventPriority.HIGH,ignoreCancelled=true)
	public void onInventoryBreak(BlockBreakEvent e)
	{
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
	@EventHandler(priority=EventPriority.HIGH,ignoreCancelled=true)
	public void onItemPickup(PlayerPickupItemEvent e)
	{
		ItemStack item = e.getItem().getItemStack();
		if (badids.contains(item.getTypeId()))
		{
			e.setCancelled(true);
			e.getItem().remove();
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.HIGH,ignoreCancelled=true)
	public void onAuthMeSetInventory(RestoreInventoryEvent e)
	{
		List<ItemStack> items = new ArrayList<ItemStack>(Arrays.asList(e.getInventory()));
		Iterator<ItemStack> itemsit = items.iterator();
		while (itemsit.hasNext())
		{
			ItemStack item = itemsit.next();
			if (item != null && badids.contains(item.getTypeId()))
			{
				itemsit.remove();
			}
		}
		e.setInventory(items.toArray(new ItemStack[items.size()]));
	}

}
