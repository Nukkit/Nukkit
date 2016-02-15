package cn.nukkit.bukkit.entity;

import cn.nukkit.*;
import cn.nukkit.math.Vector3;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.*;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.*;
import org.bukkit.util.Vector;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * Implementation of {@link org.bukkit.entity.Player}
 */
public class BukkitPlayer implements Player{
    private cn.nukkit.Player nukkitPlayer;
    private EntityDamageEvent lastDamage;

    public BukkitPlayer(cn.nukkit.Player player) {
        this.nukkitPlayer = player;
    }

    @Override
    public String getDisplayName() {
        return nukkitPlayer.getDisplayName();
    }

    @Override
    public void setDisplayName(String s) {
        nukkitPlayer.setDisplayName(s);
    }

    @Override
    public String getPlayerListName() {
        return getDisplayName();
    }

    @Override
    public void setPlayerListName(String s) {
        setDisplayName(s);
    }

    @Override
    public void setCompassTarget(Location location) {
        throw new NotImplementedException();
    }

    @Override
    public Location getCompassTarget() {
        throw new NotImplementedException();
    }

    @Override
    public InetSocketAddress getAddress() {
        return new InetSocketAddress(nukkitPlayer.getAddress(), nukkitPlayer.getPort());
    }

    @Override
    public boolean isConversing() {
        throw new NotImplementedException();
    }

    @Override
    public void acceptConversationInput(String s) {
        throw new NotImplementedException();
    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        throw new NotImplementedException();
    }

    @Override
    public void abandonConversation(Conversation conversation) {
        throw new NotImplementedException();
    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent conversationAbandonedEvent) {
        throw new NotImplementedException();
    }

    @Override
    public void sendRawMessage(String s) {
        sendMessage(s);
    }

    @Override
    public void kickPlayer(String s) {
        nukkitPlayer.kick(s);
    }

    @Override
    public void chat(String s) {
        throw new NotImplementedException();
    }

    @Override
    public boolean performCommand(String s) {
        throw new NotImplementedException();
    }

    @Override
    public boolean isSneaking() {
        return nukkitPlayer.isSneaking();
    }

    @Override
    public void setSneaking(boolean b) {
        nukkitPlayer.setSneaking(b);
    }

    @Override
    public boolean isSprinting() {
        return nukkitPlayer.isSprinting();
    }

    @Override
    public void setSprinting(boolean b) {
        nukkitPlayer.setSprinting(b);
    }

    @Override
    public void saveData() {
        throw new NotImplementedException();
    }

    @Override
    public void loadData() {
        throw new NotImplementedException();
    }

    @Override
    public void setSleepingIgnored(boolean b) {
        throw new NotImplementedException();
    }

    @Override
    public boolean isSleepingIgnored() {
        throw new NotImplementedException();
    }

    @Override
    public void playNote(Location location, byte b, byte b1) {
        throw new NotImplementedException();
    }

    @Override
    public void playNote(Location location, Instrument instrument, Note note) {
        throw new NotImplementedException();
    }

    @Override
    public void playSound(Location location, Sound sound, float v, float v1) {
        throw new NotImplementedException();
    }

    @Override
    public void playSound(Location location, String s, float v, float v1) {
        throw new NotImplementedException();
    }

    @Override
    public void playEffect(Location location, Effect effect, int i) {
        throw new NotImplementedException();
    }

    @Override
    public <T> void playEffect(Location location, Effect effect, T t) {
        throw new NotImplementedException();
    }

    @Override
    public void sendBlockChange(Location location, Material material, byte b) {
        throw new NotImplementedException();
    }

    @Override
    public boolean sendChunkChange(Location location, int i, int i1, int i2, byte[] bytes) {
        throw new NotImplementedException();
    }

    @Override
    public void sendBlockChange(Location location, int i, byte b) {
        throw new NotImplementedException();
    }

    @Override
    public void sendSignChange(Location location, String[] strings) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    @Override
    public void sendMap(MapView mapView) {
        throw new NotImplementedException();
    }

    @Override
    public void updateInventory() {
        throw new NotImplementedException();
    }

    @Override
    public void awardAchievement(Achievement achievement) {
        throw new NotImplementedException();
    }

    @Override
    public void removeAchievement(Achievement achievement) {
        throw new NotImplementedException();
    }

    @Override
    public boolean hasAchievement(Achievement achievement) {
        throw new NotImplementedException();
    }

    @Override
    public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    @Override
    public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    @Override
    public void incrementStatistic(Statistic statistic, int i) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    @Override
    public void decrementStatistic(Statistic statistic, int i) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    @Override
    public void setStatistic(Statistic statistic, int i) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    @Override
    public int getStatistic(Statistic statistic) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    @Override
    public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    @Override
    public void setStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    @Override
    public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType, int i) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType, int i) {
        throw new NotImplementedException();
    }

    @Override
    public void setStatistic(Statistic statistic, EntityType entityType, int i) {
        throw new NotImplementedException();
    }

    @Override
    public void setPlayerTime(long l, boolean b) {
        throw new NotImplementedException();
    }

    @Override
    public long getPlayerTime() {
        throw new NotImplementedException();
    }

    @Override
    public long getPlayerTimeOffset() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isPlayerTimeRelative() {
        throw new NotImplementedException();
    }

    @Override
    public void resetPlayerTime() {
        throw new NotImplementedException();
    }

    @Override
    public void setPlayerWeather(WeatherType weatherType) {
        throw new NotImplementedException();
    }

    @Override
    public WeatherType getPlayerWeather() {
        throw new NotImplementedException();
    }

    @Override
    public void resetPlayerWeather() {
        throw new NotImplementedException();
    }

    @Override
    public void giveExp(int i) {
        nukkitPlayer.addExperience(i);
    }

    @Override
    public void giveExpLevels(int i) {
        nukkitPlayer.sendExperienceLevel(i);
    }

    @Override
    public float getExp() {
        return nukkitPlayer.getExperience();
    }

    @Override
    public void setExp(float v) {
        nukkitPlayer.setExperience(Math.round(v));
    }

    @Override
    public int getLevel() {
        return nukkitPlayer.getExperienceLevel();
    }

    @Override
    public void setLevel(int i) {
        nukkitPlayer.sendExperienceLevel(i);
    }

    @Override
    public int getTotalExperience() {
        throw new NotImplementedException();
    }

    @Override
    public void setTotalExperience(int i) {
        throw new NotImplementedException();
    }

    @Override
    public float getExhaustion() {
        throw new NotImplementedException();
    }

    @Override
    public void setExhaustion(float v) {
        throw new NotImplementedException();
    }

    @Override
    public float getSaturation() {
        return nukkitPlayer.getFoodData().getFoodSaturationLevel();
    }

    @Override
    public void setSaturation(float v) {
        nukkitPlayer.getFoodData().setFoodSaturationLevel(v);
    }

    @Override
    public int getFoodLevel() {
        return nukkitPlayer.getFoodData().getLevel();
    }

    @Override
    public void setFoodLevel(int i) {
        nukkitPlayer.getFoodData().setLevel(i);
    }

    @Override
    public Location getBedSpawnLocation() {
        throw new NotImplementedException();
    }

    @Override
    public void setBedSpawnLocation(Location location) {
        throw new NotImplementedException();
    }

    @Override
    public void setBedSpawnLocation(Location location, boolean b) {
        throw new NotImplementedException();
    }

    @Override
    public boolean getAllowFlight() {
        return nukkitPlayer.getAllowFlight();
    }

    @Override
    public void setAllowFlight(boolean b) {
        nukkitPlayer.setAllowFlight(b);
    }

    @Override
    public void hidePlayer(Player player) {
        cn.nukkit.Player nukkitExternalPlayer = cn.nukkit.Server.getInstance().getPlayer(player.getDisplayName());
        nukkitPlayer.hidePlayer(nukkitExternalPlayer);
    }

    @Override
    public void showPlayer(Player player) {
        cn.nukkit.Player nukkitExternalPlayer = cn.nukkit.Server.getInstance().getPlayer(player.getDisplayName());
        nukkitExternalPlayer.showPlayer(nukkitExternalPlayer);
    }

    @Override
    public boolean canSee(Player player) {
        return nukkitPlayer.canSee(cn.nukkit.Server.getInstance().getPlayer(player.getDisplayName()));
    }

    @Override
    public Location getLocation() {
        return new Location(getWorld(), nukkitPlayer.getLocation().getX(), nukkitPlayer.getLocation().getY(), nukkitPlayer.getLocation().getZ());
    }

    @Override
    public Location getLocation(Location location) {
        return getLocation();
    }

    @Override
    public void setVelocity(Vector vector) {
        throw new NotImplementedException();
    }

    @Override
    public Vector getVelocity() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isOnGround() {
        return nukkitPlayer.isOnGround();
    }

    @Override
    public World getWorld() {
        //todo after world implementation
        return null;
    }

    @Override
    public boolean teleport(Location location) {
        nukkitPlayer.teleport(new Vector3(location.getX(), location.getY(), location.getZ()));
        return true;
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause teleportCause) {
        PlayerTeleportEvent event = new PlayerTeleportEvent(this, new Location(getWorld(), nukkitPlayer.getLocation().getX(), nukkitPlayer.getLocation().getY(), nukkitPlayer.getLocation().getZ()), location, teleportCause);
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled()) {
            return false;
        }
        nukkitPlayer.teleport(new Vector3(location.getX(), location.getY(), location.getZ()));
        return true;
    }

    @Override
    public boolean teleport(Entity entity) {
        return teleport(entity.getLocation());
    }

    @Override
    public boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause teleportCause) {
        return teleport(entity.getLocation(), teleportCause);
    }

    @Override
    public List<Entity> getNearbyEntities(double v, double v1, double v2) {
        throw new NotImplementedException();
    }

    @Override
    public int getEntityId() {
        throw new NotImplementedException();
    }

    @Override
    public int getFireTicks() {
        throw new NotImplementedException();
    }

    @Override
    public int getMaxFireTicks() {
        throw new NotImplementedException();
    }

    @Override
    public void setFireTicks(int i) {
        throw new NotImplementedException();
    }

    @Override
    public void remove() {
        nukkitPlayer.kill();
    }

    @Override
    public boolean isDead() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isValid() {
        return nukkitPlayer.isValid();
    }

    @Override
    public void sendMessage(String s) {
        nukkitPlayer.sendMessage(s);
    }

    @Override
    public void sendMessage(String[] strings) {
        for (String string : strings) {
            nukkitPlayer.sendMessage(string);
        }
    }

    @Override
    public Server getServer() {
        return cn.nukkit.Server.getInstance().getBukkitServer();
    }

    @Override
    public Entity getPassenger() {
        throw new NotImplementedException();
    }

    @Override
    public boolean setPassenger(Entity entity) {
        throw new NotImplementedException();
    }

    @Override
    public boolean isEmpty() {
        throw new NotImplementedException();
    }

    @Override
    public boolean eject() {
        throw new NotImplementedException();
    }

    @Override
    public float getFallDistance() {
        throw new NotImplementedException();
    }

    @Override
    public void setFallDistance(float v) {
        throw new NotImplementedException();
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent entityDamageEvent) {
        throw new NotImplementedException();
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        return this.lastDamage;
    }

    @Override
    public UUID getUniqueId() {
        return nukkitPlayer.getUniqueId();
    }

    @Override
    public boolean isBanned() {
        return nukkitPlayer.isBanned();
    }

    @Override
    public void setBanned(boolean b) {
        nukkitPlayer.setBanned(b);
    }

    @Override
    public boolean isWhitelisted() {
        return nukkitPlayer.isWhitelisted();
    }

    @Override
    public void setWhitelisted(boolean b) {
        nukkitPlayer.setWhitelisted(b);
    }

    @Override
    public Player getPlayer() {
        return this;
    }

    @Override
    public long getFirstPlayed() {
        return nukkitPlayer.getFirstPlayed();
    }

    @Override
    public long getLastPlayed() {
        return nukkitPlayer.getLastPlayed();
    }

    @Override
    public boolean hasPlayedBefore() {
        return nukkitPlayer.hasPlayedBefore();
    }

    @Override
    public int getTicksLived() {
        throw new NotImplementedException();
    }

    @Override
    public void setTicksLived(int i) {
        throw new NotImplementedException();
    }

    @Override
    public void playEffect(EntityEffect entityEffect) {
        throw new NotImplementedException();
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    public boolean isInsideVehicle() {
        throw new NotImplementedException();
    }

    @Override
    public boolean leaveVehicle() {
        throw new NotImplementedException();
    }

    @Override
    public Entity getVehicle() {
        throw new NotImplementedException();
    }

    @Override
    public void setCustomName(String s) {
        nukkitPlayer.setNameTag(s);
    }

    @Override
    public String getCustomName() {
        return nukkitPlayer.getNameTag();
    }

    @Override
    public void setCustomNameVisible(boolean b) {
        nukkitPlayer.setNameTagVisible(b);
    }

    @Override
    public boolean isCustomNameVisible() {
        return nukkitPlayer.isNameTagVisible();
    }

    @Override
    public boolean isFlying() {
        throw new NotImplementedException();
    }

    @Override
    public void setFlying(boolean b) {
        throw new NotImplementedException();
    }

    @Override
    public void setFlySpeed(float v) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    @Override
    public void setWalkSpeed(float v) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    @Override
    public float getFlySpeed() {
        throw new NotImplementedException();
    }

    @Override
    public float getWalkSpeed() {
        throw new NotImplementedException();
    }

    @Override
    public void setTexturePack(String s) {
        throw new NotImplementedException();
    }

    @Override
    public void setResourcePack(String s) {
        throw new NotImplementedException();
    }

    @Override
    public Scoreboard getScoreboard() {
        throw new NotImplementedException();
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
        throw new NotImplementedException();
    }

    @Override
    public boolean isHealthScaled() {
        throw new NotImplementedException();
    }

    @Override
    public void setHealthScaled(boolean b) {
        throw new NotImplementedException();
    }

    @Override
    public void setHealthScale(double v) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    @Override
    public double getHealthScale() {
        throw new NotImplementedException();
    }

    @Override
    public Entity getSpectatorTarget() {
        throw new NotImplementedException();
    }

    @Override
    public void setSpectatorTarget(Entity entity) {
        throw new NotImplementedException();
    }

    @Override
    public void sendTitle(String s, String s1) {
        nukkitPlayer.sendPopup(s, s1);
    }

    @Override
    public void resetTitle() {
        throw new NotImplementedException();
    }

    @Override
    public Map<String, Object> serialize() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isOnline() {
        return nukkitPlayer.isOnline();
    }

    @Override
    public String getName() {
        return nukkitPlayer.getName();
    }

    @Override
    public PlayerInventory getInventory() {
        throw new NotImplementedException();
    }

    @Override
    public Inventory getEnderChest() {
        throw new NotImplementedException();
    }

    @Override
    public boolean setWindowProperty(InventoryView.Property property, int i) {
        throw new NotImplementedException();
    }

    @Override
    public InventoryView getOpenInventory() {
        throw new NotImplementedException();
    }

    @Override
    public InventoryView openInventory(Inventory inventory) {
        return null;
    }

    @Override
    public InventoryView openWorkbench(Location location, boolean b) {
        return null;
    }

    @Override
    public InventoryView openEnchanting(Location location, boolean b) {
        return null;
    }

    @Override
    public void openInventory(InventoryView inventoryView) {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public ItemStack getItemInHand() {
        return null;
    }

    @Override
    public void setItemInHand(ItemStack itemStack) {

    }

    @Override
    public ItemStack getItemOnCursor() {
        return null;
    }

    @Override
    public void setItemOnCursor(ItemStack itemStack) {

    }

    @Override
    public boolean isSleeping() {
        return false;
    }

    @Override
    public int getSleepTicks() {
        return 0;
    }

    @Override
    public GameMode getGameMode() {
        return null;
    }

    @Override
    public void setGameMode(GameMode gameMode) {

    }

    @Override
    public boolean isBlocking() {
        return false;
    }

    @Override
    public int getExpToLevel() {
        return 0;
    }

    @Override
    public double getEyeHeight() {
        return 0;
    }

    @Override
    public double getEyeHeight(boolean b) {
        return 0;
    }

    @Override
    public Location getEyeLocation() {
        return null;
    }

    @Override
    public List<Block> getLineOfSight(HashSet<Byte> hashSet, int i) {
        return null;
    }

    @Override
    public List<Block> getLineOfSight(Set<Material> set, int i) {
        return null;
    }

    @Override
    public Block getTargetBlock(HashSet<Byte> hashSet, int i) {
        return null;
    }

    @Override
    public Block getTargetBlock(Set<Material> set, int i) {
        return null;
    }

    @Override
    public List<Block> getLastTwoTargetBlocks(HashSet<Byte> hashSet, int i) {
        return null;
    }

    @Override
    public List<Block> getLastTwoTargetBlocks(Set<Material> set, int i) {
        return null;
    }

    @Override
    public Egg throwEgg() {
        return null;
    }

    @Override
    public Snowball throwSnowball() {
        return null;
    }

    @Override
    public Arrow shootArrow() {
        return null;
    }

    @Override
    public int getRemainingAir() {
        return 0;
    }

    @Override
    public void setRemainingAir(int i) {

    }

    @Override
    public int getMaximumAir() {
        return 0;
    }

    @Override
    public void setMaximumAir(int i) {

    }

    @Override
    public int getMaximumNoDamageTicks() {
        return 0;
    }

    @Override
    public void setMaximumNoDamageTicks(int i) {

    }

    @Override
    public double getLastDamage() {
        return 0;
    }

    @Override
    public int _INVALID_getLastDamage() {
        return 0;
    }

    @Override
    public void setLastDamage(double v) {

    }

    @Override
    public void _INVALID_setLastDamage(int i) {

    }

    @Override
    public int getNoDamageTicks() {
        return 0;
    }

    @Override
    public void setNoDamageTicks(int i) {

    }

    @Override
    public Player getKiller() {
        return null;
    }

    @Override
    public boolean addPotionEffect(PotionEffect potionEffect) {
        return false;
    }

    @Override
    public boolean addPotionEffect(PotionEffect potionEffect, boolean b) {
        return false;
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffect> collection) {
        return false;
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType potionEffectType) {
        return false;
    }

    @Override
    public void removePotionEffect(PotionEffectType potionEffectType) {

    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        return null;
    }

    @Override
    public boolean hasLineOfSight(Entity entity) {
        return false;
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return false;
    }

    @Override
    public void setRemoveWhenFarAway(boolean b) {

    }

    @Override
    public EntityEquipment getEquipment() {
        return null;
    }

    @Override
    public void setCanPickupItems(boolean b) {

    }

    @Override
    public boolean getCanPickupItems() {
        return false;
    }

    @Override
    public boolean isLeashed() {
        return false;
    }

    @Override
    public Entity getLeashHolder() throws IllegalStateException {
        return null;
    }

    @Override
    public boolean setLeashHolder(Entity entity) {
        return false;
    }

    @Override
    public void damage(double v) {

    }

    @Override
    public void _INVALID_damage(int i) {

    }

    @Override
    public void damage(double v, Entity entity) {

    }

    @Override
    public void _INVALID_damage(int i, Entity entity) {

    }

    @Override
    public double getHealth() {
        return 0;
    }

    @Override
    public int _INVALID_getHealth() {
        return 0;
    }

    @Override
    public void setHealth(double v) {

    }

    @Override
    public void _INVALID_setHealth(int i) {

    }

    @Override
    public double getMaxHealth() {
        return 0;
    }

    @Override
    public int _INVALID_getMaxHealth() {
        return 0;
    }

    @Override
    public void setMaxHealth(double v) {

    }

    @Override
    public void _INVALID_setMaxHealth(int i) {

    }

    @Override
    public void resetMaxHealth() {

    }

    @Override
    public void setMetadata(String s, MetadataValue metadataValue) {

    }

    @Override
    public List<MetadataValue> getMetadata(String s) {
        return null;
    }

    @Override
    public boolean hasMetadata(String s) {
        return false;
    }

    @Override
    public void removeMetadata(String s, Plugin plugin) {

    }

    @Override
    public boolean isPermissionSet(String s) {
        return false;
    }

    @Override
    public boolean isPermissionSet(Permission permission) {
        return false;
    }

    @Override
    public boolean hasPermission(String s) {
        return false;
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return false;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int i) {
        return null;
    }

    @Override
    public void removeAttachment(PermissionAttachment permissionAttachment) {

    }

    @Override
    public void recalculatePermissions() {

    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return null;
    }

    @Override
    public void sendPluginMessage(Plugin plugin, String s, byte[] bytes) {

    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return null;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> aClass) {
        return null;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> aClass, Vector vector) {
        return null;
    }

    @Override
    public boolean isOp() {
        return false;
    }

    @Override
    public void setOp(boolean b) {

    }
}
