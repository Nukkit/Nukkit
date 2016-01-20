package cn.nukkit.command.defaults;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.TranslationContainer;

/**
 * Created on 2015/11/11 by xtypr.
 * Package cn.nukkit.command.defaults in project Nukkit .
 */
public class ListCommand extends VanillaCommand {

    public ListCommand(String name) {
        super(name, "%nukkit.command.list.description", "%commands.players.usage");
        this.setPermission("nukkit.command.list");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        final List<String> onlinePlayers = sender.getServer()
        		.getOnlinePlayers()
        		.values()
        		.stream()
        		.filter(Player::isOnline)
        		.filter(player -> !(sender instanceof Player) || ((Player) sender).canSee(player))
        		.map(Player::getName)
        		.collect(Collectors.toList());
        
        sender.sendMessage(new TranslationContainer("commands.players.list", new String[]{
        		String.valueOf(onlinePlayers.size()), String.valueOf(sender.getServer().getMaxPlayers())}));
        sender.sendMessage(Joiner.on(',').join(onlinePlayers));
        return true;
    }
}
