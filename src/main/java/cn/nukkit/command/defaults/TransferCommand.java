package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class TransferCommand extends VanillaCommand {

    public TransferCommand(String name) {
        super(name, "%nukkit.command.transfer.description", "%nukkit.command.transfer.usage");
        this.setPermission("nukkit.command.transfer");
        this.commandParameters.clear();
        this.commandParameters.put("1arg",
                new CommandParameter[]{
                        new CommandParameter("address", CommandParameter.ARG_TYPE_STRING, false),
                });
		this.commandParameters("2arg",
				new CommandParameter[]{
					new CommandParameter("port", CommandParameter.ARG_TYPE_INT, false)
				});
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            
            return false;
        }
        
		if(!(sender instanceof Player)){
			sender.sendMessage(TextFormat.RED + "You must run this command as player!");
		}
		
        if (args.length < 2) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            
            return false;
        }
		
		sender.transfer(address, port);
		
		return true;
    }
}
