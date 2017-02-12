/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.chat.ComponentSerializer
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package org.inventivetalent.bossbar;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.inventivetalent.bossbar.BossBar;
import org.inventivetalent.bossbar.BossBarAPI;
import org.inventivetalent.reflection.resolver.FieldResolver;
import org.inventivetalent.reflection.resolver.MethodResolver;
import org.inventivetalent.reflection.resolver.ResolverQuery;
import org.inventivetalent.reflection.resolver.minecraft.NMSClassResolver;

public class PacketBossBar
implements BossBar {
    static NMSClassResolver nmsClassResolver = new NMSClassResolver();
    static Class<?> PacketPlayOutBoss = nmsClassResolver.resolveSilent("PacketPlayOutBoss");
    static Class<?> PacketPlayOutBossAction = nmsClassResolver.resolveSilent("PacketPlayOutBoss$Action");
    static Class<?> ChatSerializer = nmsClassResolver.resolveSilent("ChatSerializer", "IChatBaseComponent$ChatSerializer");
    static Class<?> BossBattleBarColor = nmsClassResolver.resolveSilent("BossBattle$BarColor");
    static Class<?> BossBattleBarStyle = nmsClassResolver.resolveSilent("BossBattle$BarStyle");
    static FieldResolver PacketPlayOutBossFieldResolver = new FieldResolver(PacketPlayOutBoss);
    static MethodResolver ChatSerializerMethodResolver = new MethodResolver(ChatSerializer);
    private final UUID uuid = UUID.randomUUID();
    private Collection<Player> receivers = new ArrayList<Player>();
    private float progress;
    private String message;
    private BossBarAPI.Color color;
    private BossBarAPI.Style style;
    private boolean visible;
    private boolean darkenSky;
    private boolean playMusic;
    private boolean createFog;

    protected /* varargs */ PacketBossBar(String message, BossBarAPI.Color color, BossBarAPI.Style style, float progress, BossBarAPI.Property ... properties) {
        this.color = color != null ? color : BossBarAPI.Color.PURPLE;
        this.style = style != null ? style : BossBarAPI.Style.PROGRESS;
        this.setMessage(message);
        this.setProgress(progress);
        for (BossBarAPI.Property property : properties) {
            this.setProperty(property, true);
        }
    }

    protected /* varargs */ PacketBossBar(BaseComponent message, BossBarAPI.Color color, BossBarAPI.Style style, float progress, BossBarAPI.Property ... properties) {
        this(ComponentSerializer.toString((BaseComponent)message), color, style, progress, properties);
    }

    @Override
    public Collection<? extends Player> getPlayers() {
        return new ArrayList<Player>(this.receivers);
    }

    @Override
    public void addPlayer(Player player) {
        if (!this.receivers.contains((Object)player)) {
            this.receivers.add(player);
            this.sendPacket(0, player);
            BossBarAPI.addBarForPlayer(player, this);
        }
    }

    @Override
    public void removePlayer(Player player) {
        if (this.receivers.contains((Object)player)) {
            this.receivers.remove((Object)player);
            this.sendPacket(1, player);
            BossBarAPI.removeBarForPlayer(player, this);
        }
    }

    @Override
    public BossBarAPI.Color getColor() {
        return this.color;
    }

    @Override
    public void setColor(BossBarAPI.Color color) {
        if (color == null) {
            throw new IllegalArgumentException("color cannot be null");
        }
        if (color != this.color) {
            this.color = color;
            this.sendPacket(4, null);
        }
    }

    @Override
    public BossBarAPI.Style getStyle() {
        return this.style;
    }

    @Override
    public void setStyle(BossBarAPI.Style style) {
        if (style == null) {
            throw new IllegalArgumentException("style cannot be null");
        }
        if (style != this.style) {
            this.style = style;
            this.sendPacket(4, null);
        }
    }

    @Override
    public void setProperty(BossBarAPI.Property property, boolean flag) {
        switch (property) {
            case DARKEN_SKY: {
                this.darkenSky = flag;
                break;
            }
            case PLAY_MUSIC: {
                this.playMusic = flag;
                break;
            }
            case CREATE_FOG: {
                this.createFog = flag;
                break;
            }
        }
        this.sendPacket(5, null);
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public void setMessage(String message) {
        if (message == null) {
            throw new IllegalArgumentException("message cannot be null");
        }
        if (!message.startsWith("{") || !message.endsWith("}")) {
            throw new IllegalArgumentException("Invalid JSON");
        }
        if (!message.equals(this.message)) {
            this.message = message;
            this.sendPacket(3, null);
        }
    }

    @Override
    public float getProgress() {
        return this.progress;
    }

    @Override
    public void setProgress(float progress) {
        if (progress > 1.0f) {
            progress /= 100.0f;
        }
        if (progress != this.progress) {
            this.progress = progress;
            this.sendPacket(2, null);
        }
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public void setVisible(boolean flag) {
        if (flag != this.visible) {
            this.visible = flag;
            this.sendPacket(flag ? 0 : 1, null);
        }
    }

    void sendPacket(int action, Player player) {
        try {
            Object packet = PacketPlayOutBoss.newInstance();
            PacketPlayOutBossFieldResolver.resolve("a").set(packet, this.uuid);
            PacketPlayOutBossFieldResolver.resolve("b").set(packet, PacketPlayOutBossAction.getEnumConstants()[action]);
            PacketPlayOutBossFieldResolver.resolve("c").set(packet, PacketBossBar.serialize(this.message));
            PacketPlayOutBossFieldResolver.resolve("d").set(packet, Float.valueOf(this.progress));
            PacketPlayOutBossFieldResolver.resolve("e").set(packet, BossBattleBarColor.getEnumConstants()[this.color.ordinal()]);
            PacketPlayOutBossFieldResolver.resolve("f").set(packet, BossBattleBarStyle.getEnumConstants()[this.style.ordinal()]);
            PacketPlayOutBossFieldResolver.resolve("g").set(packet, this.darkenSky);
            PacketPlayOutBossFieldResolver.resolve("h").set(packet, this.playMusic);
            PacketPlayOutBossFieldResolver.resolve("i").set(packet, this.createFog);
            if (player != null) {
                BossBarAPI.sendPacket(player, packet);
            } else {
                for (Player player1 : this.getPlayers()) {
                    BossBarAPI.sendPacket(player1, packet);
                }
            }
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public float getMaxHealth() {
        return 100.0f;
    }

    @Override
    public void setHealth(float percentage) {
        this.setProgress(percentage / 100.0f);
    }

    @Override
    public float getHealth() {
        return this.getProgress() * 100.0f;
    }

    @Override
    public Player getReceiver() {
        return null;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public void updateMovement() {
    }

    static Object serialize(String json) throws ReflectiveOperationException {
        return ChatSerializerMethodResolver.resolve(new ResolverQuery("a", String.class)).invoke(null, json);
    }

}

