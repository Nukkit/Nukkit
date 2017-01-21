package cn.nukkit;

import cn.nukkit.metadata.Metadatable;
import cn.nukkit.permission.ServerOperator;

/**
 * 用来描述一个玩家和?得?个玩家相?信息的接口。<br>
 * An interface to describe a player and get its information.
 * <p>
 * <p>?个玩家可以在?，也可以是不在?。<br>
 * This player can be online or offline.</p>
 *
 * @author MagicDroidX(code) @ Nukkit Project
 * @author 粉鞋大?(javadoc) @ Nukkit Project
 * @see cn.nukkit.Player
 * @see cn.nukkit.OfflinePlayer
 * @since Nukkit 1.0 | Nukkit API 1.0.0
 */
public interface IPlayer extends ServerOperator, Metadatable {

    /**
     * 返回?个玩家是否在?。<br>
     * Returns if this player is online.
     *
     * @return ?个玩家是否在?。<br>If this player is online.
     * @since Nukkit 1.0 | Nukkit API 1.0.0
     */
    boolean isOnline();

    /**
     * 返回?个玩家的名称。<br>
     * Returns the name of this player.
     * <p>
     * <p>如果是在?的玩家，?个函数只会返回登?名字。如果要返回?示的名字，参?{@link cn.nukkit.Player#getDisplayName}<br>
     * Notice that this will only return its login name. If you need its display name, turn to
     * {@link cn.nukkit.Player#getDisplayName}</p>
     *
     * @return ?个玩家的名称。<br>The name of this player.
     * @since Nukkit 1.0 | Nukkit API 1.0.0
     */
    String getName();

    /**
     * 返回?个玩家是否已加入白名?。<br>
     * Returns if this player is pardoned by whitelist.
     *
     * @return ?个玩家是否已加入白名?。<br>If this player is pardoned by whitelist.
     * @see cn.nukkit.Server#isWhitelisted
     * @since Nukkit 1.0 | Nukkit API 1.0.0
     */
    boolean isWhitelisted();

    /**
     * 把?个玩家加入白名?，或者取消?个玩家的白名?。<br>
     * Adds this player to the white list, or removes it from the whitelist.
     *
     * @param value 如果?{@code true}，把玩家加入白名?。如果?{@code false}，取消?个玩家的白名?。<br>
     *              {@code true} for add and {@code false} for remove.
     * @see #isWhitelisted
     * @see cn.nukkit.Server#addWhitelist
     * @see cn.nukkit.Server#removeWhitelist
     * @since Nukkit 1.0 | Nukkit API 1.0.0
     */
    void setWhitelisted(boolean value);

    /**
     * 得到?个接口的{@code Player}?象。<br>
     * Returns a {@code Player} object for this interface.
     *
     * @return ?个接口的 {@code Player}?象。<br>a {@code Player} object for this interface.
     * @see cn.nukkit.Server#getPlayerExact
     * @since Nukkit 1.0 | Nukkit API 1.0.0
     */
    Player getPlayer();

    /**
     * 返回玩家所在的服?器。<br>
     * Returns the server carrying this player.
     *
     * @return 玩家所在的服?器。<br>the server carrying this player.
     * @since Nukkit 1.0 | Nukkit API 1.0.0
     */
    Server getServer();

    /**
     * 得到?个玩家第一次游?的??。<br>
     * Returns the time this player first played in this server.
     *
     * @return ?个玩家第一次游?的??。<br>The time this player first played in this server.
     * @since Nukkit 1.0 | Nukkit API 1.0.0
     */
    Long getFirstPlayed();

    /**
     * 得到?个玩家上次加入游?的??。<br>
     * Returns the time this player last joined in this server.
     *
     * @return ?个玩家上次游?的??。<br>The time this player last joined in this server.
     * @since Nukkit 1.0 | Nukkit API 1.0.0
     */
    Long getLastPlayed();

    /**
     * 返回?个玩家以前是否来?服?器。<br>
     * Returns if this player has played in this server before.
     * <p>
     * <p>如果想得到?个玩家是不是第一次玩，可以使用：<br>
     * If you want to know if this player is the first time playing in this server, you can use:<br>
     * <pre>if(!player.hasPlayerBefore()) {...}</pre></p>
     *
     * @return ?个玩家以前是不是玩?游?。<br>If this player has played in this server before.
     * @since Nukkit 1.0 | Nukkit API 1.0.0
     */
    boolean hasPlayedBefore();

}
