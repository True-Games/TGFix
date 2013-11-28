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

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
	
	private File configfile;
	public Config(File configfile)
	{
		this.configfile = configfile;
	}
	
	public boolean chatlimiterenabled = true;
	public int chatlimitermsecdiff = 5000;
	public int chatlimitermaxmessagecount = 120;
	
	public boolean doorrecoilenabled = true;

	public boolean voidlistenerenabled = true;
	
	public boolean essentialstpaenabled = true;
	
	public boolean itemremover172enabled = true;
	
	public boolean joinspawnteleportenabled = true;
	public HashSet<String> joinspawnteleportworlds = new HashSet<String>();
	
	public boolean namesrestrictenabled = true;
	
	public boolean nicknamelimiterenabled = true;
	
	public boolean nochainentityexplosionenabled = true;
	
	public boolean nocreativehorseinteractenabled = true;
	
	public boolean quitgamemodechangerenabled = true;

	public void loadConfig()
	{
		FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);
		
		chatlimiterenabled = config.getBoolean("chatlimiter.enabled",chatlimiterenabled);
		chatlimitermsecdiff = config.getInt("chatlimiter.msecdiff",chatlimitermsecdiff);
		chatlimitermaxmessagecount = config.getInt("chatlimiter.maxmessagecount",chatlimitermaxmessagecount);
		
		doorrecoilenabled = config.getBoolean("doorrecoil.enabled",doorrecoilenabled);
		
		voidlistenerenabled = config.getBoolean("voidlistener.enabled",voidlistenerenabled);
		
		essentialstpaenabled = config.getBoolean("essentialstpa.enabled",essentialstpaenabled);
		
		itemremover172enabled = config.getBoolean("itemremover172.enabled",itemremover172enabled);
		
		joinspawnteleportenabled = config.getBoolean("joinspawnteleport.enabled",joinspawnteleportenabled);
		joinspawnteleportworlds = new HashSet<String>(config.getStringList("joinspawnteleport.worlds"));
		
		namesrestrictenabled = config.getBoolean("namesrestrict.enabled",namesrestrictenabled);
		
		nochainentityexplosionenabled = config.getBoolean("nochainentityexplosion.enabled",nochainentityexplosionenabled);
		
		nocreativehorseinteractenabled = config.getBoolean("nocreativehorseinteract.enabled",nocreativehorseinteractenabled);
		
		quitgamemodechangerenabled = config.getBoolean("quitgamemodechanger.enabled",quitgamemodechangerenabled);
		
		config.set("chatlimiter.enabled",chatlimiterenabled);
		config.set("chatlimiter.msecdiff",chatlimitermsecdiff);
		config.set("chatlimiter.maxmessagecount",chatlimitermaxmessagecount);
		
		config.set("doorrecoil.enabled",doorrecoilenabled);
		
		config.set("voidlistener.enabled",voidlistenerenabled);
		
		config.set("essentialstpa.enabled",essentialstpaenabled);
		
		config.set("itemremover172.enabled",itemremover172enabled);
		
		config.set("joinspawnteleport.enabled",joinspawnteleportenabled);
		config.set("joinspawnteleport.worlds", new ArrayList<String>(joinspawnteleportworlds));
		
		config.set("namesrestrict.enabled",namesrestrictenabled);
		
		config.set("nochainentityexplosion.enabled",nochainentityexplosionenabled);
		
		config.set("nocreativehorseinteract.enabled",nocreativehorseinteractenabled);
		
		config.set("quitgamemodechanger.enabled",quitgamemodechangerenabled);

		try {config.save(configfile);} catch (IOException e) {}
	}
	
}
