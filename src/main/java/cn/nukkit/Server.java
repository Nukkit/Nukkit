package cn.nukkit;

import cn.nukkit.block.Block;
import cn.nukkit.blockentity.*;
import cn.nukkit.command.*;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.entity.item.*;
import cn.nukkit.entity.mob.EntityCreeper;
import cn.nukkit.entity.passive.*;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntitySnowball;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.level.LevelInitEvent;
import cn.nukkit.event.level.LevelLoadEvent;
import cn.nukkit.event.player.PlayerKickEvent;
import cn.nukkit.event.server.QueryRegenerateEvent;
import cn.nukkit.inventory.*;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.lang.BaseLang;
import cn.nukkit.lang.TextContainer;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.LevelProvider;
import cn.nukkit.level.format.LevelProviderManager;
import cn.nukkit.level.format.anvil.Anvil;
import cn.nukkit.level.format.leveldb.LevelDB;
import cn.nukkit.level.format.mcregion.McRegion;
import cn.nukkit.level.generator.Flat;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.level.generator.Normal;
import cn.nukkit.level.generator.biome.Biome;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.metadata.EntityMetadataStore;
import cn.nukkit.metadata.LevelMetadataStore;
import cn.nukkit.metadata.PlayerMetadataStore;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.CompressBatchedTask;
import cn.nukkit.network.Network;
import cn.nukkit.network.RakNetInterface;
import cn.nukkit.network.SourceInterface;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.CraftingDataPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.PlayerListPacket;
import cn.nukkit.network.query.QueryHandler;
import cn.nukkit.network.rcon.RCON;
import cn.nukkit.permission.BanEntry;
import cn.nukkit.permission.BanList;
import cn.nukkit.permission.DefaultPermissions;
import cn.nukkit.permission.Permissible;
import cn.nukkit.plugin.JavaPluginLoader;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginLoadOrder;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.potion.Effect;
import cn.nukkit.potion.Potion;
import cn.nukkit.scheduler.FileWriteTask;
import cn.nukkit.scheduler.ServerScheduler;
import cn.nukkit.timings.Timings;
import cn.nukkit.utils.*;

import java.io.*;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * @author MagicDroidX
 * @author Box
 */
public class Server {

    public static final String BROADCAST_CHANNEL_ADMINISTRATIVE = "nukkit.broadcast.admin";
    public static final String BROADCAST_CHANNEL_USERS = "nukkit.broadcast.user";

    private static Server instance = null;

    private volatile Thread mainThread;

    private volatile BanList banByName;

    private volatile BanList banByIP;

    private volatile Config operators;

    private volatile Config whitelist;

    private volatile boolean isRunning = true;

    private volatile boolean hasStopped = false;

    private volatile PluginManager pluginManager;

    private int profilingTickrate = 20;

    private final Object schedulerLock = new Object();
    private volatile ServerScheduler scheduler;

    private volatile int tickCounter;

    private volatile long nextTick;

    private final float[] tickAverage = {20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20};

    private final float[] useAverage = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private volatile float maxTick = 20;

    private volatile float maxUse = 0;

    private volatile int sendUsageTicker = 0;

    private volatile boolean dispatchSignals = false;

    private final MainLogger logger;

    private final Object commandMapLock = new Object();
    private volatile SimpleCommandMap commandMap;

    private volatile CraftingManager craftingManager;

    private final Object consoleSenderLock = new Object();
    private volatile ConsoleCommandSender consoleSender;

    private volatile int maxPlayers;

    private volatile boolean autoSave;

    private volatile RCON rcon;

    private volatile EntityMetadataStore entityMetadata;

    private volatile PlayerMetadataStore playerMetadata;

    private volatile LevelMetadataStore levelMetadata;

    private final Object networkLock = new Object();
    private volatile Network network;

    private volatile boolean networkCompressionAsync = true;
    public volatile int networkCompressionLevel = 7;
    public volatile int networkCompressionStrategy = 2;

    private volatile boolean autoTickRate = true;
    private volatile int autoTickRateLimit = 20;
    private volatile boolean alwaysTickPlayers = false;
    private volatile int baseTickRate = 1;
    private volatile Boolean getAllowFlight = null;
    public volatile boolean storeGeneratedChunks = false;

    private int autoSaveTicker = 0;
    private int autoSaveTicks = 6000;

    private final Object baseLangLock = new Object();
    private volatile BaseLang baseLang;

    private volatile boolean forceLanguage = false;

    private volatile UUID serverID;

    private final String filePath;
    private final String dataPath;
    private final String pluginPath;

    private final Set<UUID> uniquePlayers = new HashSet<>(); // Unused

    private volatile QueryHandler queryHandler;

    private volatile QueryRegenerateEvent queryRegenerateEvent;

    private final ForkJoinPool executor;

    private final Object propertiesLock = new Object();
    private volatile Config properties;
    private final Object configLock = new Object();
    private volatile Config config;

    private final Map<String, Player> players = new ConcurrentHashMap<>(8, 0.9f, 1);

    private final Map<UUID, Player> playerList = new ConcurrentHashMap<>(8, 0.9f, 1);

    private final Map<Integer, String> identifier = new ConcurrentHashMap<>(8, 0.9f, 1);

    private final Map<Integer, Level> levels = new ConcurrentHashMap<>(8, 0.9f, 1);

    private volatile Level defaultLevel = null;

    public Server(MainLogger logger, final String filePath, final String dataPath, final String pluginPath) {
        
        instance = this;
        this.logger = logger;
        this.mainThread = Thread.currentThread();
        this.filePath = filePath;
        this.executor = new ForkJoinPool();
        { // Initialize some classes
            this.executor.submit(new Runnable() {
                @Override
                public void run() {
                    Block.init();
                    Item.init();
                }
            });
            this.executor.submit(new Runnable() {
                @Override
                public void run() {
                    craftingManager = new CraftingManager();
                    craftingManager.init();
                }
            });
            this.executor.submit(new Runnable() {
                @Override
                public void run() {
                    registerEntities();
                }
            });

            this.executor.submit(new Runnable() {
                @Override
                public void run() {
                    Biome.init();
                }
            });
            this.executor.submit(new Runnable() {
                @Override
                public void run() {
                    registerBlockEntities();
                    Effect.init();
                    Potion.init();
                    Enchantment.init();
                    Attribute.init();
                }
            });
        }
        // Ban list
        this.executor.submit(new Runnable() {
            @Override
            public void run() {
                banByName = new BanList(dataPath + "banned-players.json");
                banByIP = new BanList(dataPath + "banned-ips.json");
            }
        });
        // Operators + Whitelist
        this.executor.submit(new Runnable() {
            @Override
            public void run() {
                operators = new Config(dataPath + "ops.txt", Config.ENUM);
                whitelist = new Config(dataPath + "white-list.txt", Config.ENUM);
            }
        });
        // Create folders
        this.executor.submit(new Runnable() {
            @Override
            public void run() {
                if (!Files.exists(Paths.get("worlds/"))) {
                    new File(dataPath + "worlds/").mkdirs();
                }
                if (!Files.exists(Paths.get("players/"))) {
                    new File(dataPath + "players/").mkdirs();
                }
                if (!Files.exists(Paths.get(pluginPath))) {
                    new File(pluginPath).mkdirs();
                }
            }
        });
        // Metadata store
        this.executor.submit(new Runnable() {
            @Override
            public void run() {
                entityMetadata = new EntityMetadataStore();
                playerMetadata = new PlayerMetadataStore();
                levelMetadata = new LevelMetadataStore();
            }
        });
        // Paths
        this.dataPath = Paths.get(dataPath).toAbsolutePath() + "/";
        this.pluginPath = Paths.get(pluginPath).toAbsolutePath() + "/";
        if (!Files.exists(Paths.get("nukkit.yml"))) {
            this.getLogger().info(TextFormat.GREEN + "Welcome! Please choose a language first!");
            try {
                String[] lines = Utils.readFile(this.getClass().getClassLoader().getResourceAsStream("lang/language.list")).split("\n");
                for (String line : lines) {
                    this.getLogger().info(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String fallback = BaseLang.FALLBACK_LANGUAGE;
            String language = null;
            while (language == null) {
                String lang = CommandReader.getInstance().readLine();
                InputStream conf = this.getClass().getClassLoader().getResourceAsStream("lang/" + lang + "/lang.ini");
                if (conf != null) {
                    language = lang;
                }
            }
            InputStream advacedConf = this.getClass().getClassLoader().getResourceAsStream("lang/" + language + "/nukkit.yml");
            if (advacedConf == null) {
                advacedConf = this.getClass().getClassLoader().getResourceAsStream("lang/" + fallback + "/nukkit.yml");
            }
            try {
                Utils.writeFile(this.dataPath + "nukkit.yml", advacedConf);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            CommandReader.getInstance().start();
        } else {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    CommandReader.getInstance().start();
                }
            });
        }
        // rcon
        this.executor.submit(new Runnable() {
            @Override
            public void run() {
                if (getPropertyBoolean("enable-rcon", false)) {
                    rcon = new RCON(Server.this, getPropertyString("rcon.password", ""), (!getIp().equals("")) ? getIp() : "0.0.0.0", getPropertyInt("rcon.port", getPort()));
                }
            }
        });
        // Some config options
        this.executor.submit(new Runnable() {
            @Override
            public void run() {
                logger.info(getLanguage().translateString("language.selected", new String[]{getLanguage().getName(), getLanguage().getLang()}));
                logger.info(getLanguage().translateString("nukkit.server.start", TextFormat.AQUA + getVersion() + TextFormat.WHITE));
                Object poolSize = getConfig("settings.async-workers", "auto");
                if (!(poolSize instanceof Integer)) {
                    try {
                        poolSize = Integer.valueOf((String) poolSize);
                    } catch (Exception e) {
                        poolSize = Math.max(Runtime.getRuntime().availableProcessors() + 1, 4);
                    }
                }
                ServerScheduler.WORKERS = (int) poolSize;
            }
        });
        // Network settings
        this.executor.submit(new Runnable() {
            @Override
            public void run() {
                int threshold;
                try {
                    threshold = Integer.valueOf(String.valueOf(getConfig("network.batch-threshold", 256)));
                } catch (Exception e) {
                    threshold = 256;
                }

                if (threshold < 0) {
                    threshold = -1;
                }
                Network.BATCH_THRESHOLD = threshold;
                networkCompressionLevel = (int) getConfig("network.compression-level", 7);
                networkCompressionAsync = (boolean) getConfig("network.async-compression", true);

                networkCompressionLevel = (int) getConfig("network.compression-level", 7);
                networkCompressionAsync = (boolean) getConfig("network.async-compression", true);

                autoTickRate = (boolean) getConfig("level-settings.auto-tick-rate", true);
                autoTickRateLimit = (int) getConfig("level-settings.auto-tick-rate-limit", 20);
                alwaysTickPlayers = (boolean) getConfig("level-settings.always-tick-players", false);
                baseTickRate = (int) getConfig("level-settings.base-tick-rate", 1);

                if ((int) getConfig("ticks-per.autosave", 6000) > 0) {
                    autoSaveTicks = (int) getConfig("ticks-per.autosave", 6000);
                }
            }
        });
        // Some config options
        this.executor.submit(new Runnable() {
            @Override
            public void run() {
                storeGeneratedChunks = (boolean) getConfig("chunk-generation.save-newly-generated", true);
                maxPlayers = getPropertyInt("max-players", 20);
                setAutoSave(getPropertyBoolean("auto-save", true));

                if (getPropertyBoolean("hardcore", false) && getDifficulty() < 3) {
                    setPropertyInt("difficulty", 3);
                }
                Nukkit.DEBUG = (int) getConfig("debug.level", 1);
                if (logger instanceof MainLogger) {
                    logger.setLogDebug(Nukkit.DEBUG > 1);
                }

                logger.info(getLanguage().translateString("nukkit.server.networkStart", new String[]{getIp().equals("") ? "*" : getIp(), String.valueOf(getPort())}));
                serverID = UUID.randomUUID();

                logger.info(getLanguage().translateString("nukkit.server.info", new String[]{getName(), TextFormat.YELLOW + getNukkitVersion() + TextFormat.WHITE, TextFormat.AQUA + getCodename() + TextFormat.WHITE, getApiVersion()}));
                logger.info(getLanguage().translateString("nukkit.server.license", getName()));
            }
        });
        // Init plugins, then do some more stuff in parallel
        Runnable initPlugins = new Runnable() {
            @Override
            public void run() {
                pluginManager = new PluginManager(Server.this, getCommandMap());
                pluginManager.registerInterface(JavaPluginLoader.class);
                pluginManager.loadPlugins(pluginPath);
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        getCommandMap().setDefaultCommands();
                    }
                });
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        pluginManager.subscribeToPermission(Server.BROADCAST_CHANNEL_ADMINISTRATIVE, getConsoleSender());
                    }
                });
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        queryRegenerateEvent = new QueryRegenerateEvent(Server.this, 5);
                        getNetwork().registerInterface(new RakNetInterface(Server.this));
                    }
                });
                if (hasPlugin(PluginLoadOrder.STARTUP)) {
                    executor.awaitQuiescence(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
                    enablePlugins(PluginLoadOrder.STARTUP);
                }
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        getCommandMap().registerServerAliases();
                    }
                });
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        DefaultPermissions.registerCorePermissions();
                    }
                });
                // Load levels
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        LevelProviderManager.addProvider(Server.this, Anvil.class);
                        LevelProviderManager.addProvider(Server.this, McRegion.class);
                        LevelProviderManager.addProvider(Server.this, LevelDB.class);

                        Generator.addGenerator(Flat.class, "flat", Generator.TYPE_FLAT);
                        Generator.addGenerator(Normal.class, "normal", Generator.TYPE_INFINITE);
                        Generator.addGenerator(Normal.class, "default", Generator.TYPE_INFINITE);
                        //todo: add old generator and hell generator

                        for (String name : ((Map<String, Object>) getConfig("worlds", new HashMap<>())).keySet()) {
                            executor.submit(new Runnable() {
                                @Override
                                public void run() {
                                    if (!loadLevel(name)) {
                                        long seed;
                                        try {
                                            seed = ((Integer) getConfig("worlds." + name + ".seed")).longValue();
                                        } catch (Exception e) {
                                            seed = System.currentTimeMillis();
                                        }

                                        Map<String, Object> options = new HashMap<>();
                                        String[] opts = ((String) getConfig("worlds." + name + ".generator", Generator.getGenerator("default").getSimpleName())).split(":");
                                        Class<? extends Generator> generator = Generator.getGenerator(opts[0]);
                                        if (opts.length > 1) {
                                            String preset = "";
                                            for (int i = 1; i < opts.length; i++) {
                                                preset += opts[i] + ":";
                                            }
                                            preset = preset.substring(0, preset.length() - 1);

                                            options.put("preset", preset);
                                        }
                                        generateLevel(name, seed, generator, options);
                                    }
                                }
                            });
                        }
                        executor.submit(new Runnable() {
                            @Override
                            public void run() {
                                if (getDefaultLevel() == null) {
                                    String defaultName = getPropertyString("level-name", "world");
                                    if (defaultName == null || "".equals(defaultName.trim())) {
                                        getLogger().warning("level-name cannot be null, using default");
                                        defaultName = "world";
                                        setPropertyString("level-name", defaultName);
                                    }

                                    if (!loadLevel(defaultName)) {
                                        long seed;
                                        String seedString = String.valueOf(getProperty("level-seed", System.currentTimeMillis()));
                                        try {
                                            seed = Long.valueOf(seedString);
                                        } catch (NumberFormatException e) {
                                            seed = seedString.hashCode();
                                        }
                                        generateLevel(defaultName, seed == 0 ? System.currentTimeMillis() : seed);
                                    }
                                    setDefaultLevel(getLevelByName(defaultName));
                                }
                                properties.save(true);

                                if (getDefaultLevel() == null) {
                                    getLogger().emergency(getLanguage().translateString("nukkit.level.defaultError"));
                                    forceShutdown();
                                    return;
                                }
                            }
                        });
                    }
                });
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        if (getPropertyBoolean("enable-query", true)) {
                            queryHandler = new QueryHandler();
                        }
                    }
                });
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        for (BanEntry entry : getIPBans().getEntires().values()) {
                            getNetwork().blockAddress(entry.getName(), -1);
                        }
                    }
                });
            }
        };
        Thread initPluginsThread = new Thread(initPlugins);
        initPluginsThread.start();
        
        this.executor.awaitQuiescence(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        
        try {
            initPluginsThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        this.executor.awaitQuiescence(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        
        if (this.hasStopped) {
            return;
        }
        
        this.enablePlugins(PluginLoadOrder.POSTWORLD);
        
        this.start();
    }

    public final Thread getMainThread() {
        return mainThread;
    }

    public int broadcastMessage(String message) {
        return this.broadcast(message, BROADCAST_CHANNEL_USERS);
    }

    public int broadcastMessage(TextContainer message) {
        return this.broadcast(message, BROADCAST_CHANNEL_USERS);
    }

    public int broadcastMessage(String message, CommandSender[] recipients) {
        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.length;
    }

    public int broadcastMessage(String message, Collection<CommandSender> recipients) {
        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.size();
    }

    public int broadcastMessage(TextContainer message, Collection<CommandSender> recipients) {
        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.size();
    }

    public int broadcast(String message, String permissions) {
        Set<CommandSender> recipients = new HashSet<>();

        for (String permission : permissions.split(";")) {
            for (Permissible permissible : this.pluginManager.getPermissionSubscriptions(permission)) {
                if (permissible instanceof CommandSender && permissible.hasPermission(permission)) {
                    recipients.add((CommandSender) permissible);
                }
            }
        }

        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.size();
    }

    public int broadcast(TextContainer message, String permissions) {
        Set<CommandSender> recipients = new HashSet<>();

        for (String permission : permissions.split(";")) {
            for (Permissible permissible : this.pluginManager.getPermissionSubscriptions(permission)) {
                if (permissible instanceof CommandSender && permissible.hasPermission(permission)) {
                    recipients.add((CommandSender) permissible);
                }
            }
        }

        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.size();
    }


    public static void broadcastPacket(Collection<Player> players, DataPacket packet) {
        broadcastPacket(players.stream().toArray(Player[]::new), packet);
    }

    public static void broadcastPacket(Player[] players, DataPacket packet) {
        packet.encode();
        packet.isEncoded = true;
        if (Network.BATCH_THRESHOLD >= 0 && packet.getBuffer().length >= Network.BATCH_THRESHOLD) {
            Server.getInstance().batchPackets(players, new DataPacket[]{packet}, false);
            return;
        }

        for (Player player : players) {
            player.dataPacket(packet);
        }

        if (packet.encapsulatedPacket != null) {
            packet.encapsulatedPacket = null;
        }
    }

    public void batchPackets(Player[] players, DataPacket[] packets) {
        this.batchPackets(players, packets, false);
    }

    public void batchPackets(Player[] players, DataPacket[] packets, boolean forceSync) {
        if (players == null || packets == null || players.length == 0 || packets.length == 0) {
            return;
        }
        Timings.playerNetworkSendTimer.startTiming();
        if (!forceSync && this.networkCompressionAsync) {
            byte[] data = DataPacket.join(packets);
            List<String> targets = new ArrayList<>();
            for (Player p : players) {
                if (p.isConnected()) {
                    targets.add(this.identifier.get(p.rawHashCode()));
                }
            }
            this.getScheduler().scheduleAsyncTask(new CompressBatchedTask(data, targets, this.networkCompressionLevel));
        } else {
            ForkJoinPool pool = new ForkJoinPool();

            int parallelism = Math.min(packets.length, pool.getParallelism());
            int chunkSize = (packets.length + parallelism - 1) / parallelism;
            int chunks = (packets.length + chunkSize - 1) / chunkSize;

            for (int index = 0, offset = 0; index < chunks; index++, offset += chunkSize) {
                final DataPacket[] range = Arrays.copyOfRange(packets, offset, Math.min(packets.length, offset + chunkSize));
                pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BatchPacket bpk = BatchPacket.compressPackets(range, networkCompressionLevel, networkCompressionStrategy);
                            for (final Player p : players) {
                                p.dataPacket(bpk);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }

            pool.awaitQuiescence(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            pool.shutdownNow();
        }
        Timings.playerNetworkSendTimer.stopTiming();
    }

    public void broadcastPacketsCallback(byte[] data, List<String> identifiers) {
        BatchPacket pk = new BatchPacket();
        pk.payload = data;
        pk.encode();
        pk.isEncoded = true;

        for (String i : identifiers) {
            Player player = this.players.get(i);
            if (player != null) {
                player.dataPacket(pk);
            }
        }
    }

    public boolean hasPlugin(PluginLoadOrder type) {
        for (Map.Entry<String, Plugin> entry : this.pluginManager.getPlugins().entrySet()) {
            Plugin plugin = entry.getValue();
            if (!plugin.isEnabled() && type == plugin.getDescription().getOrder()) {
                return true;
            }
        }
        return false;
    }

    public void enablePlugins(PluginLoadOrder type) {
        for (Map.Entry<String, Plugin> entry : this.pluginManager.getPlugins().entrySet()) {
            Plugin plugin = entry.getValue();
            if (!plugin.isEnabled() && type == plugin.getDescription().getOrder()) {
                this.enablePlugin(plugin);
            }
        }
    }

    public void enablePlugin(Plugin plugin) {
        this.pluginManager.enablePlugin(plugin);
    }

    public void disablePlugins() {
        this.pluginManager.disablePlugins();
    }

    public boolean dispatchCommand(CommandSender sender, String commandLine) throws ServerException {
        if (sender == null) {
            throw new ServerException("CommandSender is not valid");
        }

        if (this.commandMap.dispatch(sender, commandLine)) {
            return true;
        }

        sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.notFound"));

        return false;
    }

    //todo: use ticker to check console
    public ConsoleCommandSender getConsoleSender() {
        if (this.consoleSender == null) {
            synchronized (consoleSenderLock) {
                if (this.consoleSender == null) {
                    this.consoleSender = new ConsoleCommandSender();
                }
            }
        }
        return consoleSender;
    }

    public void reload() {
        this.logger.info("Reloading...");

        this.logger.info("Saving levels...");

        for (Level level : this.levels.values()) {
            level.save();
        }

        this.pluginManager.disablePlugins();
        this.pluginManager.clearPlugins();
        this.commandMap.clearCommands();

        this.logger.info("Reloading properties...");
        getProperties().reload();
        this.maxPlayers = this.getPropertyInt("max-players", 20);

        if (this.getPropertyBoolean("hardcore", false) && this.getDifficulty() < 3) {
            this.setPropertyInt("difficulty", 3);
        }

        this.banByIP.load();
        this.banByName.load();
        this.reloadWhitelist();
        this.operators.reload();

        for (BanEntry entry : this.getIPBans().getEntires().values()) {
            this.getNetwork().blockAddress(entry.getName(), -1);
        }

        this.pluginManager.registerInterface(JavaPluginLoader.class);
        this.pluginManager.loadPlugins(this.pluginPath);
        this.enablePlugins(PluginLoadOrder.STARTUP);
        this.enablePlugins(PluginLoadOrder.POSTWORLD);
        Timings.reset();
    }

    public void shutdown() {
        if (this.isRunning) {
            ServerKiller killer = new ServerKiller(90);
            killer.start();
        }
        this.isRunning = false;
    }

    public void forceShutdown() {
        if (this.hasStopped) {
            return;
        }

        try {
            if (!this.isRunning) {
                //todo sendUsage
            }

            // clean shutdown of console thread asap
            CommandReader.getInstance().shutdown();

            this.hasStopped = true;

            this.shutdown();

            if (this.rcon != null) {
                this.rcon.close();
            }

            this.getLogger().debug("Disabling all plugins");
            this.pluginManager.disablePlugins();

            for (Player player : new ArrayList<>(this.players.values())) {
                player.close(player.getLeaveMessage(), (String) this.getConfig("settings.shutdown-message", "Server closed"));
            }

            this.getLogger().debug("Unloading all levels");
            for (Level level : new ArrayList<>(this.getLevels().values())) {
                this.unloadLevel(level, true);
            }

            this.getLogger().debug("Removing event handlers");
            HandlerList.unregisterAll();

            this.getLogger().debug("Stopping all tasks");
            getScheduler().cancelAllTasks();
            getScheduler().mainThreadHeartbeat(Integer.MAX_VALUE);
            this.getLogger().debug("Closing console");
            CommandReader.getInstance().interrupt();

            this.getLogger().debug("Stopping network interfaces");
            for (SourceInterface interfaz : getNetwork().getInterfaces()) {
                interfaz.shutdown();
                getNetwork().unregisterInterface(interfaz);
            }

            this.getLogger().debug("Disabling timings");
            Timings.stopServer();
            //todo other things
        } catch (Exception e) {
            this.logger.logException(e); //todo remove this?
            this.logger.emergency("Exception happened while shutting down, exit the process");
            System.exit(1);
        }
    }

    public void start() {
        //todo send usage setting

        this.tickCounter = 0;

        this.logger.info(this.getLanguage().translateString("nukkit.server.defaultGameMode", getGamemodeString(this.getGamemode())));

        
        this.logger.info(this.getLanguage().translateString("nukkit.server.startFinished", String.valueOf((double) (System.currentTimeMillis() - Nukkit.START_TIME) / 1000)));

        this.tickProcessor();
        this.forceShutdown();
    }

    public void handlePacket(String address, int port, byte[] payload) {
        try {
            if (payload.length > 2 && Arrays.equals(Binary.subBytes(payload, 0, 2), new byte[]{(byte) 0xfe, (byte) 0xfd}) && this.queryHandler != null) {
                this.queryHandler.handle(address, port, payload);
            }
        } catch (Exception e) {
            this.logger.logException(e);

            this.getNetwork().blockAddress(address, 600);
        }
    }

    public void tickProcessor() {
        this.nextTick = System.currentTimeMillis();
        try {
            while (this.isRunning) {
                try {
                    this.tick();
                } catch (RuntimeException e) {
                    this.getLogger().logException(e);
                }

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Server.getInstance().getLogger().logException(e);
                }
            }
        } catch (Throwable e) {
            this.logger.emergency("Exception happened while ticking server");
            this.logger.alert(Utils.getExceptionMessage(e));
            this.logger.alert(Utils.getAllThreadDumps());
        }
    }

    public void onPlayerLogin(Player player) {
        if (this.sendUsageTicker > 0) {
            this.uniquePlayers.add(player.getUniqueId());
        }
    }

    public void addPlayer(String identifier, Player player) {
        this.players.put(identifier, player);
        this.identifier.put(player.rawHashCode(), identifier);
    }

    public void addOnlinePlayer(Player player) {
        this.playerList.put(player.getUniqueId(), player);
        this.updatePlayerListData(player.getUniqueId(), player.getId(), player.getDisplayName(), player.getSkin());
    }

    public void removeOnlinePlayer(Player player) {
        if (this.playerList.remove(player.getUniqueId()) != null) {
            PlayerListPacket pk = new PlayerListPacket();
            pk.type = PlayerListPacket.TYPE_REMOVE;
            pk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(player.getUniqueId())};

            Server.broadcastPacket(this.playerList.values(), pk);
        }
    }

    public void updatePlayerListData(UUID uuid, long entityId, String name, Skin skin) {
        this.updatePlayerListData(uuid, entityId, name, skin, this.playerList.values());
    }

    public void updatePlayerListData(UUID uuid, long entityId, String name, Skin skin, Player[] players) {
        PlayerListPacket pk = new PlayerListPacket();
        pk.type = PlayerListPacket.TYPE_ADD;
        pk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid, entityId, name, skin)};
        Server.broadcastPacket(players, pk);
    }

    public void updatePlayerListData(UUID uuid, long entityId, String name, Skin skin, Collection<Player> players) {
        this.updatePlayerListData(uuid, entityId, name, skin, players.stream().toArray(Player[]::new));
    }

    public void removePlayerListData(UUID uuid) {
        this.removePlayerListData(uuid, this.playerList.values());
    }

    public void removePlayerListData(UUID uuid, Player[] players) {
        PlayerListPacket pk = new PlayerListPacket();
        pk.type = PlayerListPacket.TYPE_REMOVE;
        pk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid)};
        Server.broadcastPacket(players, pk);
    }

    public void removePlayerListData(UUID uuid, Collection<Player> players) {
        this.removePlayerListData(uuid, players.stream().toArray(Player[]::new));
    }

    public void sendFullPlayerListData(Player player) {
        PlayerListPacket pk = new PlayerListPacket();
        pk.type = PlayerListPacket.TYPE_ADD;
        pk.entries = this.playerList
                .values()
                .stream()
                .map(p -> new PlayerListPacket.Entry(
                        p.getUniqueId(),
                        p.getId(),
                        p.getDisplayName(),
                        p.getSkin()))
                .toArray(PlayerListPacket.Entry[]::new);

        player.dataPacket(pk);
    }

    public void sendRecipeList(Player player) {
        CraftingDataPacket pk = new CraftingDataPacket();
        pk.cleanRecipes = true;

        for (Recipe recipe : this.getCraftingManager().getRecipes().values()) {
            if (recipe instanceof ShapedRecipe) {
                pk.addShapedRecipe((ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                pk.addShapelessRecipe((ShapelessRecipe) recipe);
            }
        }

        for (FurnaceRecipe recipe : this.getCraftingManager().getFurnaceRecipes().values()) {
            pk.addFurnaceRecipe(recipe);
        }

        player.dataPacket(pk);
    }

    private void checkTickUpdates(int currentTick, long tickTime) {
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            Player p = entry.getValue();
            if (!p.loggedIn && (tickTime - p.creationTime) >= 10000 && p.kick(PlayerKickEvent.Reason.LOGIN_TIMOUT)) {
                continue;
            }
            if (this.alwaysTickPlayers) {
                p.onUpdate(currentTick);
            }
        }
        //Do level ticks
        ArrayList<Level> levelsToTick = new ArrayList(getLevels().values());
        for (Level level : levelsToTick) {
            level.timings.doTick.startTiming();
        }
        for (Level level : levelsToTick) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    if (level.getTickRate() > baseTickRate && level.tickRateCounter.decrementAndGet() > 0) {
                        return;
                    }
                    try {
                        long levelTime = System.currentTimeMillis();
                        level.doTick(currentTick, executor);
                        int tickMs = (int) (System.currentTimeMillis() - levelTime);
                        level.tickRateTime.set(tickMs);
                        if (autoTickRate) {
                            if (tickMs < 50 && level.getTickRate() > baseTickRate) {
                                int r;
                                level.setTickRate(r = level.getTickRate() - 1);
                                if (r > baseTickRate) {
                                    level.tickRateCounter.set(level.getTickRate());
                                }
                                getLogger().debug("Raising level \"" + level.getName() + "\" tick rate to " + level.getTickRate() + " ticks");
                            } else if (tickMs >= 50) {
                                if (level.getTickRate() == baseTickRate) {
                                    level.setTickRate((int) Math.max(baseTickRate + 1, Math.min(autoTickRateLimit, Math.floor(tickMs / 50))));
                                    getLogger().debug("Level \"" + level.getName() + "\" took " + NukkitMath.round(tickMs, 2) + "ms, setting tick rate to " + level.getTickRate() + " ticks");
                                } else if ((tickMs / level.getTickRate()) >= 50 && level.getTickRate() < autoTickRateLimit) {
                                    level.setTickRate(level.getTickRate() + 1);
                                    getLogger().debug("Level \"" + level.getName() + "\" took " + NukkitMath.round(tickMs, 2) + "ms, setting tick rate to " + level.getTickRate() + " ticks");
                                }
                                level.tickRateCounter.set(level.getTickRate());
                            }
                        }
                    } catch (Exception e) {
                        if (Nukkit.DEBUG > 1 && logger != null) {
                            logger.logException(e);
                        }
                        logger.critical(getLanguage().translateString("nukkit.level.tickError", new String[]{level.getName(), e.toString()}));
                    }
                }
            });
        }
        executor.awaitQuiescence(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        for (Level level : levelsToTick) {
            level.timings.doTick.stopTiming();
        }
    }

    public void doAutoSave() {
        if (this.getAutoSave()) {
            Timings.levelSaveTimer.startTiming();
            for (Map.Entry<String, Player> entry : this.players.entrySet()) {
                Player player = entry.getValue();
                if (player.isOnline()) {
                    player.save(true);
                } else if (!player.isConnected()) {
                    this.removePlayer(player);
                }
            }
            for (Level level : this.getLevels().values()) {
                level.save();
            }
            Timings.levelSaveTimer.stopTiming();
        }
    }

    private boolean tick() {
        long tickTime = System.currentTimeMillis();
        long tickTimeNano = System.nanoTime();
        if ((tickTime - this.nextTick) < -25) {
            return false;
        }

        Timings.fullServerTickTimer.startTiming();

        ++this.tickCounter;

        Timings.connectionTimer.startTiming();
        getNetwork().processInterfaces();

        if (this.rcon != null) {
            this.rcon.check();
        }
        Timings.connectionTimer.stopTiming();

        Timings.schedulerTimer.startTiming();
        getScheduler().mainThreadHeartbeat(this.tickCounter);
        Timings.schedulerTimer.stopTiming();

        this.checkTickUpdates(this.tickCounter, tickTime);

        for (Map.Entry<String, Player> entry : players.entrySet()) {
            entry.getValue().checkNetwork();
        }

        if ((this.tickCounter & 0b11111) == 0) {
            this.titleTick();
            this.maxTick = 20;
            this.maxUse = 0;

            if ((this.tickCounter & 0b111111111) == 0) {
                try {
                    this.getPluginManager().callEvent(this.queryRegenerateEvent = new QueryRegenerateEvent(this, 5));
                    if (this.queryHandler != null) {
                        this.queryHandler.regenerateInfo();
                    }
                } catch (Exception e) {
                    this.logger.logException(e);
                }
            }

            this.getNetwork().updateName();
        }

        if (this.autoSave && ++this.autoSaveTicker >= this.autoSaveTicks) {
            this.autoSaveTicker = 0;
            this.doAutoSave();
        }

        if (this.sendUsageTicker > 0 && --this.sendUsageTicker == 0) {
            this.sendUsageTicker = 6000;
            //todo sendUsage
        }

        if (this.tickCounter % 100 == 0) {
            for (Level level : this.levels.values()) {
                level.clearCache();
                level.doChunkGarbageCollection();
            }
        }

        Timings.fullServerTickTimer.stopTiming();
        //long now = System.currentTimeMillis();
        long nowNano = System.nanoTime();
        //float tick = Math.min(20, 1000 / Math.max(1, now - tickTime));
        //float use = Math.min(1, (now - tickTime) / 50);

        float tick = (float) Math.min(20, 1000000000 / Math.max(1000000, ((double) nowNano - tickTimeNano)));
        float use = (float) Math.min(1, ((double) (nowNano - tickTimeNano)) / 50000000);

        if (this.maxTick > tick) {
            this.maxTick = tick;
        }

        if (this.maxUse < use) {
            this.maxUse = use;
        }

        System.arraycopy(this.tickAverage, 1, this.tickAverage, 0, this.tickAverage.length - 1);
        this.tickAverage[this.tickAverage.length - 1] = tick;

        System.arraycopy(this.useAverage, 1, this.useAverage, 0, this.useAverage.length - 1);
        this.useAverage[this.useAverage.length - 1] = use;

        if ((this.nextTick - tickTime) < -1000) {
            this.nextTick = tickTime;
        } else {
            this.nextTick += 50;
        }

        return true;
    }

    public void titleTick() {
        if (!Nukkit.ANSI) {
            return;
        }

        Runtime runtime = Runtime.getRuntime();
        double used = NukkitMath.round((double) (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024, 2);
        double max = NukkitMath.round(((double) runtime.maxMemory()) / 1024 / 1024, 2);
        String usage = Math.round(used / max * 100) + "%";
        String title = (char) 0x1b + "]0;" + this.getName() + " " +
                this.getNukkitVersion() +
                " | Online " + this.players.size() + "/" + this.getMaxPlayers() +
                " | Memory " + usage;
        if (!Nukkit.shortTitle) {
            title += " | U " + NukkitMath.round((this.network.getUpload() / 1024 * 1000), 2)
                    + " D " + NukkitMath.round((this.network.getDownload() / 1024 * 1000), 2) + " kB/s";
        }
        title += " | TPS " + this.getTicksPerSecond() +
                " | Load " + this.getTickUsage() + "%" + (char) 0x07;

        System.out.print(title);

        this.network.resetStatistics();
    }

    public QueryRegenerateEvent getQueryInformation() {
        return this.queryRegenerateEvent;
    }

    public String getName() {
        return "Nukkit";
    }

    public boolean isRunning() {
        return isRunning;
    }

    public String getNukkitVersion() {
        return Nukkit.VERSION;
    }

    public String getCodename() {
        return Nukkit.CODENAME;
    }

    public String getVersion() {
        return Nukkit.MINECRAFT_VERSION;
    }

    public String getApiVersion() {
        return Nukkit.API_VERSION;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getDataPath() {
        return dataPath;
    }

    public String getPluginPath() {
        return pluginPath;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getPort() {
        return this.getPropertyInt("server-port", 19132);
    }

    public int getViewDistance() {
        return this.getPropertyInt("view-distance", 10);
    }

    public String getIp() {
        return this.getPropertyString("server-ip", "0.0.0.0");
    }

    public UUID getServerUniqueId() {
        return this.serverID;
    }

    public boolean getAutoSave() {
        return this.autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
        for (Level level : this.getLevels().values()) {
            level.setAutoSave(this.autoSave);
        }
    }

    public String getLevelType() {
        return this.getPropertyString("level-type", "DEFAULT");
    }

    public boolean getGenerateStructures() {
        return this.getPropertyBoolean("generate-structures", true);
    }

    public int getGamemode() {
        return this.getPropertyInt("gamemode", 0) & 0b11;
    }

    public boolean getForceGamemode() {
        return this.getPropertyBoolean("force-gamemode", false);
    }

    public static String getGamemodeString(int mode) {
        switch (mode) {
            case Player.SURVIVAL:
                return "%gameMode.survival";
            case Player.CREATIVE:
                return "%gameMode.creative";
            case Player.ADVENTURE:
                return "%gameMode.adventure";
            case Player.SPECTATOR:
                return "%gameMode.spectator";
        }
        return "UNKNOWN";
    }

    public static int getGamemodeFromString(String str) {
        switch (str.trim().toLowerCase()) {
            case "0":
            case "survival":
            case "s":
                return Player.SURVIVAL;

            case "1":
            case "creative":
            case "c":
                return Player.CREATIVE;

            case "2":
            case "adventure":
            case "a":
                return Player.ADVENTURE;

            case "3":
            case "spectator":
            case "view":
            case "v":
                return Player.SPECTATOR;
        }
        return -1;
    }

    public static int getDifficultyFromString(String str) {
        switch (str.trim().toLowerCase()) {
            case "0":
            case "peaceful":
            case "p":
                return 0;

            case "1":
            case "easy":
            case "e":
                return 1;

            case "2":
            case "normal":
            case "n":
                return 2;

            case "3":
            case "hard":
            case "h":
                return 3;
        }
        return -1;
    }

    public int getDifficulty() {
        return this.getPropertyInt("difficulty", 1);
    }

    public boolean hasWhitelist() {
        return this.getPropertyBoolean("white-list", false);
    }

    public int getSpawnRadius() {
        return this.getPropertyInt("spawn-protection", 16);
    }

    public boolean getAllowFlight() {
        if (getAllowFlight == null) {
            getAllowFlight = this.getPropertyBoolean("allow-flight", false);
        }
        return getAllowFlight;
    }

    public boolean isHardcore() {
        return this.getPropertyBoolean("hardcore", false);
    }

    public int getDefaultGamemode() {
        return this.getPropertyInt("gamemode", 0);
    }

    public String getMotd() {
        return this.getPropertyString("motd", "Nukkit Server For Minecraft: PE");
    }

    public MainLogger getLogger() {
        return this.logger;
    }

    public EntityMetadataStore getEntityMetadata() {
        return entityMetadata;
    }

    public PlayerMetadataStore getPlayerMetadata() {
        return playerMetadata;
    }

    public LevelMetadataStore getLevelMetadata() {
        return levelMetadata;
    }

    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    public CraftingManager getCraftingManager() {
        return craftingManager;
    }

    public ServerScheduler getScheduler() {
        if (scheduler == null) {
            synchronized (schedulerLock) {
                if (scheduler == null) {
                    return scheduler = new ServerScheduler();
                }
            }
        }
        return scheduler;
    }

    public int getTick() {
        return tickCounter;
    }

    public float getTicksPerSecond() {
        return ((float) Math.round(this.maxTick * 100)) / 100;
    }

    public float getTicksPerSecondAverage() {
        float sum = 0;
        int count = this.tickAverage.length;
        for (float aTickAverage : this.tickAverage) {
            sum += aTickAverage;
        }
        return (float) NukkitMath.round(sum / count, 2);
    }

    public float getTickUsage() {
        return (float) NukkitMath.round(this.maxUse * 100, 2);
    }

    public float getTickUsageAverage() {
        float sum = 0;
        int count = this.useAverage.length;
        for (float aUseAverage : this.useAverage) {
            sum += aUseAverage;
        }
        return ((float) Math.round(sum / count * 100)) / 100;
    }

    public SimpleCommandMap getCommandMap() {
        if (commandMap == null) {
            synchronized (commandMapLock) {
                if (commandMap == null) {
                    commandMap = new SimpleCommandMap(Server.this);
                }
            }
        }
        return commandMap;
    }

    public Map<UUID, Player> getOnlinePlayers() {
        return new HashMap<>(playerList);
    }

    public void addRecipe(Recipe recipe) {
        this.craftingManager.registerRecipe(recipe);
    }

    public IPlayer getOfflinePlayer(String name) {
        IPlayer result = this.getPlayerExact(name.toLowerCase());
        if (result == null) {
            return new OfflinePlayer(this, name);
        }

        return result;
    }

    public CompoundTag getOfflinePlayerData(String name) {
        name = name.toLowerCase();
        String path = this.getDataPath() + "players/";
        File file = new File(path + name + ".dat");

        if (this.shouldSavePlayerData() && file.exists()) {
            try {
                return NBTIO.readCompressed(new FileInputStream(file));
            } catch (Exception e) {
                file.renameTo(new File(path + name + ".dat.bak"));
                this.logger.notice(this.getLanguage().translateString("nukkit.data.playerCorrupted", name));
            }
        } else {
            this.logger.notice(this.getLanguage().translateString("nukkit.data.playerNotFound", name));
        }

        Position spawn = this.getDefaultLevel().getSafeSpawn();
        CompoundTag nbt = new CompoundTag()
                .putLong("firstPlayed", System.currentTimeMillis() / 1000)
                .putLong("lastPlayed", System.currentTimeMillis() / 1000)
                .putList(new ListTag<DoubleTag>("Pos")
                        .add(new DoubleTag("0", spawn.x))
                        .add(new DoubleTag("1", spawn.y))
                        .add(new DoubleTag("2", spawn.z)))
                .putString("Level", this.getDefaultLevel().getName())
                .putList(new ListTag<>("Inventory"))
                .putCompound("Achievements", new CompoundTag())
                .putInt("playerGameType", this.getGamemode())
                .putList(new ListTag<DoubleTag>("Motion")
                        .add(new DoubleTag("0", 0))
                        .add(new DoubleTag("1", 0))
                        .add(new DoubleTag("2", 0)))
                .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("0", 0))
                        .add(new FloatTag("1", 0)))
                .putFloat("FallDistance", 0)
                .putShort("Fire", 0)
                .putShort("Air", 300)
                .putBoolean("OnGround", true)
                .putBoolean("Invulnerable", false)
                .putString("NameTag", name);

        this.saveOfflinePlayerData(name, nbt);
        return nbt;
    }

    public void saveOfflinePlayerData(String name, CompoundTag tag) {
        this.saveOfflinePlayerData(name, tag, false);
    }

    public void saveOfflinePlayerData(String name, CompoundTag tag, boolean async) {
        if (this.shouldSavePlayerData()) {
            try {
                if (async) {
                    this.getScheduler().scheduleAsyncTask(new FileWriteTask(this.getDataPath() + "players/" + name.toLowerCase() + ".dat", NBTIO.writeGZIPCompressed(tag, ByteOrder.BIG_ENDIAN)));
                } else {
                    Utils.writeFile(this.getDataPath() + "players/" + name.toLowerCase() + ".dat", new ByteArrayInputStream(NBTIO.writeGZIPCompressed(tag, ByteOrder.BIG_ENDIAN)));
                }
            } catch (Exception e) {
                this.logger.critical(this.getLanguage().translateString("nukkit.data.saveError", new String[]{name, e.getMessage()}));
                if (Nukkit.DEBUG > 1) {
                    this.logger.logException(e);
                }
            }
        }
    }

    public Player getPlayer(String name) {
        Player found = null;
        name = name.toLowerCase();
        int delta = Integer.MAX_VALUE;
        for (Player player : this.getOnlinePlayers().values()) {
            if (player.getName().toLowerCase().startsWith(name)) {
                int curDelta = player.getName().length() - name.length();
                if (curDelta < delta) {
                    found = player;
                    delta = curDelta;
                }
                if (curDelta == 0) {
                    break;
                }
            }
        }

        return found;
    }

    public Player getPlayerExact(String name) {
        name = name.toLowerCase();
        for (Player player : this.getOnlinePlayers().values()) {
            if (player.getName().toLowerCase().equals(name)) {
                return player;
            }
        }

        return null;
    }

    public Player[] matchPlayer(String partialName) {
        partialName = partialName.toLowerCase();
        List<Player> matchedPlayer = new ArrayList<>();
        for (Player player : this.getOnlinePlayers().values()) {
            if (player.getName().toLowerCase().equals(partialName)) {
                return new Player[]{player};
            } else if (player.getName().toLowerCase().contains(partialName)) {
                matchedPlayer.add(player);
            }
        }

        return matchedPlayer.toArray(new Player[matchedPlayer.size()]);
    }

    public void removePlayer(Player player) {
        String identifier = this.identifier.remove(player.rawHashCode());
        if (identifier != null) {
            if (this.players.remove(identifier) != null) {
                return;
            }
        }
        Iterator<Map.Entry<String, Player>> iter = players.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Player> entry = iter.next();
            if (entry.getValue() == player) {
                iter.remove();
            }
        }
    }

    public Map<Integer, Level> getLevels() {
        return levels;
    }

    public Level getDefaultLevel() {
        return defaultLevel;
    }

    public void setDefaultLevel(Level defaultLevel) {
        if (defaultLevel == null || (this.isLevelLoaded(defaultLevel.getFolderName()) && defaultLevel != this.defaultLevel)) {
            this.defaultLevel = defaultLevel;
        }
    }

    public boolean isLevelLoaded(String name) {
        return this.getLevelByName(name) != null;
    }

    public Level getLevel(int levelId) {
        if (this.levels.containsKey(levelId)) {
            return this.levels.get(levelId);
        }
        return null;
    }

    public Level getLevelByName(String name) {
        for (Level level : this.getLevels().values()) {
            if (level.getFolderName().equals(name)) {
                return level;
            }
        }

        return null;
    }

    public boolean unloadLevel(Level level) {
        return this.unloadLevel(level, false);
    }

    public boolean unloadLevel(Level level, boolean forceUnload) {
        if (level == this.getDefaultLevel() && !forceUnload) {
            throw new IllegalStateException("The default level cannot be unloaded while running, please switch levels.");
        }

        return level.unload(forceUnload);

    }

    public boolean loadLevel(String name) {
        if (Objects.equals(name.trim(), "")) {
            throw new LevelException("Invalid empty level name");
        }
        if (this.isLevelLoaded(name)) {
            return true;
        } else if (!this.isLevelGenerated(name)) {
            this.logger.notice(this.getLanguage().translateString("nukkit.level.notFound", name));
            return false;
        }

        String path = this.getDataPath() + "worlds/" + name + "/";

        Class<? extends LevelProvider> provider = LevelProviderManager.getProvider(path);

        if (provider == null) {
            this.logger.error(this.getLanguage().translateString("nukkit.level.loadError", new String[]{name, "Unknown provider"}));

            return false;
        }

        Level level;
        try {
            level = new Level(this, name, path, provider);
        } catch (Exception e) {
            this.logger.error(this.getLanguage().translateString("nukkit.level.loadError", new String[]{name, e.getMessage()}));
            this.logger.logException(e);
            return false;
        }

        Level existing = this.levels.putIfAbsent(level.getId(), level);
        if (existing != null) {
            return true;
        }

        level.initLevel();

        this.getPluginManager().callEvent(new LevelLoadEvent(level));

        level.setTickRate(this.baseTickRate);

        return true;
    }

    public boolean generateLevel(String name) {
        return this.generateLevel(name, new java.util.Random().nextLong());
    }

    public boolean generateLevel(String name, long seed) {
        return this.generateLevel(name, seed, null);
    }

    public boolean generateLevel(String name, long seed, Class<? extends Generator> generator) {
        return this.generateLevel(name, seed, generator, new HashMap<>());
    }

    public boolean generateLevel(String name, long seed, Class<? extends Generator> generator, Map<String, Object> options) {
        if (Objects.equals(name.trim(), "") || this.isLevelGenerated(name)) {
            return false;
        }

        if (!options.containsKey("preset")) {
            options.put("preset", this.getPropertyString("generator-settings", ""));
        }

        if (generator == null) {
            generator = Generator.getGenerator(this.getLevelType());
        }

        Class<? extends LevelProvider> provider;
        String providerName;
        if ((provider = LevelProviderManager.getProviderByName
                (providerName = (String) this.getConfig("level-settings.default-format", "mcregion"))) == null) {
            provider = LevelProviderManager.getProviderByName(providerName = "mcregion");
        }

        Level level;
        try {
            String path = this.getDataPath() + "worlds/" + name + "/";

            provider.getMethod("generate", String.class, String.class, long.class, Class.class, Map.class).invoke(null, path, name, seed, generator, options);

            level = new Level(this, name, path, provider);
            Level existing = this.levels.putIfAbsent(level.getId(), level);
            if (existing != null) {
                return false;
            }

            level.initLevel();

            level.setTickRate(this.baseTickRate);
        } catch (Exception e) {
            this.logger.error(this.getLanguage().translateString("nukkit.level.generationError", new String[]{name, e.getMessage()}));
            this.logger.logException(e);
            return false;
        }

        this.getPluginManager().callEvent(new LevelInitEvent(level));

        this.getPluginManager().callEvent(new LevelLoadEvent(level));

        /*this.getLogger().notice(this.getLanguage().translateString("nukkit.level.backgroundGeneration", name));

        int centerX = (int) level.getSpawnLocation().getX() >> 4;
        int centerZ = (int) level.getSpawnLocation().getZ() >> 4;

        TreeMap<String, Integer> order = new TreeMap<>();

        for (int X = -3; X <= 3; ++X) {
            for (int Z = -3; Z <= 3; ++Z) {
                int distance = X * X + Z * Z;
                int chunkX = X + centerX;
                int chunkZ = Z + centerZ;
                order.put(Level.chunkHash(chunkX, chunkZ), distance);
            }
        }

        List<Map.Entry<String, Integer>> sortList = new ArrayList<>(order.entrySet());

        Collections.sort(sortList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });

        for (String index : order.keySet()) {
            Chunk.Entry entry = Level.getChunkXZ(index);
            level.populateChunk(entry.chunkX, entry.chunkZ, true);
        }*/

        return true;
    }

    public boolean isLevelGenerated(String name) {
        if (Objects.equals(name.trim(), "")) {
            return false;
        }

        String path = this.getDataPath() + "worlds/" + name + "/";
        if (this.getLevelByName(name) == null) {

            if (LevelProviderManager.getProvider(path) == null) {
                return false;
            }
        }

        return true;
    }

    public BaseLang getLanguage() {
        if (baseLang == null) {
            synchronized (baseLangLock) {
                if (baseLang == null) {
                    forceLanguage = (Boolean) getConfig("settings.force-language", false);
                    baseLang = new BaseLang((String) getConfig("settings.language", BaseLang.FALLBACK_LANGUAGE));
                }
            }
        }
        return baseLang;
    }

    public boolean isLanguageForced() {
        return forceLanguage;
    }

    public Network getNetwork() {
        if (network == null) {
            synchronized (networkLock) {
                if (this.network == null) {
                    this.network = new Network(this);
                    this.network.setName(this.getMotd());
                }
            }
        }
        return network;
    }

    //Revising later...
    public Config getConfig() {
        if (config == null) {
            synchronized (configLock) {
                if (config == null) {
                    this.logger.info("Loading " + TextFormat.GREEN + "nukkit.yml" + TextFormat.WHITE + "...");
                    this.config = new Config(this.dataPath + "nukkit.yml", Config.YAML);
                }
            }
        }
        return this.config;
    }

    public Object getConfig(String variable) {
        return this.getConfig(variable, null);
    }

    public Object getConfig(String variable, Object defaultValue) {
        Object value = this.getConfig().get(variable);
        return value == null ? defaultValue : value;
    }

    public Config getProperties() {
        if (this.properties == null) {
            synchronized (propertiesLock) {
                if (properties == null) {
                    this.logger.info("Loading " + TextFormat.GREEN + "server.properties" + TextFormat.WHITE + "...");
                    this.properties = new Config(this.dataPath + "server.properties", Config.PROPERTIES, new ConfigSection() {
                        {
                            put("motd", "Nukkit Server For Minecraft: PE");
                            put("server-port", 19132);
                            put("server-ip", "0.0.0.0");
                            put("view-distance", 10);
                            put("white-list", false);
                            put("announce-player-achievements", true);
                            put("spawn-protection", 16);
                            put("max-players", 20);
                            put("allow-flight", false);
                            put("spawn-animals", true);
                            put("spawn-mobs", true);
                            put("gamemode", 0);
                            put("force-gamemode", false);
                            put("hardcore", false);
                            put("pvp", true);
                            put("difficulty", 1);
                            put("generator-settings", "");
                            put("level-name", "world");
                            put("level-seed", "");
                            put("level-type", "DEFAULT");
                            put("enable-query", true);
                            put("enable-rcon", false);
                            put("rcon.password", Base64.getEncoder().encodeToString(UUID.randomUUID().toString().replace("-", "").getBytes()).substring(3, 13));
                            put("auto-save", true);
                        }
                    });
                }
            }
        }
        return this.properties;
    }

    public Object getProperty(String variable) {
        return this.getProperty(variable, null);
    }

    public Object getProperty(String variable, Object defaultValue) {
        Config properties = getProperties();
        return properties.exists(variable) ? properties.get(variable) : defaultValue;
    }

    public void setPropertyString(String variable, String value) {
        getProperties().set(variable, value);
        getProperties().save();
    }

    public String getPropertyString(String variable) {
        return this.getPropertyString(variable, null);
    }

    public String getPropertyString(String variable, String defaultValue) {
        Config properties = getProperties();
        return properties.exists(variable) ? (String) properties.get(variable) : defaultValue;
    }

    public int getPropertyInt(String variable) {
        return this.getPropertyInt(variable, null);
    }

    public int getPropertyInt(String variable, Integer defaultValue) {
        Config properties = getProperties();
        return properties.exists(variable) ? (!properties.get(variable).equals("") ? Integer.parseInt(String.valueOf(properties.get(variable))) : defaultValue) : defaultValue;
    }

    public void setPropertyInt(String variable, int value) {
        getProperties().set(variable, value);
        getProperties().save();
    }

    public boolean getPropertyBoolean(String variable) {
        return this.getPropertyBoolean(variable, null);
    }

    public boolean getPropertyBoolean(String variable, Object defaultValue) {
        Config properties = getProperties();
        Object value = properties.exists(variable) ? properties.get(variable) : defaultValue;
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        switch (String.valueOf(value)) {
            case "on":
            case "true":
            case "1":
            case "yes":
                return true;
        }
        return false;
    }

    public void setPropertyBoolean(String variable, boolean value) {
        getProperties().set(variable, value ? "1" : "0");
        getProperties().save();
    }

    public PluginIdentifiableCommand getPluginCommand(String name) {
        Command command = getCommandMap().getCommand(name);
        if (command instanceof PluginIdentifiableCommand) {
            return (PluginIdentifiableCommand) command;
        } else {
            return null;
        }
    }

    public BanList getNameBans() {
        return this.banByName;
    }

    public BanList getIPBans() {
        return this.banByIP;
    }

    public void addOp(String name) {
        this.operators.set(name.toLowerCase(), true);
        Player player = this.getPlayerExact(name);
        if (player != null) {
            player.recalculatePermissions();
        }
        this.operators.save(true);
    }

    public void removeOp(String name) {
        this.operators.remove(name.toLowerCase());
        Player player = this.getPlayerExact(name);
        if (player != null) {
            player.recalculatePermissions();
        }
        this.operators.save();
    }

    public void addWhitelist(String name) {
        this.whitelist.set(name.toLowerCase(), true);
        this.whitelist.save(true);
    }

    public void removeWhitelist(String name) {
        this.whitelist.remove(name.toLowerCase());
        this.whitelist.save(true);
    }

    public boolean isWhitelisted(String name) {
        return !this.hasWhitelist() || this.operators.exists(name, true) || this.whitelist.exists(name, true);
    }

    public boolean isOp(String name) {
        return this.operators.exists(name, true);
    }

    public Config getWhitelist() {
        return whitelist;
    }

    public Config getOps() {
        return operators;
    }

    public void reloadWhitelist() {
        this.whitelist.reload();
    }

    public Map<String, List<String>> getCommandAliases() {
        Object section = this.getConfig("aliases");
        Map<String, List<String>> result = new LinkedHashMap<>();
        if (section instanceof Map) {
            for (Map.Entry entry : (Set<Map.Entry>) ((Map) section).entrySet()) {
                List<String> commands = new ArrayList<>();
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                if (value instanceof List) {
                    for (String string : (List<String>) value) {
                        commands.add(string);
                    }
                } else {
                    commands.add((String) value);
                }

                result.put(key, commands);
            }
        }

        return result;

    }

    public boolean shouldSavePlayerData() {
        return this.getPropertyBoolean("player.save-player-data", true);
    }
    
    private void registerEntities() {
        Entity.registerEntity("Arrow", EntityArrow.class);
        Entity.registerEntity("Item", EntityItem.class);
        Entity.registerEntity("FallingSand", EntityFallingBlock.class);
        Entity.registerEntity("PrimedTnt", EntityPrimedTNT.class);
        Entity.registerEntity("Snowball", EntitySnowball.class);
        Entity.registerEntity("Painting", EntityPainting.class);
        //todo mobs
        Entity.registerEntity("Creeper", EntityCreeper.class);
        //TODO: more mobs
        Entity.registerEntity("Chicken", EntityChicken.class);
        Entity.registerEntity("Cow", EntityCow.class);
        Entity.registerEntity("Pig", EntityPig.class);
        Entity.registerEntity("Rabbit", EntityRabbit.class);
        Entity.registerEntity("Sheep", EntitySheep.class);
        Entity.registerEntity("Wolf", EntityWolf.class);
        Entity.registerEntity("Ocelot", EntityOcelot.class);
        Entity.registerEntity("Villager", EntityVillager.class);

        Entity.registerEntity("ThrownExpBottle", EntityExpBottle.class);
        Entity.registerEntity("XpOrb", EntityXPOrb.class);
        Entity.registerEntity("ThrownPotion", EntityPotion.class);

        Entity.registerEntity("Human", EntityHuman.class, true);

        Entity.registerEntity("MinecartRideable", EntityMinecartEmpty.class);
        // TODO: 2016/1/30 all finds of minecart
        Entity.registerEntity("Boat", EntityBoat.class);

        Entity.registerEntity("Lightning", EntityLightning.class);
    }

    private void registerBlockEntities() {
        BlockEntity.registerBlockEntity(BlockEntity.FURNACE, BlockEntityFurnace.class);
        BlockEntity.registerBlockEntity(BlockEntity.CHEST, BlockEntityChest.class);
        BlockEntity.registerBlockEntity(BlockEntity.SIGN, BlockEntitySign.class);
        BlockEntity.registerBlockEntity(BlockEntity.ENCHANT_TABLE, BlockEntityEnchantTable.class);
        BlockEntity.registerBlockEntity(BlockEntity.SKULL, BlockEntitySkull.class);
        BlockEntity.registerBlockEntity(BlockEntity.FLOWER_POT, BlockEntityFlowerPot.class);
        BlockEntity.registerBlockEntity(BlockEntity.BREWING_STAND, BlockEntityBrewingStand.class);
        BlockEntity.registerBlockEntity(BlockEntity.ITEM_FRAME, BlockEntityItemFrame.class);
        BlockEntity.registerBlockEntity(BlockEntity.CAULDRON, BlockEntityCauldron.class);
    }

    public static Server getInstance() {
        return instance;
    }

}
