package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;

/**
 * Created by Chase(Xwaffle) on 4/23/2017.
 */
public class ClearCommand extends VanillaCommand {

	public ClearCommand(String name) {
		super(name, "%nukkit.command.clear.description", "%commands.clear.usage");
		this.setPermission("nukkit.command.clear");
		this.commandParameters.clear();
		this.commandParameters.put("default", new CommandParameter[] {
				new CommandParameter("player", CommandParameter.ARG_TYPE_TARGET, false),
		});
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!this.testPermission(sender)) {
			return true;
		}
		String playerName = args[0];
		Player player = Server.getInstance().getPlayer(playerName);

		if (player == null) {
			sender.sendMessage("That Player is not online.");
			return true;
		}

		player.getInventory().clearAll();
		player.sendMessage("Inventory cleared!");

		sender.sendMessage(playerName + " inventory cleared!");
		return true;
	}
}
