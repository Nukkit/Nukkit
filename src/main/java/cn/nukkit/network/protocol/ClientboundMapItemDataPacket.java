package cn.nukkit.network.protocol;

import cn.nukkit.Server;
import cn.nukkit.utils.MapInfo;

/**
 * author: boybook
 * Nukkit Project
 */

public class ClientboundMapItemDataPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.CLIENTBOUND_MAP_ITEM_DATA_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public MapInfo mapInfo;

    @Override
    public void decode() {
        this.reset();
        MapInfo map = new MapInfo();

        map.mapId = getLong();
        byte[] readBytes = get(3);
        //Log.Warn($"{HexDump(readBytes)}");
        map.updateType = getSignedByte(); //
        byte[] bytes = get(6);
        //Log.Warn($"{HexDump(bytes)}");

        map.direction = getSignedByte(); //
        map.x = getSignedByte(); //
        map.z = getSignedByte(); //

        if (map.updateType == 0) {
            // Full map
            try {
                if (bytes[4] == 1) {
                    map.col = getInt();
                    map.row = getInt(); //

                    map.xOffset = getInt(); //
                    map.zOffset = getInt(); //

                    map.data = get(map.col * map.row * 4);
                }
            }
            catch (Exception e) {
                Server.getInstance().getLogger().error("Error while reading map data for map={map}", e);
            }
        } else if (map.updateType == 4) {
            // Map update
        } else {
            Server.getInstance().getLogger().warning("Unknown map-type 0x{map.UpdateType:X2}");
        }

        this.mapInfo = map;
    }

    @Override
    public void encode() {
        this.reset();
        this.putLong(this.mapInfo.mapId);
        this.put(new byte[3]);
        this.putByte(this.mapInfo.updateType);
        this.put(new byte[4]);
        this.putByte((byte) 1);
        this.putByte((byte) 0);
        this.putByte(this.mapInfo.direction);
        this.putInt(this.mapInfo.x);
        this.putInt(this.mapInfo.z);
        if (this.mapInfo.updateType == 0) {
            // Full map
            this.putInt(this.mapInfo.col);
            this.putInt(this.mapInfo.row);
            this.putInt(this.mapInfo.xOffset);
            this.putInt(this.mapInfo.zOffset);
            this.put(this.mapInfo.data);
        }
        else if (this.mapInfo.updateType == 4) {
            // Map update
        }
        else {
            Server.getInstance().getLogger().warning("Tried to send unknown map-type 0x{this.mapInfo.UpdateType:X2}");
        }
    }

}
