package cn.nukkit.service;

import cn.nukkit.IPlayer;
import cn.nukkit.level.Level;
import cn.nukkit.plugin.Plugin;
import java.util.Collection;

public interface EconomyService extends Service{
    public Plugin getPlugin();

    public boolean isEnabled();

    public String getName();

    public String getCurrency();

    public boolean hasAccount(IPlayer player, Level level);

    public double getBalance(IPlayer player, Level level);

    public boolean has(IPlayer player, Level level, double amount);

    public boolean withdraw(IPlayer player, Level level, double amount);

    public boolean deposit(IPlayer player, Level level, double amount);

    public Collection<IPlayer> getAccounts() ;
}
