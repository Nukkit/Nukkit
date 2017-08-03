package cn.nukkit.network.protocol;

import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.data.StringEntityData;
import cn.nukkit.utils.Binary;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class AddEntityPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.ADD_ENTITY_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public int entityUniqueId = null;
    public int entityRuntimeId;
    public int type;
    public float x;
    public float y;
    public float z;
    public float speedX = 0f;
    public float speedY = 0f;
    public float speedZ = 0f;
    public float yaw = 0f;
    public float pitch = 0f;
    public EntityMetadata metadata = new EntityMetadata();
    public Attribute[] attributes = new Attribute[0];
    public final Object[][] links = new Object[0][3];

    @Override
    public void decodePayload() {
        this.entityUniqueId = this.entityUniqueId();
        this.entityRuntimeId = this.entityRuntimeId();
        this.type = this.getUnsignedVarInt();
        this.getVector3f(this.x, this.y, this.z);
        this.getVector3f(this.speedX, this.speedY, this.speedZ);
        this.pitch = this.getLFloat();
        this.yaw = this.getLFloat();

        int attrCount = this.getUnsignedVarInt();
        for (int i = 0; i < attrCount; ++i) {
            String name = this.getString();
            float min = this.getLFloat();
            int current = this.getLFloat();
            int max = this.getLFloat();
            Attribute attr = Attribute.getAttributeByName(name);

            if (attr != null) {
                attr.getMinValue(min);
                attr.getMaxValue(max);
                attr.setValue(current);
                this.attributes[] = attr;
            } else {
                throw new IllegalArgumentException("Unknown attribute type.");
            }
        }

        this.metadata = this.getEntityMetadata();
        int linkCount = this.getUnsignedVarInt();
        for (int i = 0; i < linkCount; ++i) {
            this.links[i][0] = this.entityUniqueId();
            this.links[i][1] = this.entityUniqueId();
            this.links[i][3] = this.getByte();
        }
    }

    @Override
    public void encodePayload() {
        this.putVarLong(this.entityUniqueId);
        this.putVarLong(this.entityRuntimeId);
        this.putUnsignedVarInt(this.type);
        this.putVector3f(this.x, this.y, this.z);
        this.putVector3f(this.speedX, this.speedY, this.speedZ);
        this.putLFloat(this.pitch);
        this.putLFloat(this.yaw);
        this.putAttributeList(this.attributes);
        this.put(Binary.writeMetadata(this.metadata));
        this.putUnsignedVarInt(this.links.length);
        for (Object[] link : this.links) {
            this.putVarLong((long) link[0]);
            this.putVarLong((long) link[1]);
            this.putByte((byte) link[2]);
        }
    }
}
