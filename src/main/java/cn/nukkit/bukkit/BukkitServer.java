package cn.nukkit.bukkit;

import com.avaje.ebean.config.ServerConfig;
import org.bukkit.*;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
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
        return new Player[0];
    }

    @Override
    public Collection<? extends Player> getOnlinePlayers() {
        return null;
    }

    @Override
    public int getMaxPlayers() {
        return 0;
    }

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public int getViewDistance() {
        return 0;
    }

    @Override
    public String getIp() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public String getServerId() {
        return null;
    }

    @Override
    public String getWorldType() {
        return null;
    }

    @Override
    public boolean getGenerateStructures() {
        return false;
    }

    @Override
    public boolean getAllowEnd() {
        return false;
    }

    @Override
    public boolean getAllowNether() {
        return false;
    }

    @Override
    public boolean hasWhitelist() {
        return false;
    }

    @Override
    public void setWhitelist(boolean b) {

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
        return 0;
    }

    @Override
    public String getUpdateFolder() {
        return null;
    }

    @Override
    public File getUpdateFolderFile() {
        return null;
    }

    @Override
    public long getConnectionThrottle() {
        return 0;
    }

    @Override
    public int getTicksPerAnimalSpawns() {
        return 0;
    }

    @Override
    public int getTicksPerMonsterSpawns() {
        return 0;
    }

    @Override
    public Player getPlayer(String s) {
        return null;
    }

    @Override
    public Player getPlayerExact(String s) {
        return null;
    }

    @Override
    public List<Player> matchPlayer(String s) {
        return null;
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return null;
    }

    @Override
    public PluginManager getPluginManager() {
        return null;
    }

    @Override
    public BukkitScheduler getScheduler() {
        return null;
    }

    @Override
    public ServicesManager getServicesManager() {
        return null;
    }

    @Override
    public List<World> getWorlds() {
        return null;
    }

    @Override
    public World createWorld(WorldCreator worldCreator) {
        return null;
    }

    @Override
    public boolean unloadWorld(String s, boolean b) {
        return false;
    }

    @Override
    public boolean unloadWorld(World world, boolean b) {
        return false;
    }

    @Override
    public World getWorld(String s) {
        return null;
    }

    @Override
    public World getWorld(UUID uuid) {
        return null;
    }

    @Override
    public MapView getMap(short i) {
        return null;
    }

    @Override
    public MapView createMap(World world) {
        return null;
    }

    @Override
    public void reload() {

    }

    @Override
    public Logger getLogger() {
        return null;
    }

    @Override
    public PluginCommand getPluginCommand(String s) {
        return null;
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
