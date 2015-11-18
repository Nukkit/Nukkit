package cn.nukkit.level.sound;

import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.LevelEventPacket;

public class DoorBumpSound extends GenericSound{
	public DoorBumpSound(Vector3 pos){
		this(pos, 0);
	}

	public DoorBumpSound(Vector3 pos, float pitch){
		super(pos, LevelEventPacket.EVENT_SOUND_DOOR_BUMP, pitch);
	}
}
