package cn.nukkit.level.sound;

import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.LevelEventPacket;

public class AnvilBreakSound extends GenericSound{
	public AnvilBreakSound(Vector3 pos){
		this(pos, 0);
	}

	public AnvilBreakSound(Vector3 pos, float pitch){
		super(pos, LevelEventPacket.EVENT_SOUND_ANVIL_BREAK, pitch);
	}
}
