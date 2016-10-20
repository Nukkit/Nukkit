package cn.nukkit.service;

import cn.nukkit.IPlayer;
import cn.nukkit.level.Level;
import cn.nukkit.plugin.Plugin;
import java.util.ArrayList;
import java.util.Collection;

public class DefaultEconomyService implements EconomyService {
    private final Plugin plugin;

    public DefaultEconomyService(Plugin plugin) {
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

    public String getCurrency() {
        return "$";
    }

    public boolean hasAccount(IPlayer player, Level level) {
        return getBalance(player, level) != 0;
    }

    public double getBalance(IPlayer player, Level level) {
        return 0;
    }

    public boolean has(IPlayer player, Level level, double amount) {
        return getBalance(player, level) > amount;
    }

    public boolean withdraw(IPlayer player, Level level, double amount) {
        return false;
    }

    public boolean deposit(IPlayer player, Level level, double amount) {
        return false;
    }

    public Collection<IPlayer> getAccounts() {
        return new ArrayList<>();
    }
}