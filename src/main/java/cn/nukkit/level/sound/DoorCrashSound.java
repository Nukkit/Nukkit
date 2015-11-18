package cn.nukkit.level.sound;

import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.LevelEventPacket;

public class DoorCrashSound extends GenericSound{
	public DoorCrashSound(Vector3 pos){
		this(pos, 0);
	}

	public DoorCrashSound(Vector3 pos, float pitch){
		super(pos, LevelEventPacket.EVENT_SOUND_DOOR_CRASH, pitch);
	}
}
