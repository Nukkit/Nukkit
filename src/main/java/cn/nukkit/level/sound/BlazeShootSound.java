package cn.nukkit.level.sound;

import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.LevelEventPacket;

public class BlazeShootSound extends GenericSound{
	public BlazeShootSound(Vector3 pos){
		this(pos, 0);
	}

	public BlazeShootSound(Vector3 pos, float pitch){
		super(pos, LevelEventPacket.EVENT_SOUND_BLAZE_SHOOT, pitch);
	}
}
