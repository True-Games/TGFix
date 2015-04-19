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

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import tgfix.Config;

public class ChatLimiter implements Listener {

	protected Config config;

	public ChatLimiter(Config config) {
		this.config = config;
	}

	protected static final long HOURDIFF = 1000L * 60L * 60L;

	protected final ConcurrentHashMap<String, SpeakInfo> speakinfo = new ConcurrentHashMap<String, SpeakInfo>();

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
		speakinfo.compute(e.getPlayer().getName(), (key, speakInfo) -> {
			if (speakInfo == null) {
				return new SpeakInfo();
			}
			long currentTime = System.currentTimeMillis();
			if (currentTime - speakInfo.lastSpeak < config.chatlimitermsecdiff) {
				player.sendMessage(ChatColor.RED + "Можно говорить только раз в " + config.chatlimitermsecdiff / 1000 + " секунд");
				e.setCancelled(true);
			} else {
				speakInfo.lastSpeak = currentTime;
			}
			if (speakInfo.speakCount > config.chatlimitermaxmessagecount) {
				if (currentTime - speakInfo.firstSpeak < HOURDIFF) {
					player.sendMessage(ChatColor.RED + "Вы исчерпали свой лимит сообщений на этот час");
					e.setCancelled(true);
				} else {
					speakInfo.firstSpeak = System.currentTimeMillis();
					speakInfo.speakCount = 0;
				}
			} else {
				speakInfo.speakCount++;
			}
			return speakInfo;
		});
	}

	private static class SpeakInfo {

		private long lastSpeak = System.currentTimeMillis();

		private long firstSpeak = System.currentTimeMillis();
		private int speakCount = 1;

	}

}
