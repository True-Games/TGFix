package tgfix;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class WorldEditWand implements Listener {

	private Config config;

	private WorldEditPlugin we;

	public WorldEditWand(Config config) {
		this.config = config;
		we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
	}

	private String wandcommand = "//wand";
	private String wandname = ChatColor.LIGHT_PURPLE + "Selection wand";

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().trim().equalsIgnoreCase(wandcommand)) {
			ItemStack wand = new ItemStack(Material.WOOD_AXE);
			ItemMeta im = Bukkit.getItemFactory().getItemMeta(Material.WOOD_AXE);
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

		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		if (item.hasItemMeta() && wandname.equalsIgnoreCase(item.getItemMeta().getDisplayName())) {
			event.setCancelled(true);
			Block block = event.getBlock();
			if (block != null) {
				LocalPlayer lplayer = we.wrapPlayer(player);
				LocalSession session = we.getSession(player);
				Vector vector = new Vector(block.getX(), block.getY(), block.getZ());
		        if (!session.getRegionSelector(lplayer.getWorld()).selectPrimary(vector)) {
		            lplayer.printError("Position already set.");
		            return;
		        }
				session.getRegionSelector(lplayer.getWorld()).explainPrimarySelection(lplayer, session, vector);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPos2(PlayerInteractEvent event) {
		if (!config.customwandenabled) {
			return;
		}

		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		if (item.hasItemMeta() && wandname.equalsIgnoreCase(item.getItemMeta().getDisplayName())) {
			event.setCancelled(true);
			Block block = event.getClickedBlock();
			if (block != null) {
				LocalPlayer lplayer = we.wrapPlayer(player);
				LocalSession session = we.getSession(player);
				Vector vector = new Vector(block.getX(), block.getY(), block.getZ());
		        if (!session.getRegionSelector(lplayer.getWorld()).selectSecondary(vector)) {
		            lplayer.printError("Position already set.");
		            return;
		        }
				session.getRegionSelector(lplayer.getWorld()).explainSecondarySelection(lplayer, session, vector);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityAttack(EntityDamageByEntityEvent event) {
		if (!config.customwandenabled) {
			return;
		}

		Entity edamager = event.getDamager();
		if (edamager instanceof Player) {
			ItemStack item = ((Player) edamager).getItemInHand();
			if (item.hasItemMeta() && wandname.equalsIgnoreCase(item.getItemMeta().getDisplayName())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (!config.customwandenabled) {
			return;
		}

		Iterator<ItemStack> dropsit = event.getDrops().iterator();
		while (dropsit.hasNext()) {
			ItemStack item = dropsit.next();
			if (item.hasItemMeta() && wandname.equalsIgnoreCase(item.getItemMeta().getDisplayName())) {
				dropsit.remove();
			}
		}
	}

}
