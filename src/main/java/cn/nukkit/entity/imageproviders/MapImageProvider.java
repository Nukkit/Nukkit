package cn.nukkit.entity.imageproviders;

import cn.nukkit.utils.MapInfo;

/**
 * author: boybook
 * Nukkit Project
 */

public abstract class MapImageProvider implements IMapImageProvider {

    private byte[] mapData;

    public byte[] getData(MapInfo mapInfo, boolean forced) {
        if (this.mapData == null) this.mapData = this.generateColors(mapInfo);

        if (mapData.length != (mapInfo.col * mapInfo.row * 4)) return null;

        return this.mapData;
    }

    protected abstract byte[] generateColors(MapInfo map);

}
