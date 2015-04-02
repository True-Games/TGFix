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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {

	private Main main;

	public Commands(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String arg2, String[] args) {
		if (sender.hasPermission("tgfix.reload")) {
			if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
				sender.sendMessage("/tgfix reload - перезагрузить конфиг");
				return true;
			} if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				main.config.loadConfig();
				sender.sendMessage("Конфиг перезагружен");
				return true;
			}
		}
		return false;
	}

}
