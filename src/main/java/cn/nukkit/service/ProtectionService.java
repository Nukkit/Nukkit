package cn.nukkit.service;

import cn.nukkit.IPlayer;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.Plugin;

public interface ProtectionService extends Service {
    public Plugin getPlugin();

    public boolean isEnabled();

    public String getName();

    public boolean isAllowedEntry(IPlayer player, Position pos);

    public boolean isAllowedEntry(IPlayer player, Position pos, PlayerTeleportEvent.TeleportCause cause);

    public boolean isAllowedInteract(IPlayer player, Position pos);

    public boolean isAllowedInteract(IPlayer player, Position pos, int face, int action);

    public boolean isAllowedModification(IPlayer player, Position pos);
}
