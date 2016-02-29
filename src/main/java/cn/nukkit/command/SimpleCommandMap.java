package cn.nukkit.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.nukkit.Server;
import cn.nukkit.command.defaults.BanCommand;
import cn.nukkit.command.defaults.BanIpCommand;
import cn.nukkit.command.defaults.BanListCommand;
import cn.nukkit.command.defaults.DefaultGamemodeCommand;
import cn.nukkit.command.defaults.DeopCommand;
import cn.nukkit.command.defaults.DifficultyCommand;
import cn.nukkit.command.defaults.EffectCommand;
import cn.nukkit.command.defaults.EnchantCommand;
import cn.nukkit.command.defaults.GamemodeCommand;
import cn.nukkit.command.defaults.GarbageCollectorCommand;
import cn.nukkit.command.defaults.GiveCommand;
import cn.nukkit.command.defaults.HelpCommand;
import cn.nukkit.command.defaults.KickCommand;
import cn.nukkit.command.defaults.KillCommand;
import cn.nukkit.command.defaults.ListCommand;
import cn.nukkit.command.defaults.MeCommand;
import cn.nukkit.command.defaults.OpCommand;
import cn.nukkit.command.defaults.PardonCommand;
import cn.nukkit.command.defaults.PardonIpCommand;
import cn.nukkit.command.defaults.ParticleCommand;
import cn.nukkit.command.defaults.PluginsCommand;
import cn.nukkit.command.defaults.ReloadCommand;
import cn.nukkit.command.defaults.SaveCommand;
import cn.nukkit.command.defaults.SaveOffCommand;
import cn.nukkit.command.defaults.SaveOnCommand;
import cn.nukkit.command.defaults.SayCommand;
import cn.nukkit.command.defaults.SeedCommand;
import cn.nukkit.command.defaults.SetWorldSpawnCommand;
import cn.nukkit.command.defaults.SpawnpointCommand;
import cn.nukkit.command.defaults.StatusCommand;
import cn.nukkit.command.defaults.StopCommand;
import cn.nukkit.command.defaults.TeleportCommand;
import cn.nukkit.command.defaults.TellCommand;
import cn.nukkit.command.defaults.TimeCommand;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.command.defaults.VersionCommand;
import cn.nukkit.command.defaults.WeatherCommand;
import cn.nukkit.command.defaults.WhitelistCommand;
import cn.nukkit.command.defaults.XpCommand;
import cn.nukkit.event.TranslationContainer;
import cn.nukkit.utils.LocalisedLogger;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Utils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class SimpleCommandMap implements CommandMap {
    protected Map<String, Command> knownCommands = new HashMap<>();

    private Server server;

    public SimpleCommandMap(Server server) {
        this.server = server;
        this.setDefaultCommands();
    }

    private void setDefaultCommands() {
        this.register("nukkit", new VersionCommand("version"));
        this.register("nukkit", new PluginsCommand("plugins"));
        this.register("nukkit", new SeedCommand("seed"));
        this.register("nukkit", new HelpCommand("help"));
        this.register("nukkit", new StopCommand("stop"));
        this.register("nukkit", new TellCommand("tell"));
        this.register("nukkit", new DefaultGamemodeCommand("defaultgamemode"));
        this.register("nukkit", new BanCommand("ban"));
        this.register("nukkit", new BanIpCommand("ban-ip"));
        this.register("nukkit", new BanListCommand("banlist"));
        this.register("nukkit", new PardonCommand("pardon"));
        this.register("nukkit", new PardonIpCommand("pardon-ip"));
        this.register("nukkit", new SayCommand("say"));
        this.register("nukkit", new MeCommand("me"));
        this.register("nukkit", new ListCommand("list"));
        this.register("nukkit", new DifficultyCommand("difficulty"));
        this.register("nukkit", new KickCommand("kick"));
        this.register("nukkit", new OpCommand("op"));
        this.register("nukkit", new DeopCommand("deop"));
        this.register("nukkit", new WhitelistCommand("whitelist"));
        this.register("nukkit", new SaveOnCommand("save-on"));
        this.register("nukkit", new SaveOffCommand("save-off"));
        this.register("nukkit", new SaveCommand("save-all"));
        this.register("nukkit", new GiveCommand("give"));
        this.register("nukkit", new EffectCommand("effect"));
        this.register("nukkit", new EnchantCommand("enchant"));
        this.register("nukkit", new ParticleCommand("particle"));
        this.register("nukkit", new GamemodeCommand("gamemode"));
        this.register("nukkit", new KillCommand("kill"));
        this.register("nukkit", new SpawnpointCommand("spawnpoint"));
        this.register("nukkit", new SetWorldSpawnCommand("setworldspawn"));
        this.register("nukkit", new TeleportCommand("tp"));
        this.register("nukkit", new TimeCommand("time"));
        //this.register("nukkit", new TimingsCommand("timings"));
        this.register("nukkit", new ReloadCommand("reload"));
        this.register("nukkit", new WeatherCommand("weather"));
        this.register("nukkit", new XpCommand("xp"));

        if ((boolean) this.server.getConfig("debug.commands", false)) {
            this.register("nukkit", new StatusCommand("status"));
            this.register("nukkit", new GarbageCollectorCommand("gc"));
            //this.register("nukkit", new DumpMemoryCommand("dumpmemory"));
        }
    }

    @Override
    public void registerAll(String fallbackPrefix, List<? extends Command> commands) {
        for (Command command : commands) {
            this.register(fallbackPrefix, command);
        }
    }

    @Override
    public boolean register(String fallbackPrefix, Command command) {
        return this.register(fallbackPrefix, command, null);
    }

    @Override
    public boolean register(String fallbackPrefix, Command command, String label) {
        if (label == null) {
            label = command.getName();
        }
        label = label.trim().toLowerCase();
        fallbackPrefix = fallbackPrefix.trim().toLowerCase();

        boolean registered = this.registerAlias(command, false, fallbackPrefix, label);

        List<String> aliases = new ArrayList<>(Arrays.asList(command.getAliases()));

        for (Iterator<String> iterator = aliases.iterator(); iterator.hasNext(); ) {
            String alias = iterator.next();
            if (!this.registerAlias(command, true, fallbackPrefix, alias)) {
                iterator.remove();
            }
        }
        command.setAliases(aliases.stream().toArray(String[]::new));

        if (!registered) {
            command.setLabel(fallbackPrefix + ":" + label);
        }

        command.register(this);

        return registered;
    }

    private boolean registerAlias(Command command, boolean isAlias, String fallbackPrefix, String label) {
        this.knownCommands.put(fallbackPrefix + ":" + label, command);
        if ((command instanceof VanillaCommand || isAlias) && this.knownCommands.containsKey(label)) {
            return false;
        }

        if (this.knownCommands.containsKey(label) && this.knownCommands.get(label).getLabel() != null && this.knownCommands.get(label).getLabel().equals(label)) {
            return false;
        }

        if (!isAlias) {
            command.setLabel(label);
        }

        this.knownCommands.put(label, command);

        return true;
    }

    @Override
    public boolean dispatch(CommandSender sender, String cmdLine) {
        String[] args = cmdLine.split(" ");

        if (args.length == 0) {
            return false;
        }

        String sentCommandLabel = args[0].toLowerCase();
        String[] newargs = new String[args.length - 1];
        System.arraycopy(args, 1, newargs, 0, newargs.length);
        args = newargs;
        Command target = this.getCommand(sentCommandLabel);

        if (target == null) {
            return false;
        }

        try {
            target.execute(sender, sentCommandLabel, args);
        } catch (Exception e) {
            sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.exception"));
            this.server.getLogger().criticalLocal("nukkit.command.exception", cmdLine, target.toString(), Utils.getExceptionMessage(e));
            LocalisedLogger logger = sender.getServer().getLogger();
            if (logger != null) {
                logger.logException(e);
            }
        }

        return true;
    }

    @Override
    public void clearCommands() {
        for (Command command : this.knownCommands.values()) {
            command.unregister(this);
        }
        this.knownCommands.clear();
        this.setDefaultCommands();
    }

    @Override
    public Command getCommand(String name) {
        if (this.knownCommands.containsKey(name)) {
            return this.knownCommands.get(name);
        }
        return null;
    }

    public Map<String, Command> getCommands() {
        return knownCommands;
    }

    public void registerServerAliases() {
        Map<String, List<String>> values = this.server.getCommandAliases();
        for (Map.Entry<String, List<String>> entry : values.entrySet()) {
            String alias = entry.getKey();
            List<String> commandStrings = entry.getValue();
            if (alias.contains(" ") || alias.contains(":")) {
                this.server.getLogger().warningLocal("nukkit.command.alias.illegal", alias);
                continue;
            }
            List<String> targets = new ArrayList<>();

            String bad = "";

            for (String commandString : commandStrings) {
                String[] args = commandString.split(" ");
                Command command = this.getCommand(args[0]);

                if (command == null) {
                    if (bad.length() > 0) {
                        bad += ", ";
                    }
                    bad += commandString;
                } else {
                    targets.add(commandString);
                }
            }

            if (bad.length() > 0) {
                this.server.getLogger().warningLocal("nukkit.command.alias.notFound", alias, bad);
                continue;
            }

            if (!targets.isEmpty()) {
                this.knownCommands.put(alias.toLowerCase(), new FormattedCommandAlias(alias.toLowerCase(), targets));
            } else {
                this.knownCommands.remove(alias.toLowerCase());
            }
        }
    }
}
