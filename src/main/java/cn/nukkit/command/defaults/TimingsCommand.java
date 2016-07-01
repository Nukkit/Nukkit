package cn.nukkit.command.defaults;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.TimingsHandler;
import cn.nukkit.event.TranslationContainer;
import cn.nukkit.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by fromgate and Pub4Game on 30.06.2016.
 */
public class TimingsCommand extends VanillaCommand {

    public static long timingStart = 0;

    public TimingsCommand(String name) {
        super(name, "%nukkit.command.timings.description", "%nukkit.command.timings.usage");
        this.setPermission("nukkit.command.timings");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", usageMessage));
            return true;
        }
        String mode = args[0].toLowerCase();
        if (mode.equals("on")) {
            sender.getServer().getPluginManager().setUseTimings(true);
            TimingsHandler.reload();
            sender.sendMessage(new TranslationContainer("nukkit.command.timings.enable"));
            return true;
        } else if (mode.equals("off")) {
            sender.getServer().getPluginManager().setUseTimings(false);
            sender.sendMessage(new TranslationContainer("nukkit.command.timings.disable"));
            return true;
        }
        if (!sender.getServer().getPluginManager().useTimings()) {
            sender.sendMessage(new TranslationContainer("nukkit.command.timings.timingsDisabled"));
            return true;
        }
        boolean paste = mode.equals("paste");
        if (mode.equals("reset")) {
            TimingsHandler.reload();
            sender.sendMessage(new TranslationContainer("nukkit.command.timings.reset"));
        } else if (mode.equals("merged") || mode.equals("report") || paste) {
            long sampleTime = System.currentTimeMillis() / 1000 - timingStart;
            int index = 0;

            File timingFolder = new File(Server.getInstance().getDataPath()+File.separator+"timings");
            timingFolder.mkdirs();
            File timgins = new File(timingFolder+File.separator+"timings_"+String.format("%03d", index)+".txt");
            while (timgins.exists()) {
                index ++;
                timgins = new File(timingFolder+File.separator+"timings_"+String.format("%03d", index)+".txt");
            }
            List<String> timeStr = TimingsHandler.getTimings();

            timeStr.add("Sample time "+Math.round(sampleTime * 1000000000)+" ("+sampleTime+"s");
            StringBuilder sb = new StringBuilder();
            timeStr.forEach(s-> sb.append(s).append("\n"));
            try {
                Utils.writeFile(timgins, sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (paste) {

            }

        }
        return true;
    }
}

