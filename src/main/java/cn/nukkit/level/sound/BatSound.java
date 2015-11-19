package cn.nukkit.level.sound;

import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.LevelEventPacket;

public class BatSound extends GenericSound{
	public BatSound(Vector3 pos){
		this(pos, 0);
	}

	public BatSound(Vector3 pos, float pitch){
		super(pos, LevelEventPacket.EVENT_SOUND_BAT_FLY, pitch);
	}
}
