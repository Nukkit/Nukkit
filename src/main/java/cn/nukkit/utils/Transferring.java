package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.TransferPacket;

import java.net.InetSocketAddress;

/**
 * Copyright 2017 lmlstarqaq
 * All rights reserved.
 * <p>
 * Tool for plugins, that helps transfer to another server
 * <p>
 * Tips: transferPlayerTo method should be called after
 * player login is done, and the login event shouldn't be cancelled.
 * For example:
 * <pre>
 * {@code @EventHandler}
 *  public void onPlayerLogin(PlayerLoginEvent e) {
 *      Server.getInstance().getScheduler().scheduleDelayedTask(() ->
 *          Transferring.transferPlayerTo(e.getPlayer(), "192.168.1.102", 19133)
 *          , 20);
 *  }
 * </pre>
 */
public final class Transferring {
    private Transferring() {}

    public static void transferPlayerTo(Player player, InetSocketAddress address) {
        transfer0(player, address.getHostName(), address.getPort());
    }

    public static void transferPlayerTo(Player player, String addressString, int port) {
        transfer0(player, addressString, port);
    }

    private static void transfer0(Player player, String address, int port) {
        TransferPacket pk = new TransferPacket();
        pk.address = address;
        pk.port = port;
        player.dataPacket(pk);

        player.close("transferred to "+address+":"+port+" .", "transferred to "+address+":"+port+" .", false);
    }

}
