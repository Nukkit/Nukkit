package cn.nukkit.level.range;

/**
 * Nukkit Project
 */
public class CubicEffectRange extends EffectRangeImpl {
	private final int range;

	public CubicEffectRange(int range) {
		super(range);
		this.range = range;
	}

	@Override
	public void initEffectIterator(EffectIterator i) {
		i.resetAsCubicIterator(range);
	}
}
