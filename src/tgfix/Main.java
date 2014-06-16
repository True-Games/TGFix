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

import org.bukkit.plugin.java.JavaPlugin;

//some fixes for my servers
public class Main extends JavaPlugin {

	public Config config;

	@Override
	public void onEnable() {
		config = new Config(new File(this.getDataFolder(), "config.yml"));
		config.loadConfig();
		getCommand("tgfix").setExecutor(new Commands(this));
		getServer().getPluginManager().registerEvents(new DoorRecoil(config), this);
		getServer().getPluginManager().registerEvents(new JoinSpawnTeleport(this, config), this);
		getServer().getPluginManager().registerEvents(new EssentialsTPA(config), this);
		getServer().getPluginManager().registerEvents(new ChatLimiter(this, config), this);
		getServer().getPluginManager().registerEvents(new AnvilInteractFix(config), this);
		getServer().getPluginManager().registerEvents(new VehicleCommandsRestrict(config), this);
		getServer().getPluginManager().registerEvents(new CommandLocaleFix(this, config), this);
		getServer().getPluginManager().registerEvents(new NegativePayFix(config), this);
	}

	@Override
	public void onDisable() {
		config = null;
	}

}