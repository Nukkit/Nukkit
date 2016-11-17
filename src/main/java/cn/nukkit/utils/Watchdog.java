package cn.nukkit.utils;

import cn.nukkit.Server;

public class Watchdog extends Thread {
    private final Server server;
    private final long time;
    public boolean running = true;
    private boolean responding = true;

    public Watchdog(Server server, long time) {
        this.server = server;
        this.time = time;
        this.running = true;
    }

    public void kill() {
        running = false;
        synchronized (this) {
            this.notifyAll();
        }
    }

    @Override
    public void run() {
        while (this.running && server.isRunning()) {
            long current = server.getNextTick();
            if (current != 0) {
                long diff = System.currentTimeMillis() - current;
                if (diff > time) {
                    if (responding) {
                        this.server.getLogger().emergency("--------- Server stopped responding --------- (" + (diff / 1000d) + "s)");
                        StackTraceElement[] stack = server.getMainThread().getStackTrace();
                        for (StackTraceElement elem : stack) {
                            this.server.getLogger().emergency(elem.toString());
                        }
                        this.server.getLogger().emergency("---------------- Thread dump ----------------");
                        this.server.getLogger().alert(Utils.getAllThreadDumps());
                        this.server.getLogger().emergency("---------------------------------------------");
                        responding = false;
                    }
                } else {
                    responding = true;
                }
            }
            try {
                synchronized (this) {
                    this.wait(Math.max(time / 4, 1000));
                }
            } catch (InterruptedException ignore) {}
        }
    }
}
