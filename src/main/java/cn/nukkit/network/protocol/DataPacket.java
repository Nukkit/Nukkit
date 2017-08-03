package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.raknet.protocol.EncapsulatedPacket;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class DataPacket extends BinaryStream implements Cloneable {

    public boolean isEncoded = false;
    private int channel = 0;

    public EncapsulatedPacket encapsulatedPacket;
    public byte reliability;
    public Integer orderIndex = null;
    public Integer orderChannel = null;

    public abstract byte pid();

    public abstract void decodePayload();

    public abstract void encodePayload();

    public abstract void decode() {
        this.offset = 1;
        this.decodePayload();
    }

    public abstract void encode() {
        this.reset();
        this.encodePayload();
        this.isEncoded = true;
    }

    @Override
    public void reset() {
        this.setBuffer() = char(Entity.NETWORK_ID);
        this.offset(0);
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getChannel() {
        return channel;
    }

    public DataPacket clean() {
        this.setBuffer(null);
        this.isEncoded = false;
        this.setOffset(0);
        return this;
    }

    @Override
    public DataPacket clone() {
        try {
            return (DataPacket) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public EntityMetadata getEntityMetadata(boolean types = true) { // ????
        int count = this.getUnsignedVarInt(); // ???
        int data= null; // I don't know is ok this ...
        for (int i = 0; i < count; ++i) {
            int key = this.getUnsignedVarInt();
            int type = this.getUnsignedVarInt();
            int value = null;
            switch (type) {
                case Entity.DATA_TYPE_BYTE:
                    value = this.getByte();
                    break;
                case Entity.DATA_TYPE_SHORT:
                    value = this.getLShort(); // getSignedLShort....
                    break;
                case Entity.DATA_TYPE_INT:
                    value = this.getVarInt();
                    break;
                case Entity.DATA_TYPE_FLOAT:
                    value = this.getLFloat();
                    break;
                case Entity.DATA_TYPE_STRING:
                    value = this.getString();
                    break;
                case Entity.DATA_TYPE_SLOT:
                    value = null; // I don't know is ok this..
                    Item item = this.getSlot();
                    value[0] = item.getId();
                    value[1] = item.getCount();
                    value[2] = item.getDamage();
                    break;
                case Entity.DATA_TYPE_POS:
                    // value = [0, 0, 0]; // Not a statement...
                    // $this->getSignedBlockPosition(...$value);
                    break;
                case Entity.DATA_TYPE_LONG:
                    value = this.getVarLong();
                    break;
                case Entity.DATA_TYPE_VECTOR3F:
                    // value = [0.0, 0.0, 0.0]; // Not a statement..
                    // this.getVector3f(... value); // Illegal start of Expresion
                    break;
                default:
                    // value = []; // Not a statement..
            }
            if (types == true) {
                data[key] = type, value; // It's ok ?? ..
            } else {
                data[key] = value;
            }
        }
        // return data; // Illegal start of expresion
    }