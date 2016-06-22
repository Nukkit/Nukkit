package cn.nukkit.entity.imageproviders;

import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.ClientboundMapItemDataPacket;
import cn.nukkit.utils.MapInfo;

/**
 * author: boybook
 * Nukkit Project
 */

public interface IMapImageProvider {

    byte[] getData(MapInfo mapInfo, boolean forced);

}
