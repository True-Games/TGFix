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
import org.bukkit.event.Listener;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.GamePhase;

public class NamesRestrict implements Listener {

	private PlayerList playerlist;
	private Main main;
	public NamesRestrict(PlayerList config, Main main)
	{
		this.playerlist = config;
		this.main = main;
		startPurgeTask();
		startPacketJoinListener();
	}
	
	private void startPacketJoinListener()
	{
		main.getProtocolManager().addPacketListener(
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
						String playername = e.getPacket().getStrings().getValues().get(0);
						String playernamelc = playername.toLowerCase();
						//ad to list if player is not in list
						if (!playerlist.plnames.containsKey(playernamelc))
						{
							addToList(playername, playernamelc);
							return;
						}
						//check if player is alowed to join with this name
						if (!isAllowedToJoin(playername, playernamelc))
						{
							e.setCancelled(true);
							e.getPlayer().kickPlayer("Залогиньтесь используя ваш оригинальный ник: "+playerlist.plnames.get(playername.toLowerCase()));
						}
					}
				});
	}
	
	private void addToList(String playername, String playernamelc)
	{
		playerlist.plnames.put(playernamelc, playername);
	}
	
	
	private boolean isAllowedToJoin(String playername, String playernamelc)
	{	
		return playerlist.plnames.get(playernamelc).equals(playername);
	}
	
	private void startPurgeTask()
	{
		Thread purgetask = new Thread()
		{
			public void run()
			{
				while (main.isEnabled())
				{
					try {Thread.sleep(10*60*1000);} catch (InterruptedException e) {}
					try {
						for (String plname : new HashSet<String>(playerlist.plnames.values()))
						{
							OfflinePlayer offpl = Bukkit.getOfflinePlayer(plname);
							if (!offpl.isOnline() && !offpl.hasPlayedBefore())
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
