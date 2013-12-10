/**
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
*
*/

package amrcci;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

//этот плагин служит сборищем фиксов для тех или иных вещей, чтобы не писать для этого отдельные плагины, я докидываю фиксы сюда.
public class Main extends JavaPlugin {

	public Commands commands;
	public Config config;
	public NamesRestrict nr;
	public NicknameLimiter nl;
	public VoidListener vl;
	public NoChainEntityExplosion nce;
	public DoorRecoil dr;
	public JoinSpawnTeleport st;
	public EssentialsTPA etp;
	public ItemRemover172 ir172;
	public ChatLimiter cl;
        
	@Override
	public void onEnable()
	{
		config = new Config(new File(this.getDataFolder(),"config.yml"));
		config.loadConfig();
		commands = new Commands(this);
		getCommand("amrcci").setExecutor(commands);
		nr = new NamesRestrict(this, config);
		nr.lpllist();
		getServer().getPluginManager().registerEvents(nr, this);
		nl = new NicknameLimiter(config);
		getServer().getPluginManager().registerEvents(nl, this);
		nce = new NoChainEntityExplosion(config);
		getServer().getPluginManager().registerEvents(nce, this);
		vl = new VoidListener(config);
		getServer().getPluginManager().registerEvents(vl, this);
		dr = new DoorRecoil(config);
		getServer().getPluginManager().registerEvents(dr, this);
		st = new JoinSpawnTeleport(this, config);
		getServer().getPluginManager().registerEvents(st, this);
		etp = new EssentialsTPA(config);
		getServer().getPluginManager().registerEvents(etp, this);
		ir172 = new ItemRemover172(config);
		getServer().getPluginManager().registerEvents(ir172, this);
		cl = new ChatLimiter(this, config);
		getServer().getPluginManager().registerEvents(cl, this);
	}
        
	@Override
	public void onDisable()
	{
		nr.spllist();
		nr = null;
		vl = null;
		nl = null;
		nce = null;
		dr = null;
		st = null;
		ir172 = null;
		cl = null;
		commands = null;
		config = null;
	}
                
}