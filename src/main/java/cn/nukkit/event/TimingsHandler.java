package cn.nukkit.event;

import cn.nukkit.Server;
import cn.nukkit.command.defaults.TimingsCommand;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.level.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fromgate on 30.06.2016.
 */
public class TimingsHandler {

    private static List<TimingsHandler> handlers = new ArrayList<>();
    private String name;
    private TimingsHandler parent;
    private int count;
    private int curCount;
    private long start;
    private int timingDepth;
    private long totalTime;
    private int curTickTotal;
    private int violations;

    public TimingsHandler(String name) {
        this(name, null);
    }

    public TimingsHandler(String name, TimingsHandler parent) {
        this.name = name;
        this.parent = parent;
        handlers.add(this);
    }

    public static List<String> getTimings() {
        List<String> strings = new ArrayList<>();
        strings.add("Minecraft");
        for (TimingsHandler timings : handlers) {
            long time = timings.totalTime;
            int count = timings.count;
            if (count == 0) continue;
            long avg = time / count;
            strings.add("   " + timings.name + " Time: " + Math.round(time * 1000000000) + " Count: "+count+" Avg: "+Math.round(avg * 1000000000)+" Violations: "+timings.violations);
            strings.add("# Version "+Server.getInstance().getVersion());
            strings.add("# "+Server.getInstance().getName()+" "+Server.getInstance().getNukkitVersion());
            int entities = 0;
            int livingEntities = 0;
            for (Level level : Server.getInstance().getLevels().values()) {
                entities += level.getEntities().length;
                for (Entity e : level.getEntities())
                    if (e instanceof EntityLiving) livingEntities++;
            }
            strings.add("# Entities "+ entities+"\n");
            strings.add("# LivingEntities " + livingEntities);
        }
        return strings;
    }

    public static void reload() {
        if (Server.getInstance().getPluginManager().useTimings()) {
            for (TimingsHandler timings : handlers)
                timings.reset();
        }
        TimingsCommand.timingStart = microtime();
    }

    public static void tick() {
        tick(true);
    }

    public static void tick(boolean measure) {
        if (Server.getInstance().getPluginManager().useTimings()) {
            if (measure) {
                handlers.forEach(timings -> {
                    if (timings.curTickTotal > 0.05)
                        timings.violations += Math.round(timings.curTickTotal / 0.05);
                    timings.curTickTotal = 0;
                    timings.curCount = 0;
                    timings.timingDepth = 0;
                });
            } else {
                handlers.forEach(timings -> {
                    timings.totalTime -= timings.curTickTotal;
                    timings.count -= timings.curCount;
                    timings.curTickTotal = 0;
                    timings.curCount = 0;
                    timings.timingDepth = 0;
                });
            }
        }
    }

    public static long microtime() {
        return System.currentTimeMillis() / 1000;
    }

    public void startTiming() {
        if (Server.getInstance().getPluginManager().useTimings() && (++this.timingDepth == 1)) {
            this.start = System.currentTimeMillis() / 1000;
            if (this.parent != null && (++this.parent.timingDepth == 1)) {
                this.parent.start = this.start;
            }
        }
    }

    public void stopTiming() {
        if (Server.getInstance().getPluginManager().useTimings()){
            if (--this.timingDepth != 0 || this.start == 0) return;
            long diff = microtime() - this.start;
            this.totalTime += diff;
            this.curTickTotal += diff;
            this.curCount++;
            this.count++;
            this.start = 0;
            if (this.parent != null) {
                this.parent.stopTiming();
            }
        }
    }
    public void reset() {
        this.count = 0;
        this.curCount = 0;
        this.violations = 0;
        this.curTickTotal = 0;
        this.start = 0;
        this.timingDepth = 0;
    }

    public void remove() {
        this.stopTiming();
        handlers.remove(this);
    }
}
