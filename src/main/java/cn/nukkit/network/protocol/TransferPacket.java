package cn.nukkit.network.protocol;

public class TransferPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.TRANSFER_PACKET;
    
    public String address;
    public short port;
    
    @Override
    public void decode() {
        this.address = this.getString();
        this.port = this.getLShort();
        System.out.print("Client tries to join from Server" + this.address + this.port);
    }
    
    @Override
    public void encode() {
        this.reset();
        this.putString(this.address);
        this.putLShort(this.port);
        //Here the xbox settings would follow. We already ignore those in the @link StartPacket
    }
}
