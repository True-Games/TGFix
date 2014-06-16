package tgfix;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class NegativePayFix implements Listener {

	private Config config;

	public NegativePayFix(Config config) {
		this.config = config;
	}

	private HashSet<String> esscmds = new HashSet<String>(
		Arrays.asList(
			new String[] {
				"/pay",
				"/epay",
				"/essentials:pay",
				"/essentials:epay"
			}
		)
	);

	public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (!config.negativepayfixenabled) {
			return;
		}

		final String[] cmds = event.getMessage().toLowerCase().split("\\s+");
		if (cmds.length >= 3 && esscmds.contains(cmds[0].toLowerCase()) && cmds[2].startsWith("-")) {
			event.setCancelled(true);
		}
	}

}
