package cn.nukkit.level.range;

public class DiamondEffectRange extends EffectRangeImpl {
	private final int startRange, endRange;

	public DiamondEffectRange(int range) {
		this(0, range);
	}

	public DiamondEffectRange(int startRange, int endRange) {
		super(endRange);
		this.startRange = startRange;
		this.endRange = endRange;
	}

	@Override
	public void initEffectIterator(EffectIterator i) {
		i.resetAsOutwardIterator(this.startRange, this.endRange);
	}
}
