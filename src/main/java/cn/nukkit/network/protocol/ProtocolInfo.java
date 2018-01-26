package cn.nukkit.network.protocol;

/**
 * author: MagicDroidX & iNevet
 * Nukkit Project
 */
public interface ProtocolInfo {

    /**
     * Actual Minecraft: PE protocol version
     */
    int CURRENT_PROTOCOL = Integer.valueOf("160"); //plugins can change it
    int MINIMUM_COMPATIBLE_PROTOCOL = Integer.parseInt("113");

    String MINECRAFT_VERSION = "v1.x.x";
    String MINECRAFT_VERSION_NETWORK = "1.2.9";

}
