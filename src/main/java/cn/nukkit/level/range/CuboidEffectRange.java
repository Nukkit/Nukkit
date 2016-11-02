package cn.nukkit.level.range;

import cn.nukkit.math.BlockVector3;

public class CuboidEffectRange extends EffectRangeImpl {
	private int tx;
	private int ty;
	private int tz;
	private int bx;
	private int by;
	private int bz;

	public CuboidEffectRange(BlockVector3 bottom, BlockVector3 top) {
		this(bottom.getX(), bottom.getY(), bottom.getZ(), top.getX(), top.getY(), top.getZ());
	}

	public CuboidEffectRange(int bx, int by, int bz, int tx, int ty, int tz) {
		super(tx, ty, tz, bx, by, bz);
		this.tx = tx;
		this.ty = ty;
		this.tz = tz;
		this.bx = bx;
		this.by = by;
		this.bz = bz;
	}

	@Override
	public void initEffectIterator(EffectIterator i) {
		i.resetAsCubicInterator(bx, by, bz, tx, ty, tz);
	}

	@Override
	public EffectRange translate(BlockVector3 offset) {
		BlockVector3 bottom = new BlockVector3(this.bx, this.by, this.bz);
		BlockVector3 top = new BlockVector3(this.tx, this.ty, this.tz);
		bottom.add(offset);
		top.add(offset);
		return new CuboidEffectRange(bottom, top);
	}
}
