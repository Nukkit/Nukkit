package cn.nukkit.utils;

import cn.nukkit.Nukkit;
import cn.nukkit.command.CommandReader;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

/**
 * author: MagicDroidX
 * Nukkit
 */
public class MainLogger extends ThreadedLogger {

    protected final String logPath;
    protected final String[] logBuffer = new String[128];
    protected volatile int writeIndex = 0;
    protected volatile int readIndex = 0;
    protected boolean shutdown;
    protected boolean logDebug = false;
    private final Map<TextFormat, String> replacements = new EnumMap<>(TextFormat.class);
    private final TextFormat[] colors = TextFormat.values();

    protected static MainLogger logger;

    public MainLogger(String logFile) {
        this(logFile, false);
    }

    public MainLogger(String logFile, boolean logDebug) {
        if (logger != null) {
            throw new RuntimeException("MainLogger has been already created");
        }
        logger = this;
        this.logPath = logFile;
        this.logDebug = logDebug;
        this.start();
    }

    public static MainLogger getLogger() {
        return logger;
    }

    @Override
    public void emergency(String message) {
        this.send(TextFormat.RED + "[EMERGENCY] " + message);
    }

    @Override
    public void alert(String message) {
        this.send(TextFormat.RED + "[ALERT] " + message);
    }

    @Override
    public void critical(String message) {
        this.send(TextFormat.RED + "[CRITICAL] " + message);
    }

    @Override
    public void error(String message) {
        this.send(TextFormat.DARK_RED + "[ERROR] " + message);
    }

    @Override
    public void warning(String message) {
        this.send(TextFormat.YELLOW + "[WARNING] " + message);
    }

    @Override
    public void notice(String message) {
        this.send(TextFormat.AQUA + "[NOTICE] " + message);
    }

    @Override
    public void info(String message) {
        this.send(TextFormat.WHITE + "[INFO] " + message);
    }

    @Override
    public void debug(String message) {
        if (!this.logDebug) {
            return;
        }
        this.send(TextFormat.GRAY + "[DEBUG] " + message);
    }

    public void setLogDebug(Boolean logDebug) {
        this.logDebug = logDebug;
    }

    public void logException(Exception e) {
        this.alert(Utils.getExceptionMessage(e));
    }

    @Override
    public void log(LogLevel level, String message) {
        switch (level) {
            case EMERGENCY:
                this.emergency(message);
                break;
            case ALERT:
                this.alert(message);
                break;
            case CRITICAL:
                this.critical(message);
                break;
            case ERROR:
                this.error(message);
                break;
            case WARNING:
                this.warning(message);
                break;
            case NOTICE:
                this.notice(message);
                break;
            case INFO:
                this.info(message);
                break;
            case DEBUG:
                this.debug(message);
                break;
        }
    }

    public void shutdown() {
        this.shutdown = true;
    }

    protected void send(String message) {
        this.send(message, -1);
    }

    protected void send(String message, int level) {
        int index = writeIndex;
        writeIndex = (index + 1) % logBuffer.length;
        logBuffer[index] = message;
        synchronized (this) {
            this.notify();
        }
    }

    private String colorize(String string) {
        if (string.indexOf(TextFormat.ESCAPE) < 0) {
            return string;
        } else if (Nukkit.ANSI) {
            for (TextFormat color : colors) {
                if (replacements.containsKey(color)) {
                    string = string.replaceAll("(?i)" + color, replacements.get(color));
                } else {
                    string = string.replaceAll("(?i)" + color, "");
                }
            }
        } else {
            return TextFormat.clean(string);
        }
        return string + Ansi.ansi().reset();
    }


    @Override
    public void run() {
        AnsiConsole.systemInstall();
        File logFile = new File(logPath);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                this.logException(e);
            }
        } else {
            try {
                RandomAccessFile raf = new RandomAccessFile(logFile, "rw");
                raf.setLength(0);
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        replacements.put(TextFormat.BLACK, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString());
        replacements.put(TextFormat.DARK_BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString());
        replacements.put(TextFormat.DARK_GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString());
        replacements.put(TextFormat.DARK_AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());
        replacements.put(TextFormat.DARK_RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString());
        replacements.put(TextFormat.DARK_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString());
        replacements.put(TextFormat.GOLD, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString());
        replacements.put(TextFormat.GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString());
        replacements.put(TextFormat.DARK_GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString());
        replacements.put(TextFormat.BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString());
        replacements.put(TextFormat.GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString());
        replacements.put(TextFormat.AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString());
        replacements.put(TextFormat.RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString());
        replacements.put(TextFormat.LIGHT_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString());
        replacements.put(TextFormat.YELLOW, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString());
        replacements.put(TextFormat.WHITE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString());
        replacements.put(TextFormat.BOLD, Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString());
        replacements.put(TextFormat.STRIKETHROUGH, Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString());
        replacements.put(TextFormat.UNDERLINE, Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString());
        replacements.put(TextFormat.ITALIC, Ansi.ansi().a(Ansi.Attribute.ITALIC).toString());
        replacements.put(TextFormat.RESET, Ansi.ansi().a(Ansi.Attribute.RESET).toString());
        this.shutdown = false;
        do {
            if (readIndex == writeIndex) {
                try {
                    synchronized (this) {
                        wait(25000);
                    }
                } catch (InterruptedException ignore) {}
            }
            if (readIndex != writeIndex) {
                try {
                    OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(logFile, true), StandardCharsets.UTF_8);
                    Date now = new Date();
                    String dateFormat = new SimpleDateFormat("Y-M-d").format(now);
                    int count = 0;
                    while (readIndex != writeIndex && count++ < logBuffer.length) {
                        int index = readIndex;
                        readIndex = (index + 1) % logBuffer.length;
                        String message = logBuffer[index];
                        if (message != null) {
                            writer.write(dateFormat);
                            writer.write(TextFormat.clean(message));
                            writer.write("\r\n");
                            CommandReader.getInstance().stashLine();
                            System.out.println(colorize(TextFormat.AQUA + dateFormat + TextFormat.RESET + " " + message + TextFormat.RESET));
                            CommandReader.getInstance().unstashLine();
                            try {
                                Thread.sleep(5);
                            } catch (InterruptedException ignore) {}
                        }
                    }
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                    this.logException(e);
                }
            }
        } while (!this.shutdown);
    }

    @Override
    public void emergency(String message, Throwable t) {
        this.emergency(message + "\r\n" + Utils.getExceptionMessage(t));
    }

    @Override
    public void alert(String message, Throwable t) {
        this.alert(message + "\r\n" + Utils.getExceptionMessage(t));
    }

    @Override
    public void critical(String message, Throwable t) {
        this.critical(message + "\r\n" + Utils.getExceptionMessage(t));
    }

    @Override
    public void error(String message, Throwable t) {
        this.error(message + "\r\n" + Utils.getExceptionMessage(t));
    }

    @Override
    public void warning(String message, Throwable t) {
        this.warning(message + "\r\n" + Utils.getExceptionMessage(t));
    }

    @Override
    public void notice(String message, Throwable t) {
        this.notice(message + "\r\n" + Utils.getExceptionMessage(t));
    }

    @Override
    public void info(String message, Throwable t) {
        this.info(message + "\r\n" + Utils.getExceptionMessage(t));
    }

    @Override
    public void debug(String message, Throwable t) {
        this.debug(message + "\r\n" + Utils.getExceptionMessage(t));
    }

    @Override
    public void log(LogLevel level, String message, Throwable t) {
        this.log(level, message + "\r\n" + Utils.getExceptionMessage(t));
    }

}
