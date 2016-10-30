package cn.nukkit.network.protocol;

/**
 * Created on 15-10-13.
 */
public class TextPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.TEXT_PACKET;

    public TextPacket() {
        super(null);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static final byte TYPE_RAW = 0;
    public static final byte TYPE_CHAT = 1;
    public static final byte TYPE_TRANSLATION = 2;
    public static final byte TYPE_POPUP = 3;
    public static final byte TYPE_TIP = 4;
    public static final byte TYPE_SYSTEM = 5;
    public static final byte TYPE_WHISPER = 6;

    public byte type;
    public String source = "";
    public String message = "";
    public String[] parameters = new String[0];

    @Override
    public void decode() {
        this.type = (byte) getByte();
        switch (type) {
            case TYPE_POPUP:
            case TYPE_CHAT:
                this.source = this.getString();
            case TYPE_RAW:
            case TYPE_TIP:
            case TYPE_SYSTEM:
                this.message = this.getString();
                break;

            case TYPE_TRANSLATION:
                this.message = this.getString();
                int count = this.getByte();
                parameters = new String[count];
                for (int i = 0; i < count; i++) {
                    parameters[i] = getString();
                }
        }
    }

    @Override
    public void encode() {
        switch (this.type) {
            case TYPE_POPUP:
            case TYPE_CHAT:
                setBuffer(new byte[6 + source.length() + message.length()]);
                this.reset();
                this.putByte(this.type);
                this.putString(this.source);
                this.putString(this.message);
                break;
            case TYPE_RAW:
            case TYPE_TIP:
            case TYPE_SYSTEM:
                setBuffer(new byte[4 + message.length()]);
                this.reset();
                this.putByte(this.type);
                this.putString(this.message);
                break;

            case TYPE_TRANSLATION:
                int size = 6 + message.length();
                for (String parameter : this.parameters) {
                    size += 2 + parameter.length();
                }
                setBuffer(new byte[size]);
                this.reset();
                this.putByte(this.type);
                this.putString(this.message);
                this.putByte((byte) this.parameters.length);
                for (String parameter : this.parameters) {
                    this.putString(parameter);
                }
                break;
        }
    }

}
