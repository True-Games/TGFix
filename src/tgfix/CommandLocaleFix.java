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
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.PluginManager;

public class CommandLocaleFix implements Listener {

	private Main plugin;
	private Config config;

	private HashSet<String> registeredCommands = new HashSet<String>();
	public CommandLocaleFix(Main plugin, Config config) {
		this.plugin = plugin;
		this.config = config;
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

	private LocaleInverter inverter = new LocaleInverter();

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (!config.commandlocalefixenabled) {
			return;
		}

		final String[] cmds = event.getMessage().toLowerCase().substring(1).split("\\s+");
		if (!registeredCommands.contains(cmds[0])) {
			event.setMessage("/"+inverter.invertLocale(event.getMessage().substring(1)));
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onAsyncChat(AsyncPlayerChatEvent event) {
		if (!config.commandlocalefixenabled) {
			return;
		}

		final String message = event.getMessage();
		if (message.startsWith(".")) {
			final String[] cmds = message.toLowerCase().substring(1).split("\\s+");
			if (registeredCommands.contains(inverter.invertLocale(cmds[0]))) {
				event.setCancelled(true);
				final Player player = event.getPlayer();
				Bukkit.getScheduler().scheduleSyncDelayedTask(
					plugin,
					new Runnable() {
						@Override
						public void run() {
							if (player.isOnline()) {
								player.chat("/" + inverter.invertLocale(message.substring(1)));
							}
						}
					}
				);
			}
		}
	}

	private static class LocaleInverter {
		private HashMap<Character, Character> r2e = new HashMap<Character, Character>();
		private HashMap<Character, Character> e2r = new HashMap<Character, Character>();
		public LocaleInverter() {
			r2e.put('й', 'q'); r2e.put('Й', 'Q');
			r2e.put('ц', 'w'); r2e.put('Ц', 'W');
			r2e.put('у', 'e'); r2e.put('У', 'E');
			r2e.put('к', 'r'); r2e.put('К', 'R');
			r2e.put('е', 't'); r2e.put('Е', 'T');
			r2e.put('н', 'y'); r2e.put('Н', 'Y');
			r2e.put('г', 'u'); r2e.put('Г', 'U');
			r2e.put('ш', 'i'); r2e.put('Ш', 'I');
			r2e.put('щ', 'o'); r2e.put('Щ', 'O');
			r2e.put('з', 'p'); r2e.put('З', 'P');
			r2e.put('х', '['); r2e.put('Х', '{');
			r2e.put('ъ', ']'); r2e.put('Ъ', '}');
			r2e.put('ф', 'a'); r2e.put('Ф', 'A');
			r2e.put('ы', 's'); r2e.put('Ы', 'S');
			r2e.put('в', 'd'); r2e.put('В', 'D');
			r2e.put('а', 'f'); r2e.put('А', 'F');
			r2e.put('п', 'g'); r2e.put('П', 'G');
			r2e.put('р', 'h'); r2e.put('Р', 'H');
			r2e.put('о', 'j'); r2e.put('О', 'J');
			r2e.put('л', 'k'); r2e.put('Л', 'K');
			r2e.put('д', 'l'); r2e.put('Д', 'L');
			r2e.put('ж', ';'); r2e.put('Ж', ':');
			r2e.put('э', '\''); r2e.put('Э', '"');
			r2e.put('я', 'z'); r2e.put('Я', 'Z');
			r2e.put('ч', 'x'); r2e.put('Ч', 'X');
			r2e.put('с', 'c'); r2e.put('С', 'C');
			r2e.put('м', 'v'); r2e.put('М', 'V');
			r2e.put('и', 'b'); r2e.put('И', 'B');
			r2e.put('т', 'n'); r2e.put('Т', 'N');
			r2e.put('ь', 'm'); r2e.put('Ь', 'M');
			r2e.put('б', ','); r2e.put('Б', '<');
			r2e.put('ю', '.'); r2e.put('Ю', '>');
			r2e.put('.', '/'); r2e.put(',', '?');
			for (Entry<Character, Character> entry : r2e.entrySet()) {
				e2r.put(entry.getValue(), entry.getKey());
			}
		}

		public String invertLocale(String string) {
			StringBuilder sb = new StringBuilder(255);
			for (char c : string.toCharArray()) {
				if (r2e.containsKey(c)) {
					sb.append(r2e.get(c));
				} else if (e2r.containsKey(c)) {
					sb.append(e2r.get(c));
				} else {
					sb.append(c);
				}
			}
			return sb.toString();
		}

	}

}
