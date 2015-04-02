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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import tgfix.Config;
import tgfix.Main;
import fr.xephi.authme.AuthMe;
import fr.xephi.authme.events.AuthMeTeleportEvent;

public class JoinSpawnTeleport implements Listener {

	private Main main;
	private Config config;

	public JoinSpawnTeleport(Main main, Config config) {
		this.main = main;
		this.config = config;
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerLoginAfterTeleport(AuthMeTeleportEvent e) {
		if (!config.joinspawnteleportenabled) {
			return;
		}

		final Player player = e.getPlayer();
		if (config.joinspawnteleportworlds.contains(e.getTo().getWorld().getName()) && !player.hasPermission("tgfix.bypass")) {
			final Location spawn = AuthMe.getInstance().essentialsSpawn;
			if (spawn != null) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(main,
					new Runnable() {
						@Override
						public void run() {
							player.teleport(spawn);
						}
					}
				);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onJoin(PlayerJoinEvent e) {
		if (!config.joinspawnteleportenabled) {
			return;
		}

		final Player player = e.getPlayer();
		if (config.joinspawnteleportworlds.contains(player.getWorld().getName()) && !player.hasPermission("tgfix.bypass")) {
			final Location spawn = AuthMe.getInstance().essentialsSpawn;
			if (spawn != null) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(main,
					new Runnable() {
						@Override
						public void run() {
							player.teleport(spawn);
						}
					}
				);
			}
		}
	}

}
