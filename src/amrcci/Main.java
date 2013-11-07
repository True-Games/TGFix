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

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

//этот плагин служит сборищем фиксов для тех или иных вещей, чтобы не писать для этого отдельные плагины, я докидываю фиксы сюда.
public class Main  extends JavaPlugin {


	private PlayerList playerlist;
	private Commands commands;
	private NamesRestrict nr;
	private QuitListener ql;
	private VoidListener vl;
	private NoChainExplosion nce;
	private DoorRecoil dr;
	private ProtocolManager protocolManager;
	protected ProtocolManager getProtocolManager()
	{
		return protocolManager;
	}
	
	@Override
	public void onEnable()
	{
		protocolManager = ProtocolLibrary.getProtocolManager();
		playerlist = new PlayerList();
		playerlist.lpllist();
		commands = new Commands(playerlist);
		getCommand("amrcci").setExecutor(commands);
		nr = new NamesRestrict(playerlist, this);
		getServer().getPluginManager().registerEvents(nr, this);
		ql = new QuitListener();
		getServer().getPluginManager().registerEvents(ql, this);
		vl = new VoidListener();
		getServer().getPluginManager().registerEvents(vl, this);
		nce = new NoChainExplosion();
		getServer().getPluginManager().registerEvents(nce, this);
		dr = new DoorRecoil();
		getServer().getPluginManager().registerEvents(dr, this);
	}
	
	@Override
	public void onDisable()
	{
		protocolManager.removePacketListeners(this);
		protocolManager = null;
		playerlist.spllist();
		HandlerList.unregisterAll(this);
		nr = null;
		ql = null;
		vl = null;
		nce = null;
		dr = null;
		commands = null;
		playerlist = null;
	}
		
}

