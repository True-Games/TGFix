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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class NamesRestrict implements Listener {

	private Config config;
	private File playerlist;
	public NamesRestrict(Main main, Config config)
	{
		this.config = config;
		this.playerlist = new File(main.getDataFolder(),"playerlist.yml");
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
	
	protected HashMap<String, String> plnames = new HashMap<String, String>();
	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerLogin(PlayerLoginEvent e)
	{
		if (!config.namesrestrictenabled) {return;}
		
		String joinname = e.getPlayer().getName();
		String lcname = joinname.toLowerCase();
		if (!plnames.containsKey(lcname))
		{
			plnames.put(lcname, joinname);
		} else
		{
			String realname = plnames.get(lcname);
			if (!joinname.equals(realname))
			{
				e.disallow(Result.KICK_OTHER, "Залогиньтесь используя ваш оригинальный ник: "+realname);
			}
		}
	}

	
	public void doPurge() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		Iterator<String> namesit = plnames.values().iterator();
		while (namesit.hasNext())
		{
			String name = namesit.next();
			Server server = Bukkit.getServer();
			Class<?> craftofflineplayer = Bukkit.getOfflinePlayer("fakeautopurgeplayer").getClass();
			Constructor<?> ctor = craftofflineplayer.getDeclaredConstructor(server.getClass(), String.class);
			ctor.setAccessible(true);
			OfflinePlayer offpl = (OfflinePlayer) ctor.newInstance(server, name);
			if (!offpl.isOnline() && !offpl.hasPlayedBefore())
			{
				namesit.remove();
			}
		}
	}

}
