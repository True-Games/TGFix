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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.GamePhase;

public class NamesRestrict {

	private Main main;
	private File playerlist;
	public NamesRestrict(Main main)
	{
		this.main = main;
		this.playerlist = new File(main.getDataFolder(),"playerlist.yml");
	}
	public void start()
	{
		lpllist();
		startPurgeTask();
		startPacketJoinListener();
	}
	public void stop()
	{
		run = false;
		spllist();
	}
	
	protected void lpllist()
	{
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(playerlist);
		plnames.clear();
		for (String name : cfg.getStringList("players"))
		{
			plnames.put(name.toLowerCase(), name);
		}
		spllist();
	}
	protected void spllist()
	{
		FileConfiguration cfg = new YamlConfiguration();
		cfg.set("players", new ArrayList<String>(plnames.values()));
		try {cfg.save(playerlist);} catch (IOException e) {e.printStackTrace();}
	}
	
	protected ConcurrentHashMap<String, String> plnames = new  ConcurrentHashMap<String, String>();
	private void startPacketJoinListener()
	{
		main.protocolManager.addPacketListener(
				new PacketAdapter(
						PacketAdapter
						.params(main, Packets.Client.HANDSHAKE)
						.clientSide()
						.optionIntercept()
						.gamePhase(GamePhase.BOTH)
				)
				{
					@Override
					public void onPacketReceiving(PacketEvent e) 
					{
						String joinname = e.getPacket().getStrings().getValues().get(0);
						String lcname = joinname.toLowerCase();
						if (!plnames.containsKey(lcname))
						{
							plnames.put(lcname, joinname);
						} else
						{
							String realname = plnames.get(lcname);
							if (!joinname.equals(realname))
							{
								e.setCancelled(true);
								e.getPlayer().kickPlayer("Залогиньтесь используя ваш оригинальный ник: "+realname);
							}
						}
					}
				});
	}

	private boolean run = true;
	private void startPurgeTask()
	{
		new Thread()
		{
			public void run()
			{
				while (run)
				{
					try {Thread.sleep(10*60*1000);} catch (InterruptedException e) {}
					if (!run) {return;}
					for (String plname : new HashSet<String>(plnames.values()))
					{
						OfflinePlayer offpl = Bukkit.getOfflinePlayer(plname);
						if (!offpl.isOnline() && !offpl.hasPlayedBefore())
						{
							plnames.remove(plname.toLowerCase());
						}
					}
				}
			}
		}.start();
	}
	
}
