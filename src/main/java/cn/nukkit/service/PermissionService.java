package cn.nukkit.service;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.nukkit.plugin.Plugin;

public interface PermissionService extends Service {
    public Plugin getPlugin();

    public boolean isEnabled();

    public String getName();

    public boolean has(IPlayer player, Level level, String perm);

    public boolean isSet(IPlayer player, Level level, String perm);

    public boolean add(IPlayer player, Level level, String perm, boolean value);

    public boolean remove(IPlayer player, Level level, String perm);

    public boolean addTransient(IPlayer player, String permission, boolean value);

    public boolean addTransient(Player player, String permission, boolean value);

    public boolean removeTransient(IPlayer player, String permission);

    public boolean removeTransient(Player player, String permission);

    public boolean groupHas(String group, Level level, String permission);

    public boolean groupAdd(String group, Level level, String permission);

    public boolean groupRemove(String group, Level level, String permission);

    public boolean playerInGroup(IPlayer player, Level level, String group);

    public boolean playerAddGroup(IPlayer player, Level level, String group);

    public boolean playerRemoveGroup(IPlayer player, Level level, String group);

    public String[] getPlayerGroups(IPlayer player, Level level);

    public String getPrimaryGroup(IPlayer player, Level level);

    public String[] getGroups();
}
