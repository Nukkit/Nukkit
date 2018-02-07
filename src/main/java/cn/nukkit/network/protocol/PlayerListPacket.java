package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.Skin;

import java.util.UUID;

/**
 * @author Nukkit Project Team
 */
public class PlayerListPacket extends DataPacket {

    public static final byte TYPE_ADD = 0;
    public static final byte TYPE_REMOVE = 1;

    public byte type;
    public Entry[] entries = new Entry[0];

    @Override
    public byte pid(PlayerProtocol protocol) {
        return protocol.getPacketId("PLAYER_LIST_PACKET");
    }

    @Override
    public void decode(PlayerProtocol protocol) {

    }

    @Override
    public void encode(PlayerProtocol protocol) {
        this.reset(protocol);
        this.putByte(this.type);
        this.putUnsignedVarInt(this.entries.length);
        for (Entry entry : this.entries) {
            if (type == TYPE_ADD) {
                this.putUUID(entry.uuid, protocol);
                this.putVarLong(entry.entityId);
                this.putString(entry.name);
                if (protocol.getNumber() >= 200){
                    this.putString(entry.thirdPartyName);
                    this.putVarInt(entry.platformId);
                }
                this.putSkin(entry.skin, protocol);
                if (protocol.getMainNumber() >= 130){
                    if (protocol.getNumber() >= 200) this.putBoolean(entry.skin.getCape().getData().length != 0);
                    this.putByteArray(entry.skin.getCape().getData());
                    this.putString(entry.geometryModel);
                    this.putByteArray(entry.geometryData);
                    this.putString(entry.xboxUserId);
                    this.putString(entry.platformChatId);
                }
            } else {
                this.putUUID(entry.uuid, protocol);
            }
        }

    }

    public static class Entry {

        public final UUID uuid;
        public long entityId = 0;
        public String name = "";
        public String thirdPartyName = ""; //TODO
        public int platformId = 0; //TODO
        public Skin skin;
        public byte[] capeData = new byte[0]; //TODO
        public String geometryModel = "";
        public byte[] geometryData = new byte[0]; //TODO
        public String xboxUserId = ""; //TODO
        public String platformChatId = ""; //TODO

        public Entry(UUID uuid) {
            this.uuid = uuid;
        }

        public Entry(UUID uuid, long entityId, String name, Skin skin) {
            this(uuid, entityId, name, skin, "");
        }

        public Entry(UUID uuid, long entityId, String name, Skin skin, String xboxUserId) {
            this(uuid, entityId, name, skin, xboxUserId, "", 0, "");
        }

        public Entry(UUID uuid, long entityId, String name, Skin skin, String xboxUserId, String thirdPartyName, int platformId, String platformChatId){
            this.uuid = uuid;
            this.entityId = entityId;
            this.name = name;
            this.thirdPartyName = thirdPartyName;
            this.platformId = platformId;
            this.skin = skin;
            this.capeData = skin.getCape().getData();
            this.xboxUserId = xboxUserId == null ? "" : xboxUserId;
            this.platformChatId = platformChatId;
        }
    }

}
