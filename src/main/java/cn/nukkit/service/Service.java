package cn.nukkit.service;

import cn.nukkit.plugin.Plugin;

public interface Service {
    public Plugin getPlugin();

    public boolean isEnabled();

    public String getName();
}
