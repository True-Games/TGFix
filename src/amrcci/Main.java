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

import org.bukkit.plugin.java.JavaPlugin;

//этот плагин служит сборищем фиксов для тех или иных вещей, чтобы не писать для этого отдельные плагины, я докидываю фиксы сюда.
public class Main extends JavaPlugin {

	public Commands commands;
	public NamesRestrict nr;
	public QuitListener ql;
	public VoidListener vl;
	public NoChainExplosion nce;
	public DoorRecoil dr;
	public SpawnTeleport st;
	public NoGamemodeInteract ngi;
	public EssentialsTPA etp;
	public ItemRemover172 ir172;
	public ChatLimiter cl;
        
	@Override
	public void onEnable()
	{
		nr = new NamesRestrict(this);
		nr.start();
		getServer().getPluginManager().registerEvents(nr, this);
		commands = new Commands(this);
		getCommand("amrcci").setExecutor(commands);
		ql = new QuitListener();
		getServer().getPluginManager().registerEvents(ql, this);
		vl = new VoidListener();
		getServer().getPluginManager().registerEvents(vl, this);
		nce = new NoChainExplosion();
		getServer().getPluginManager().registerEvents(nce, this);
		dr = new DoorRecoil();
		getServer().getPluginManager().registerEvents(dr, this);
		st = new SpawnTeleport(this);
		st.loadConfig();
		getServer().getPluginManager().registerEvents(st, this);
		ngi = new NoGamemodeInteract();
		getServer().getPluginManager().registerEvents(ngi, this);
		etp = new EssentialsTPA();
		getServer().getPluginManager().registerEvents(etp, this);
		ir172 = new ItemRemover172();
		getServer().getPluginManager().registerEvents(ir172, this);
		cl = new ChatLimiter(this);
		cl.loadConfig();
		getServer().getPluginManager().registerEvents(cl, this);
	}
        
	@Override
	public void onDisable()
	{
		nr.stop();
		nr = null;
		ql = null;
		vl = null;
		nce = null;
		dr = null;
		st = null;
		ir172 = null;
		cl = null;
		commands = null;
	}
                
}