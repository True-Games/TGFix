package amrcci;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

	private PlayerList playerlist;
	public Commands(PlayerList config)
	{
		this.playerlist = config;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String arg2,
			String[] args) {
		if (this.isAllowed(sender, command.getName(), args))
		{
			if (args.length == 1 && args[0].equalsIgnoreCase("loadlist"))
			{
				playerlist.lpllist();
				sender.sendMessage("Список игроков загружен из файла");
				return true;
			} else
			if (args.length == 1 && args[0].equalsIgnoreCase("savelist"))
			{
				playerlist.spllist();
				sender.sendMessage("Список игроков сохранён в файл");
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
