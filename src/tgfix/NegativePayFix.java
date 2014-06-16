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

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class NegativePayFix implements Listener {

	private Config config;

	public NegativePayFix(Config config) {
		this.config = config;
	}

	private HashSet<String> esscmds = new HashSet<String>(
		Arrays.asList(
			new String[] {
				"/pay",
				"/epay",
				"/essentials:pay",
				"/essentials:epay"
			}
		)
	);

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (!config.negativepayfixenabled) {
			return;
		}

		final String[] cmds = event.getMessage().toLowerCase().split("\\s+");
		if (cmds.length >= 3 && esscmds.contains(cmds[0].toLowerCase()) && cmds[2].startsWith("-")) {
			event.setCancelled(true);
		}
	}

}
