package cn.nukkit.service;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.block.BlockAir;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.RegisteredListener;
import java.util.ArrayList;

public class DefaultProtectionService implements ProtectionService {
    private final Plugin plugin;

    private static Item AIR = new ItemBlock(new BlockAir(), 0, 0);

    public DefaultProtectionService(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public boolean isEnabled() {
        return plugin == null ? true : plugin.isEnabled();
    }

    public String getName() {
        return plugin == null ? "Nukkit" : plugin.getName();
    }

    public boolean isAllowedEntry(IPlayer player, Position pos) {
        return isAllowedEntry(player, pos, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public boolean isAllowedEntry(IPlayer player, Position pos, PlayerTeleportEvent.TeleportCause cause) {
        if (!(player instanceof Player)) {
            return true;
        }
        Player nukkitPlayer = (Player) player;
        PlayerTeleportEvent event = new PlayerTeleportEvent(nukkitPlayer, nukkitPlayer.getLocation(), pos, PlayerTeleportEvent.TeleportCause.PLUGIN);
        ArrayList<RegisteredListener> nonMonitor = BlockBreakEvent.getHandlers().getRegisteredListeners(EventPriority.HIGHEST, EventPriority.HIGH, EventPriority.NORMAL, EventPriority.LOW, EventPriority.LOWEST);
        for (RegisteredListener listener : nonMonitor) {
            listener.callEvent(event);
        }
        if (event.isCancelled()) {
            return false;
        }
        return true;
    }

    public boolean isAllowedInteract(IPlayer player, Position pos) {
        return isAllowedInteract(player, pos, 0, PlayerInteractEvent.RIGHT_CLICK_BLOCK);
    }

    public boolean isAllowedInteract(IPlayer player, Position pos, int face, int action) {
        if (!(player instanceof Player)) {
            return true;
        }
        PlayerInteractEvent event = new PlayerInteractEvent((Player) player, AIR, pos, face, action);
        ArrayList<RegisteredListener> nonMonitor = BlockBreakEvent.getHandlers().getRegisteredListeners(EventPriority.HIGHEST, EventPriority.HIGH, EventPriority.NORMAL, EventPriority.LOW, EventPriority.LOWEST);
        for (RegisteredListener listener : nonMonitor) {
            listener.callEvent(event);
        }
        if (event.isCancelled()) {
            return false;
        }
        return true;
    }

    public boolean isAllowedModification(IPlayer player, Position pos) {
        if (!(player instanceof Player)) {
            return true;
        }
        BlockBreakEvent event = new BlockBreakEvent((Player) player, pos.getLevelBlock(), AIR);
        ArrayList<RegisteredListener> nonMonitor = BlockBreakEvent.getHandlers().getRegisteredListeners(EventPriority.HIGHEST, EventPriority.HIGH, EventPriority.NORMAL, EventPriority.LOW, EventPriority.LOWEST);
        for (RegisteredListener listener : nonMonitor) {
            listener.callEvent(event);
        }
        if (event.isCancelled()) {
            return false;
        }
        return true;
    }
}