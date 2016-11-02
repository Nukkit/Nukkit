package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Binary;

import java.util.UUID;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class AddPlayerPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.ADD_PLAYER_PACKET;

    public AddPlayerPacket() {
        super(null);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public UUID uuid;
    public String username;
    public long entityUniqueId;
    public long entityRuntimeId;
    public float x;
    public float y;
    public float z;
    public float speedX;
    public float speedY;
    public float speedZ;
    public float pitch;
    public float yaw;
    public Item item;
    public EntityMetadata metadata = new EntityMetadata();

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        int slot = this.item == null ? 2 : 7;
        byte[] meta = Binary.writeMetadata(this.metadata);
        setBuffer(new byte[63 + username.length() + slot + meta.length]);
        this.reset();
        this.putUUID(this.uuid);
        this.putString(this.username);
        this.putEntityId(this.entityUniqueId);
        this.putEntityId(this.entityRuntimeId);
        this.putVector3f(x, y, z);
        this.putVector3f(speedX, speedY, speedZ);
        this.putLFloat(this.pitch);
        this.putLFloat(this.yaw); //TODO headrot
        this.putLFloat(this.yaw);
        this.putSlot(this.item);

        this.put(meta);
    }
}
