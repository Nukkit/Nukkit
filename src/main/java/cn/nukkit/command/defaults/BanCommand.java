package cn.nukkit.command.defaults;

import java.util.Arrays;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.TranslationContainer;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BanCommand extends VanillaCommand {

    public BanCommand(String name) {
        super(name, "%nukkit.command.ban.player.description", "%commands.ban.usage");
        this.setPermission("nukkit.command.ban.player");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));

            return false;
        }

        String name = args[0];
        StringBuilder builder = new StringBuilder();
        
        Arrays.stream(args).skip(1).forEach(arg -> {
        	builder.append(arg).append(" ");
        });

        if (builder.length() > 0) {
        	builder.substring(0, builder.length() - 1);
        }

        String reason = builder.toString();
        
        sender.getServer().getNameBans().addBan(name, reason, null, sender.getName());

        Player player = sender.getServer().getPlayerExact(name);
        
        if (player != null) {
            player.kick(reason.length() != 0 ? "Banned by admin. Reason: " + reason : "Banned by admin");
        }

        Command.broadcastCommandMessage(sender, new TranslationContainer("%commands.ban.success", player != null ? player.getName() : name));

        return true;
    }
}
