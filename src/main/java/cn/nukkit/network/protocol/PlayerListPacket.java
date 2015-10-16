package cn.nukkit.network.protocol;

import java.util.UUID;

/**
 * PlayerListPacket implementation.
 *
 * @author Nukkit Team
 */
public class PlayerListPacket extends DataPacket {

    public static final byte NETWORK_ID = Info.PLAYER_LIST_PACKET;

    public static final byte TYPE_ADD = 0;
    public static final byte TYPE_REMOVE = 1;

    public byte type;
    public Entry[] entries;

    @Override
    public DataPacket clean() {
        this.entries = null;
        return super.clean();
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte(type);
        this.putInt(entries.length);
        for(Entry entry : entries) {
            if(this.type == TYPE_ADD) {
                this.putUUID(entry.uuid);
                this.putLong(entry.entityID);
                this.putString(entry.name);
                this.putByte((byte) (entry.isSlim ? 1 : 0));
                this.putShort((short) entry.skin.length);
                this.put(entry.skin);
            } else {
                this.putUUID(entry.uuid);
            }
        }
    }

    @Override
    public void decode() {

    }

    public static class Entry {
        private final UUID uuid;
        private long entityID;
        private String name;
        private boolean isSlim;
        private byte[] skin; //I store skin in bytes due to strange things with storing in a java.lang.String

        /**
         * Create a new entry for REMOVAL.
         * @param uuid The player's uuid.
         */
        public Entry(UUID uuid) {
            this.uuid = uuid;
        }

        /**
         * Create a new entry for ADDITION.
         * @param uuid The player's uuid.
         * @param entiryID The player's entityID.
         * @param name The player's name.
         * @param isSlim If the player's skin is slim.
         * @param skin The player's skin.
         */
        public Entry(UUID uuid, long entiryID, String name, boolean isSlim, byte[] skin) {
            this.uuid = uuid;
            this.entityID = entiryID;
            this.isSlim = isSlim;
            this.skin = skin;
        }
    }
}
