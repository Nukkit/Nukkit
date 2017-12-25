package cn.nukkit.command.defaults;


import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;

/**
 * author: akkz
 */
public class TransferServerCommand extends VanillaCommand {

    public TransferServerCommand(String name) {
        super(name, "%nukkit.command.transferserver.description", "%commands.transferserver.usage");
        this.setPermission("nukkit.command.transferserver");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("server addreee", CommandParameter.ARG_TYPE_STRING, false)});
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage("This command must be executed as a player");

            return false;
        }

        if (args.length == 0 || args.length > 2) {
            sender.sendMessage(new TranslationContainer("commands.transferserver.usage", this.usageMessage));
            return true;
        }

        int port = args.length == 2 ? Integer.parseInt(args[1]) : 19132;
        ((Player) sender).transfer(args[0], port);

        return true;
    }
}
