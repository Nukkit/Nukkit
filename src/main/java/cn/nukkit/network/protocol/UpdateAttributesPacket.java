package cn.nukkit.network.protocol;

import cn.nukkit.entity.Attribute;

/**
 * @author Nukkit Project Team
 */
public class UpdateAttributesPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.UPDATE_ATTRIBUTES_PACKET;

    public Attribute[] entries;
    public long entityId;

    public UpdateAttributesPacket() {
        super(null);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public void decode() {

    }

    public void encode() {
        if (this.entries == null) {
            setBuffer(new byte[11]);
            this.reset();
            this.putLong(this.entityId);
            this.putShort(0);
        } else {
            int size = 11;
            for (Attribute entry : this.entries) {
                size += 14 + entry.getName().length();
            }
            setBuffer(new byte[size]);
            this.reset();
            this.putLong(this.entityId);
            this.putShort(this.entries.length);
            for (Attribute entry : this.entries) {
                this.putFloat(entry.getMinValue());
                this.putFloat(entry.getMaxValue());
                this.putFloat(entry.getValue());
                this.putString(entry.getName());
            }
        }
    }

}
