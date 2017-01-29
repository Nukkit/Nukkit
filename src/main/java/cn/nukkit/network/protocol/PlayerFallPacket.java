package cn.nukkit.network.protocol;

public class PlayerFallPacket extends DataPacket {
	public float fallDistance;
	
	@Override
	public byte pid() {
		return ProtocolInfo.PLAYER_FALL_PACKET;
	}

	@Override
	public void decode() {
		fallDistance = this.getLFloat();
	}

	@Override
	public void encode() {

	}

}
