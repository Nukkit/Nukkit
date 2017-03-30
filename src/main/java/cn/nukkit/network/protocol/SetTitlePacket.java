package cn.nukkit.network.protocol;

/**
 * author: Jons[Hunyahemee]
 * Nukkit Project
 */
public class SetTitlePacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.SET_TITLE_PACKET;
    
    public static final int RESET = 0;
    public static final int CLEAR = 1;
    public static final int TITLE = 2;
    public static final int SUBTITLE = 3;
    public static final int ACTIONBAR = 4;
    public static final int ANIMATIONTIMES = 5;
    
    public string text;
    public int type;
    public int fadeInTime;
    public int stayTime;
    public int fadeOutTime;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
    
    this.type = this.getVarInt();
    this.text = this.getString();
    this.fadeInTime = this.getVarInt();
    this.stayTime = this.getVarInt();
    this.fadeOutTime = this.getVarInt();
    
	}

     @Override
     public void encode(){
    
     this.reset();
     this.putVarInt(this.type);
     this.putString(this.text);
     this.putVarInt(this.fadeInTime);
     this.putVarInt(this.stayTime);
     this.putVarInt(this.fadeOutTime);
    
    }
}
