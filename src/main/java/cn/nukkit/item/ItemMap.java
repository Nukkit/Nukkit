package cn.nukkit.item;

import cn.nukkit.Server;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * author: boybook
 * Nukkit Project
 */
public class ItemMap extends Item {

    public ItemMap(long mapId) {
        this(mapId, 1);
    }

    public ItemMap(long mapId, int count) {
        super(MAP, 0, count, "Map");
    }

    public long getMapId() {
        if (this.getNamedTag() == null) return 0;
        return Long.parseLong(this.getNamedTag().getString("map_uuid"));
    }

    public void setMapId(long mapId) {
        this.setNamedTag(new CompoundTag("mapData").putString("map_uuid", String.valueOf(mapId)));
        Server.getInstance().getLogger().debug("ItemMap setMapId = " + mapId);
    }

}
