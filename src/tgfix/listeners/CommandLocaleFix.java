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

import tgfix.Config;
import tgfix.Main;

public class CommandLocaleFix implements Listener {

	private Main plugin;
	private Config config;

	protected HashSet<String> registeredCommands = new HashSet<String>();
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
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			},
			40
		);
	}

	protected LocaleInverter inverter = new LocaleInverter();

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (!config.commandlocalefixenabled) {
			return;
		}

		final String[] cmds = event.getMessage().toLowerCase().substring(1).split("\\s+");
		if (!registeredCommands.contains(cmds[0]) && registeredCommands.contains(inverter.invertLocale(cmds[0]))) {
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

		private final HashMap<Integer, Integer> r2e = new HashMap<Integer, Integer>();
		private final HashMap<Integer, Integer> e2r = new HashMap<Integer, Integer>();
		public LocaleInverter() {
			addMapping('й', 'q'); addMapping('Й', 'Q');
			addMapping('ц', 'w'); addMapping('Ц', 'W');
			addMapping('у', 'e'); addMapping('У', 'E');
			addMapping('к', 'r'); addMapping('К', 'R');
			addMapping('е', 't'); addMapping('Е', 'T');
			addMapping('н', 'y'); addMapping('Н', 'Y');
			addMapping('г', 'u'); addMapping('Г', 'U');
			addMapping('ш', 'i'); addMapping('Ш', 'I');
			addMapping('щ', 'o'); addMapping('Щ', 'O');
			addMapping('з', 'p'); addMapping('З', 'P');
			addMapping('х', '['); addMapping('Х', '{');
			addMapping('ъ', ']'); addMapping('Ъ', '}');
			addMapping('ф', 'a'); addMapping('Ф', 'A');
			addMapping('ы', 's'); addMapping('Ы', 'S');
			addMapping('в', 'd'); addMapping('В', 'D');
			addMapping('а', 'f'); addMapping('А', 'F');
			addMapping('п', 'g'); addMapping('П', 'G');
			addMapping('р', 'h'); addMapping('Р', 'H');
			addMapping('о', 'j'); addMapping('О', 'J');
			addMapping('л', 'k'); addMapping('Л', 'K');
			addMapping('д', 'l'); addMapping('Д', 'L');
			addMapping('ж', ';'); addMapping('Ж', ':');
			addMapping('э', '\''); addMapping('Э', '"');
			addMapping('я', 'z'); addMapping('Я', 'Z');
			addMapping('ч', 'x'); addMapping('Ч', 'X');
			addMapping('с', 'c'); addMapping('С', 'C');
			addMapping('м', 'v'); addMapping('М', 'V');
			addMapping('и', 'b'); addMapping('И', 'B');
			addMapping('т', 'n'); addMapping('Т', 'N');
			addMapping('ь', 'm'); addMapping('Ь', 'M');
			addMapping('б', ','); addMapping('Б', '<');
			addMapping('ю', '.'); addMapping('Ю', '>');
			addMapping('.', '/'); addMapping(',', '?');
			for (Entry<Integer, Integer> entry : r2e.entrySet()) {
				e2r.put(entry.getValue(), entry.getKey());
			}
		}

		private void addMapping(char from, char to) {
			r2e.put(Integer.valueOf(from), Integer.valueOf(to));
		}

		public String invertLocale(String string) {
			StringBuilder sb = new StringBuilder(string.length());
			string.codePoints().forEach(codepoint -> {
				Integer replacement = r2e.get(codepoint);
				if (replacement == null) {
					replacement = e2r.get(codepoint);
				}
				sb.append(replacement != null ? Character.toChars(replacement) : Character.toChars(codepoint));
			});
			return sb.toString();
		}

	}

}
