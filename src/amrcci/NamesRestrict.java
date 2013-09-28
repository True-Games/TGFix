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

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class NamesRestrict implements Listener {

	private PlayerList playerlist;
	private Main main;
	public NamesRestrict(PlayerList config, Main main)
	{
		this.playerlist = config;
		this.main = main;
		startPurgeTask();
	}
	
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onPlayerJoin(PlayerLoginEvent e)
	{
		String plname = e.getPlayer().getName();
		
		if (!e.getPlayer().hasPermission("amrcci.restrictname")) 
		{
			playerlist.plnames.remove(plname.toLowerCase());
			return;
		}
		
		if (playerlist.plnames.containsKey(plname.toLowerCase()))
		{
			if (!playerlist.plnames.get(plname.toLowerCase()).equals(plname))
			{
				e.disallow(Result.KICK_OTHER, "Залогиньтесь используя ваш оригинальнй ник: "+playerlist.plnames.get(plname.toLowerCase())+" (Регистр букв важен)");
			}
		} else
		{
			playerlist.plnames.put(e.getPlayer().getName().toLowerCase(), e.getPlayer().getName());
		}
	}
	
	private void startPurgeTask()
	{
		Thread purgetask = new Thread()
		{
			public void run()
			{
				while (main.isEnabled())
				{
					try {
						Thread.sleep(60*60*1000);
					} catch (InterruptedException e) {
					}
					try {
						for (String plname : new HashSet<String>(playerlist.plnames.values()))
						{
							OfflinePlayer offpl = Bukkit.getOfflinePlayer(plname);
							if (!offpl.isOnline() && System.currentTimeMillis() - offpl.getLastPlayed() > 4*24*60*60*1000)
							{
								playerlist.plnames.remove(plname.toLowerCase());
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		purgetask.start();
	}
	
}
