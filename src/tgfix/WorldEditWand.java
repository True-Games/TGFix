package tgfix;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.entity.Player;

public class WorldEditWand implements Listener {

	private Config config;

	private WorldEditPlugin we;

	public WorldEditWand(Config config) {
		this.config = config;
		we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
	}

	private String wandcommand = "//wand";
	private org.bukkit.Material wandmaterial = org.bukkit.Material.WOOD_AXE;
	private String wandname = ChatColor.LIGHT_PURPLE + "Selection wand";

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().trim().equalsIgnoreCase(wandcommand)) {
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
	public void onPos1(BlockDamageEvent event) {
		if (!config.customwandenabled) {
			return;
		}

		org.bukkit.entity.Player player = event.getPlayer();
		org.bukkit.inventory.ItemStack item = player.getItemInHand();
		if (item.getType() == wandmaterial && item.hasItemMeta() && wandname.equalsIgnoreCase(item.getItemMeta().getDisplayName())) {
			event.setCancelled(true);
			org.bukkit.block.Block block = event.getBlock();
			if (block != null) {
				Player bplayer = we.wrapPlayer(player);
				LocalSession session = we.getSession(player);
				Vector vector = new Vector(block.getX(), block.getY(), block.getZ());
		        if (!session.getRegionSelector(bplayer.getWorld()).selectPrimary(vector)) {
		            bplayer.printError("Position already set.");
		            return;
		        }
				session.getRegionSelector(bplayer.getWorld()).explainPrimarySelection(bplayer, session, vector);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPos2(PlayerInteractEvent event) {
		if (!config.customwandenabled) {
			return;
		}

		org.bukkit.entity.Player player = event.getPlayer();
		org.bukkit.inventory.ItemStack item = player.getItemInHand();
		if (item.getType() == wandmaterial && item.hasItemMeta() && wandname.equalsIgnoreCase(item.getItemMeta().getDisplayName())) {
			event.setCancelled(true);
			org.bukkit.block.Block block = event.getClickedBlock();
			if (block != null) {
				Player bplayer = we.wrapPlayer(player);
				LocalSession session = we.getSession(player);
				Vector vector = new Vector(block.getX(), block.getY(), block.getZ());
		        if (!session.getRegionSelector(bplayer.getWorld()).selectSecondary(vector)) {
		            bplayer.printError("Position already set.");
		            return;
		        }
				session.getRegionSelector(bplayer.getWorld()).explainSecondarySelection(bplayer, session, vector);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityAttack(EntityDamageByEntityEvent event) {
		if (!config.customwandenabled) {
			return;
		}

		org.bukkit.entity.Entity edamager = event.getDamager();
		if (edamager instanceof Player) {
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
