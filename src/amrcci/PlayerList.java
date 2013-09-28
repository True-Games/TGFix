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
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerList {
	
	
	public ConcurrentHashMap<String, String> plnames = new  ConcurrentHashMap<String, String>();

	private File amrcciconfig = new File("plugins/AMRCCI/playerlist.yml");
	
	public void lpllist()
	{
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(amrcciconfig);
		plnames.clear();
		for (String name : cfg.getStringList("players"))
		{
			plnames.put(name.toLowerCase(), name);
		}
		spllist();
	}
	
	public void spllist()
	{
		FileConfiguration cfg = new YamlConfiguration();
		cfg.set("players", new ArrayList<String>(plnames.values()));
		try {cfg.save(amrcciconfig);} catch (IOException e) {e.printStackTrace();}
	}
	
}
