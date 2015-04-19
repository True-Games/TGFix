/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 */

package tgfix;

import java.io.File;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import tgfix.listeners.ChatLimiter;
import tgfix.listeners.CommandLocaleFix;
import tgfix.listeners.DoorRecoil;
import tgfix.listeners.EssentialsTPA;
import tgfix.listeners.Fix127;
import tgfix.listeners.JoinSpawnTeleport;
import tgfix.listeners.NegativePayFix;
import tgfix.listeners.VehicleCommandsRestrict;

public class Main extends JavaPlugin implements Listener {

	public Config config;

	@Override
	public void onEnable() {
		config = new Config(new File(this.getDataFolder(), "config.yml"));
		config.loadConfig();
		getCommand("tgfix").setExecutor(new Commands(this));
		getServer().getPluginManager().registerEvents(new DoorRecoil(config), this);
		getServer().getPluginManager().registerEvents(new JoinSpawnTeleport(this, config), this);
		getServer().getPluginManager().registerEvents(new EssentialsTPA(config), this);
		getServer().getPluginManager().registerEvents(new ChatLimiter(config), this);
		getServer().getPluginManager().registerEvents(new VehicleCommandsRestrict(config), this);
		getServer().getPluginManager().registerEvents(new CommandLocaleFix(this, config), this);
		getServer().getPluginManager().registerEvents(new NegativePayFix(config), this);
		getServer().getPluginManager().registerEvents(new Fix127(config), this);
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		config = null;
	}

}