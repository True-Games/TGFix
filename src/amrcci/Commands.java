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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

	private Main main;
	public Commands(Main main)
	{
		this.main = main;
	}
	

	@Override
	public boolean onCommand(CommandSender sender, Command command, String arg2, String[] args) 
	{
		if (this.isAllowed(sender, command.getName(), args))
		{
			if (args.length == 1 && args[0].equalsIgnoreCase("help"))
			{
				sender.sendMessage("/amrcci loadlist - загрузить список игроков NamesRestrict из файла");
				sender.sendMessage("/amrcci savelist - сохранить список игроков NamesRestrict в файл");
				sender.sendMessage("/amrcci purgelist - очистить список игроков NamesRestrict от не играющих игроков");
				sender.sendMessage("/amrcci changenameinlist - сменить имя в списке NamesRestrict");
				sender.sendMessage("/amrcci reload - перезагрузить конфиг");
				return true;
			}
			if (args.length == 1 && args[0].equalsIgnoreCase("loadlist"))
			{
				main.nr.lpllist();
				sender.sendMessage("Список игроков NamesRestrict загружен из файла");
				return true;
			} else
			if (args.length == 1 && args[0].equalsIgnoreCase("savelist"))
			{
				main.nr.spllist();
				sender.sendMessage("Список игроков NamesRestrict сохранён в файл");
				return true;
			} else
			if (args.length == 1 && args[0].equalsIgnoreCase("purgelist"))
			{
				try {
					main.nr.doPurge();
					sender.sendMessage("Список игроков NamesRestrict очищен");
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			} else
			if (args.length == 2 && args[0].equalsIgnoreCase("changenameinlist"))
			{
				if (main.nr.plnames.containsKey(args[1].toLowerCase()))
				{
					main.nr.plnames.put(args[1].toLowerCase(), args[1]);
					sender.sendMessage("Имя в списке NamesRestrict изменено");
				} else
				{
					sender.sendMessage("Данное имя не найдено в списке NamesRestrict");
				}
				return true;
			} else
			if (args.length == 1 && args[0].equalsIgnoreCase("reload"))
			{
				main.config.loadConfig();
				sender.sendMessage("Конфиг перезагружен");
				return true;
			}
		}
		return false;
	}

	
	private boolean isAllowed(CommandSender sender,final String commandName, String[] args)
	{
		boolean allowed = false;
		
		if ((sender instanceof Player)) {
			Player player = (Player) sender;
			if (player.isOp() || player.hasPermission("amrcci.reload")) {
				allowed = true;
			}
		} else if (sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender) {
			// Success, this was from the Console or Remote Console
			allowed = true;
		}
		
		return allowed;
	}
	
}
