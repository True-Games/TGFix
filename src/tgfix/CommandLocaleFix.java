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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.PluginManager;

public class CommandLocaleFix implements Listener {

	private Config config;

	private HashSet<String> registeredCommands = new HashSet<String>();
	private HashMap<Character, Character> charsMappings = new HashMap<Character, Character>();
	public CommandLocaleFix(Main plugin, Config config) {
		this.config = config;
		charsMappings.put('й', 'q'); charsMappings.put('Й', 'Q');
		charsMappings.put('ц', 'w'); charsMappings.put('Ц', 'W');
		charsMappings.put('у', 'e'); charsMappings.put('У', 'E');
		charsMappings.put('к', 'r'); charsMappings.put('К', 'R');
		charsMappings.put('е', 't'); charsMappings.put('Е', 'T');
		charsMappings.put('н', 'y'); charsMappings.put('Н', 'Y');
		charsMappings.put('г', 'u'); charsMappings.put('Г', 'U');
		charsMappings.put('ш', 'i'); charsMappings.put('Ш', 'I');
		charsMappings.put('щ', 'o'); charsMappings.put('Щ', 'O');
		charsMappings.put('з', 'p'); charsMappings.put('З', 'P');
		charsMappings.put('х', '['); charsMappings.put('Х', '{');
		charsMappings.put('ъ', ']'); charsMappings.put('Ъ', '}');
		charsMappings.put('ф', 'a'); charsMappings.put('Ф', 'A');
		charsMappings.put('ы', 's'); charsMappings.put('Ы', 'S');
		charsMappings.put('в', 'd'); charsMappings.put('В', 'D');
		charsMappings.put('а', 'f'); charsMappings.put('А', 'F');
		charsMappings.put('п', 'g'); charsMappings.put('П', 'G');
		charsMappings.put('р', 'h'); charsMappings.put('Р', 'H');
		charsMappings.put('о', 'j'); charsMappings.put('О', 'J');
		charsMappings.put('л', 'k'); charsMappings.put('Л', 'K');
		charsMappings.put('д', 'l'); charsMappings.put('Д', 'L');
		charsMappings.put('ж', ';'); charsMappings.put('Ж', ':');
		charsMappings.put('э', '\''); charsMappings.put('Э', '"');
		charsMappings.put('я', 'z'); charsMappings.put('Я', 'Z');
		charsMappings.put('ч', 'x'); charsMappings.put('Ч', 'X');
		charsMappings.put('с', 'c'); charsMappings.put('С', 'C');
		charsMappings.put('м', 'v'); charsMappings.put('М', 'V');
		charsMappings.put('и', 'b'); charsMappings.put('И', 'B');
		charsMappings.put('т', 'n'); charsMappings.put('Т', 'N');
		charsMappings.put('ь', 'm'); charsMappings.put('Ь', 'M');
		charsMappings.put('б', ','); charsMappings.put('Б', '<');
		charsMappings.put('ю', '.'); charsMappings.put('Ю', '>');
		charsMappings.put('.', '/'); charsMappings.put(',', '?');
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, 
			new Runnable() {
				@Override
				public void run() {
					try {
						PluginManager pluginmanager = Bukkit.getPluginManager();
						Class<? extends PluginManager> managerclass = pluginmanager.getClass();
						Field commandMapField = managerclass.getDeclaredField("commandMap");
						commandMapField.setAccessible(true);
						CommandMap commandMap = (CommandMap) commandMapField.get(pluginmanager);
						Method getCommandsMethod = commandMap.getClass().getMethod("getCommands");
						getCommandsMethod.setAccessible(true);
						@SuppressWarnings("unchecked")
						Collection<Command> commands = (Collection<Command>) getCommandsMethod.invoke(commandMap);
						for (Command cmd : commands) {
							if (cmd.isRegistered()) {
								registeredCommands.add(cmd.getName().toLowerCase());
								for (String alias : cmd.getAliases()) {
									registeredCommands.add(alias.toLowerCase());
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			},
			40
		);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (!config.commandlocalefixenabled) {
			return;
		}

		final String[] cmds = event.getMessage().toLowerCase().substring(1).split("\\s+");
		if (registeredCommands.contains(remapToEnglish(cmds[0]))) {
			event.setMessage(remapToEnglish(event.getMessage()));
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onAsyncChat(AsyncPlayerChatEvent event) {
		if (!config.commandlocalefixenabled) {
			return;
		}

		String message = event.getMessage();
		if (message.startsWith(".")) {
			final String[] cmds = message.toLowerCase().substring(1).split("\\s+");
			if (registeredCommands.contains(remapToEnglish(cmds[0]))) {
				event.setCancelled(true);
				event.getPlayer().chat("/" + remapToEnglish(message.substring(1)));
			}
		}
	}

	private String remapToEnglish(String cmd) {
		StringBuilder sb = new StringBuilder(255);
		for (char c : cmd.toCharArray()) {
			if (charsMappings.containsKey(c)) {
				sb.append(charsMappings.get(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
