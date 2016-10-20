package cn.nukkit.service;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.nukkit.permission.PermissionAttachment;
import cn.nukkit.permission.PermissionAttachmentInfo;
import cn.nukkit.plugin.Plugin;

public class DefaultPermissionService implements PermissionService {
    private final Plugin plugin;

    public DefaultPermissionService(Plugin plugin) {
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

    public boolean has(IPlayer player, Level level, String perm) {
        if (!(player instanceof Player)) {
            return false;
        }
        return ((Player) player).hasPermission(perm);
    }

    public boolean isSet(IPlayer player, Level level, String perm) {
        if (!(player instanceof Player)) {
            return false;
        }
        return ((Player) player).isPermissionSet(perm);
    }

    public boolean add(IPlayer player, Level level, String perm, boolean value) {
        return false;
    }

    public boolean remove(IPlayer player, Level level, String perm) {
        return false;
    }


    public boolean addTransient(IPlayer player, String permission, boolean value) {
        if (player instanceof Player) {
            return addTransient((Player) player, permission, value);
        } else {
            return false;
        }
    }

    public boolean addTransient(Player player, String permission, boolean value) {
        for (PermissionAttachmentInfo paInfo : player.getEffectivePermissions().values()) {
            if (paInfo.getAttachment() != null && paInfo.getAttachment().getPlugin().equals(plugin)) {
                paInfo.getAttachment().setPermission(permission, value);
                return true;
            }
        }
        PermissionAttachment attach = player.addAttachment(plugin);
        attach.setPermission(permission, value);
        return true;
    }

    public boolean removeTransient(IPlayer player, String permission) {
        if (player instanceof Player) {
            return removeTransient((Player) player, permission);
        } else {
            return false;
        }
    }

    public boolean removeTransient(Player player, String permission) {
        for (PermissionAttachmentInfo paInfo : player.getEffectivePermissions().values()) {
            if (paInfo.getAttachment() != null && paInfo.getAttachment().getPlugin().equals(plugin)) {
                paInfo.getAttachment().unsetPermission(permission, true);
                return true;
            }
        }
        return false;
    }

    public boolean groupHas(String group, Level level, String permission) {
        return false;
    }

    public boolean groupAdd(String group, Level level, String permission) {
        return false;
    }

    public boolean groupRemove(String group, Level level, String permission) {
        return false;
    }

    public boolean playerInGroup(IPlayer player, Level level, String group) {
        if (player instanceof Player) {
            return ((Player) player).hasPermission("group." + group);
        } else {
            return false;
        }
    }

    public boolean playerAddGroup(IPlayer player, Level level, String group) {
        return false;
    }

    public boolean playerRemoveGroup(IPlayer player, Level level, String group) {
        return false;
    }

    public String[] getPlayerGroups(IPlayer player, Level level) {
        return new String[]{};
    }

    public String getPrimaryGroup(IPlayer player, Level level) {
        return null;
    }

    public String[] getGroups() {
        return new String[]{};
    }
}