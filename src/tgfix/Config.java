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

package tgfix;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

	private File configfile;

	public Config(File configfile) {
		this.configfile = configfile;
	}

	public boolean chatlimiterenabled = true;
	public int chatlimitermsecdiff = 5000;
	public int chatlimitermaxmessagecount = 120;
	public boolean chatallowasciionly = true;

	public boolean doorrecoilenabled = true;

	public boolean essentialstpaenabled = true;

	public boolean joinspawnteleportenabled = true;
	public HashSet<String> joinspawnteleportworlds = new HashSet<String>();

	public boolean vechiclecommandsrestrictenabled = true;

	public boolean commandlocalefixenabled = true;

	public boolean negativepayfixenabled = true;

	public boolean customwandenabled = true;

	public boolean fix127enabled = true;
	public int fix127maxlevel = 15;

	public void loadConfig() {
		FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);

		chatlimiterenabled = config.getBoolean("chatlimiter.enabled", chatlimiterenabled);
		chatlimitermsecdiff = config.getInt("chatlimiter.msecdiff", chatlimitermsecdiff);
		chatlimitermaxmessagecount = config.getInt("chatlimiter.maxmessagecount", chatlimitermaxmessagecount);
		chatallowasciionly = config.getBoolean("chatlimiter.allowasciionly", chatallowasciionly);

		doorrecoilenabled = config.getBoolean("doorrecoil.enabled",	doorrecoilenabled);

		essentialstpaenabled = config.getBoolean("essentialstpa.enabled", essentialstpaenabled);

		joinspawnteleportenabled = config.getBoolean("joinspawnteleport.enabled", joinspawnteleportenabled);
		joinspawnteleportworlds = new HashSet<String>(config.getStringList("joinspawnteleport.worlds"));

		vechiclecommandsrestrictenabled = config.getBoolean("vechiclecommandsrestrict.enabled", vechiclecommandsrestrictenabled);

		commandlocalefixenabled = config.getBoolean("commandlocalefix.enabled", commandlocalefixenabled);

		negativepayfixenabled = config.getBoolean("negativepayfix.enabled", negativepayfixenabled);

		customwandenabled = config.getBoolean("customwewand.enabled", customwandenabled);

		fix127enabled = config.getBoolean("fix127.enabled", fix127enabled);
		fix127maxlevel = config.getInt("fix127.maxlevel", fix127maxlevel);

		config.set("chatlimiter.enabled", chatlimiterenabled);
		config.set("chatlimiter.msecdiff", chatlimitermsecdiff);
		config.set("chatlimiter.maxmessagecount", chatlimitermaxmessagecount);
		config.set("chatlimiter.allowasciionly", chatallowasciionly);

		config.set("doorrecoil.enabled", doorrecoilenabled);

		config.set("essentialstpa.enabled", essentialstpaenabled);

		config.set("joinspawnteleport.enabled", joinspawnteleportenabled);
		config.set("joinspawnteleport.worlds", new ArrayList<String>(joinspawnteleportworlds));

		config.set("vechiclecommandsrestrict.enabled", vechiclecommandsrestrictenabled);

		config.set("commandlocalefix.enabled", commandlocalefixenabled);

		config.set("negativepayfix.enabled", negativepayfixenabled);

		config.set("customwewand.enabled", customwandenabled);

		config.set("fix127.enabled", fix127enabled);
		config.set("fix127.maxlevel", fix127maxlevel);

		try {
			config.save(configfile);
		} catch (IOException e) {
		}
	}

}
