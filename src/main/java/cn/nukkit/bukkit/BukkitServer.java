package cn.nukkit.bukkit;

import cn.nukkit.bukkit.entity.BukkitPlayer;
import cn.nukkit.bukkit.thread.TaskScheduler;
import com.avaje.ebean.config.ServerConfig;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.logging.Logger;

/**
 * Implementation of {@link org.bukkit.Server}
 */
public class BukkitServer implements Server {
    private cn.nukkit.Server nukkitServer;

    public BukkitServer(cn.nukkit.Server server) {
        this.nukkitServer = server;
    }

    @Override
    public String getName() {
        return nukkitServer.getName();
    }

    @Override
    public String getVersion() {
        return nukkitServer.getVersion();
    }

    @Override
    public String getBukkitVersion() {
        return "1.8.8-R0.1-SNAPSHOT";
    }

    @Override
    public Player[] _INVALID_getOnlinePlayers() {
        Collection<cn.nukkit.Player> nukkitPlayers = nukkitServer.getOnlinePlayers().values();
        ArrayList<Player> players = new ArrayList<Player>();
        for (cn.nukkit.Player player : nukkitPlayers) {
            players.add(new BukkitPlayer(player));
        }
        return players.toArray(new Player[players.size()]);
    }

    @Override
    public Collection<? extends Player> getOnlinePlayers() {
        return new ArrayList<Player>(Arrays.asList(_INVALID_getOnlinePlayers()));
    }

    @Override
    public int getMaxPlayers() {
        return nukkitServer.getMaxPlayers();
    }

    @Override
    public int getPort() {
        return nukkitServer.getPort();
    }

    @Override
    public int getViewDistance() {
        return nukkitServer.getViewDistance();
    }

    @Override
    public String getIp() {
        return nukkitServer.getIp();
    }

    @Override
    public String getServerName() {
        return nukkitServer.getName();
    }

    @Override
    public String getServerId() {
        return nukkitServer.getServerUniqueId().toString();
    }

    @Override
    public String getWorldType() {
        return nukkitServer.getLevelType();
    }

    @Override
    public boolean getGenerateStructures() {
        return nukkitServer.getGenerateStructures();
    }

    @Override
    public boolean getAllowEnd() {
        return false;
    }

    @Override
    public boolean getAllowNether() {
        return true;
    }

    @Override
    public boolean hasWhitelist() {
        return nukkitServer.hasWhitelist();
    }

    @Override
    public void setWhitelist(boolean b) {
        throw new NotImplementedException();
    }

    @Override
    public Set<OfflinePlayer> getWhitelistedPlayers() {
        return null;
    }

    @Override
    public void reloadWhitelist() {

    }

    @Override
    public int broadcastMessage(String s) {
        nukkitServer.broadcastMessage(s);
        return 0;
    }

    @Override
    public String getUpdateFolder() {
        throw new NotImplementedException();
    }

    @Override
    public File getUpdateFolderFile() {
        throw new NotImplementedException();
    }

    @Override
    public long getConnectionThrottle() {
        throw new NotImplementedException();
    }

    @Override
    public int getTicksPerAnimalSpawns() {
        throw new NotImplementedException();
    }

    @Override
    public int getTicksPerMonsterSpawns() {
        throw new NotImplementedException();
    }

    @Override
    public Player getPlayer(String s) {
        Map<String, cn.nukkit.Player> nukkitPlayers = nukkitServer.getOnlinePlayers();
        for (Map.Entry<String, cn.nukkit.Player> entry : nukkitPlayers.entrySet()) {
            if(entry.getKey().equals(s)) {
                return new BukkitPlayer(entry.getValue());
            }
        }
        return null;
    }

    @Override
    public Player getPlayerExact(String s) {
        return getPlayer(s);
    }

    @Override
    public List<Player> matchPlayer(String s) {
        throw new NotImplementedException();
    }

    @Override
    public Player getPlayer(UUID uuid) {
        Player[] players = this.getOnlinePlayers().toArray(new Player[this.getOnlinePlayers().size()]);
        for (Player player : players) {
            if(player.getUniqueId() == (uuid)){
                return player;
            }
        }
        return null;
    }

    @Override
    public PluginManager getPluginManager() {
        return new SimplePluginManager(this, new SimpleCommandMap(this));
    }

    @Override
    public BukkitScheduler getScheduler() {
        return new TaskScheduler(nukkitServer.getScheduler());
    }

    @Override
    public ServicesManager getServicesManager() {
        throw new NotImplementedException();
    }

    @Override
    public List<World> getWorlds() {
        return null; //todo todo
    }

    @Override
    public World createWorld(WorldCreator worldCreator) {
        return null; //todo todo
    }

    @Override
    public boolean unloadWorld(String s, boolean b) {
        return false; //todo todo
    }

    @Override
    public boolean unloadWorld(World world, boolean b) {
        return false; // todo todo
    }

    @Override
    public World getWorld(String s) {
        return null; // todo todo first priority
    }

    @Override
    public World getWorld(UUID uuid) {
        return null; // todo todo second priority
    }

    @Override
    public MapView getMap(short i) {
        throw new NotImplementedException();
    }

    @Override
    public MapView createMap(World world) {
        throw new NotImplementedException();
    }

    @Override
    public void reload() {
        throw new NotImplementedException();
    }

    @Override
    public Logger getLogger() {
        throw new NotImplementedException();
    }

    @Override
    public PluginCommand getPluginCommand(String s) {
        throw new NotImplementedException();
    }

    @Override
    public void savePlayers() {

    }

    @Override
    public boolean dispatchCommand(CommandSender commandSender, String s) throws CommandException {
        return false;
    }

    @Override
    public void configureDbConfig(ServerConfig serverConfig) {

    }

    @Override
    public boolean addRecipe(Recipe recipe) {
        return false;
    }

    @Override
    public List<Recipe> getRecipesFor(ItemStack itemStack) {
        return null;
    }

    @Override
    public Iterator<Recipe> recipeIterator() {
        return null;
    }

    @Override
    public void clearRecipes() {

    }

    @Override
    public void resetRecipes() {

    }

    @Override
    public Map<String, String[]> getCommandAliases() {
        return null;
    }

    @Override
    public int getSpawnRadius() {
        return 0;
    }

    @Override
    public void setSpawnRadius(int i) {

    }

    @Override
    public boolean getOnlineMode() {
        return false;
    }

    @Override
    public boolean getAllowFlight() {
        return false;
    }

    @Override
    public boolean isHardcore() {
        return false;
    }

    @Override
    public boolean useExactLoginLocation() {
        return false;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public int broadcast(String s, String s1) {
        return 0;
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String s) {
        return null;
    }

    @Override
    public OfflinePlayer getOfflinePlayer(UUID uuid) {
        return null;
    }

    @Override
    public Set<String> getIPBans() {
        return null;
    }

    @Override
    public void banIP(String s) {

    }

    @Override
    public void unbanIP(String s) {

    }

    @Override
    public Set<OfflinePlayer> getBannedPlayers() {
        return null;
    }

    @Override
    public BanList getBanList(BanList.Type type) {
        return null;
    }

    @Override
    public Set<OfflinePlayer> getOperators() {
        return null;
    }

    @Override
    public GameMode getDefaultGameMode() {
        return null;
    }

    @Override
    public void setDefaultGameMode(GameMode gameMode) {

    }

    @Override
    public ConsoleCommandSender getConsoleSender() {
        return null;
    }

    @Override
    public File getWorldContainer() {
        return null;
    }

    @Override
    public OfflinePlayer[] getOfflinePlayers() {
        return new OfflinePlayer[0];
    }

    @Override
    public Messenger getMessenger() {
        return null;
    }

    @Override
    public HelpMap getHelpMap() {
        return null;
    }

    @Override
    public Inventory createInventory(InventoryHolder inventoryHolder, InventoryType inventoryType) {
        return null;
    }

    @Override
    public Inventory createInventory(InventoryHolder inventoryHolder, InventoryType inventoryType, String s) {
        return null;
    }

    @Override
    public Inventory createInventory(InventoryHolder inventoryHolder, int i) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Inventory createInventory(InventoryHolder inventoryHolder, int i, String s) throws IllegalArgumentException {
        return null;
    }

    @Override
    public int getMonsterSpawnLimit() {
        return 0;
    }

    @Override
    public int getAnimalSpawnLimit() {
        return 0;
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        return 0;
    }

    @Override
    public int getAmbientSpawnLimit() {
        return 0;
    }

    @Override
    public boolean isPrimaryThread() {
        return false;
    }

    @Override
    public String getMotd() {
        return null;
    }

    @Override
    public String getShutdownMessage() {
        return null;
    }

    @Override
    public Warning.WarningState getWarningState() {
        return null;
    }

    @Override
    public ItemFactory getItemFactory() {
        return null;
    }

    @Override
    public ScoreboardManager getScoreboardManager() {
        return null;
    }

    @Override
    public CachedServerIcon getServerIcon() {
        return null;
    }

    @Override
    public CachedServerIcon loadServerIcon(File file) throws IllegalArgumentException, Exception {
        return null;
    }

    @Override
    public CachedServerIcon loadServerIcon(BufferedImage bufferedImage) throws IllegalArgumentException, Exception {
        return null;
    }

    @Override
    public void setIdleTimeout(int i) {

    }

    @Override
    public int getIdleTimeout() {
        return 0;
    }

    @Override
    public ChunkGenerator.ChunkData createChunkData(World world) {
        return null;
    }

    @Override
    public UnsafeValues getUnsafe() {
        return null;
    }

    @Override
    public void sendPluginMessage(Plugin plugin, String s, byte[] bytes) {

    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return null;
    }
}
