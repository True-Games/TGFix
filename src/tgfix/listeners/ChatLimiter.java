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

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import tgfix.Config;
import tgfix.Main;

public class ChatLimiter implements Listener {

	private Main main;
	protected Config config;

	public ChatLimiter(Main main, Config config) {
		this.main = main;
		this.config = config;
	}

	protected final ConcurrentHashMap<String, Long> playerspeaktime = new ConcurrentHashMap<String, Long>();
	protected final ConcurrentHashMap<String, Integer> playerspeakcount = new ConcurrentHashMap<String, Integer>();

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onChat(final AsyncPlayerChatEvent e) {
		if (!config.chatlimiterenabled || e.getPlayer().hasPermission("tgfix.bypass")) {
			return;
		}
		final Player player = e.getPlayer();
		if (config.chatallowasciionly) {
			if (!player.hasPermission("tgfix.chatallowunicode")) {
				String message = e.getMessage();
				StringBuilder newmessage = new StringBuilder();
				for (int i = 0; i < e.getMessage().length(); i++) {
					char c = message.charAt(i);
					if (c < 128 || Character.isLetterOrDigit(c)) {
						newmessage.append(c);
					}
				}
				if (message.length() != newmessage.length()) {
					player.sendMessage(ChatColor.RED+"Запрещено использовать не буквенные unicode символы в сообщении, они были автоматически удалены");
					e.setMessage(newmessage.toString());
				}
			}
		}
		final String playername = e.getPlayer().getName();
		playerspeaktime.compute(playername, (key, lastSpeak) -> {
			long current = System.currentTimeMillis();
			if (lastSpeak != null && current - lastSpeak < config.chatlimitermsecdiff)  {
				player.sendMessage(ChatColor.RED + "Можно говорить только раз в " + config.chatlimitermsecdiff / 1000 + " секунд");
				e.setCancelled(true);
				return lastSpeak;
			} else {
				return current;
			}
		});
		playerspeakcount.compute(playername, (key, speakCount) -> {
			if (speakCount != null) {
				if (speakCount > config.chatlimitermaxmessagecount) {
					player.sendMessage(ChatColor.RED + "Вы исчерпали свой лимит сообщений на этот час");
					e.setCancelled(true);
					return speakCount;
				} else {
					return speakCount+1;
				}
			} else {
				Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
					@Override
					public void run() {
						playerspeakcount.remove(key);
					}
				}, 20 * 60 * 60);
				return 1;
			}
		});
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onQuit(PlayerQuitEvent event) {
		playerspeakcount.remove(event.getPlayer().getName());
		playerspeaktime.remove(event.getPlayer().getName());
	}

}
