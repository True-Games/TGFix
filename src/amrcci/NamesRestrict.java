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

import java.io.File;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class NamesRestrict implements Listener {

	private Config config;

	public NamesRestrict(Main main, Config config) {
		this.config = config;
		for (File playerfile : playersdir.listFiles()) {
			String name = playerfile.getName();
			if (name.endsWith(".dat")) {
				name = name.substring(0, name.length() - 4);
				plnames.put(name.toLowerCase(), name);
			}
		}
	}
	
	File playersdir = new File(Bukkit.getWorlds().get(0).getWorldFolder(), "players");

	protected HashMap<String, String> plnames = new HashMap<String, String>();

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLogin(PlayerLoginEvent e) {
		if (!config.namesrestrictenabled) {
			return;
		}

		String joinname = e.getPlayer().getName();
		String lcname = joinname.toLowerCase();
		if (!plnames.containsKey(lcname)) {
			plnames.put(lcname, joinname);
			return;
		}
		String realname = plnames.get(lcname);
		if (!new File(playersdir, realname+".dat").exists()) {
			plnames.put(lcname, joinname);
			return;
		}
		if (!joinname.equals(realname)) {
			e.disallow(Result.KICK_OTHER, "Залогиньтесь используя ваш оригинальный ник: " + realname);
		}
	}

}
