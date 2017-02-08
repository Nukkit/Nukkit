package cn.nukkit.command.defaults;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;

public class TransferServerCommand extends VanillaCommand {

    public TransferServerCommand(String name) {
        super(name, "%nukkit.command.transfer.description", "%nukkit.command.transfer.usage");
        this.setPermission("nukkit.command.transfer");
        this.commandParameters.clear();
        this.commandParameters.put("1arg", new CommandParameter[]{
                new CommandParameter("adress", CommandParameter.ARG_TYPE_STRING, false)
        });
        this.commandParameters.put("2args", new CommandParameter[]{
                new CommandParameter("port", CommandParameter.ARG_TYPE_SHORT, false),
        });
    }
    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            
            return false;
        }
        
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(TextFormat.RED + "A console can not be transferred!");
            
            return true;
        }
        
        /** @var String address
          * @var int port
          */
          
        if (args.length < 2) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            
            return false;
        }
        
        /** @var Player sender */
        int success = sender.transferTo(address, port);
        return success;
    }
}
