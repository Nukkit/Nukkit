package cn.nukkit.level.range;

import cn.nukkit.level.Level;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.utils.Utils;
import gnu.trove.impl.hash.TIntHash;
import gnu.trove.set.hash.TIntHashSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ListEffectRange extends EffectRangeImpl {
	private final List<BlockVector3> effectList;

	public ListEffectRange(EffectRange... ranges) {
		this(combineRanges(ranges), false);
	}

	public ListEffectRange(List<BlockVector3> effectList) {
		this(effectList, true);
	}

	protected ListEffectRange(List<BlockVector3> effectList, boolean copy) {
		super(effectList);
		if (copy) {
			this.effectList = new ArrayList<>(effectList.size());
			for (BlockVector3 v : effectList) {
				this.effectList.add(v.clone());
			}
			Utils.removeDuplicates(this.effectList);
		} else {
			this.effectList = effectList;
		}
	}

	@Override
	public void initEffectIterator(EffectIterator i) {
		i.resetAsList(effectList);
	}

	@Override
	public EffectRange translate(BlockVector3 offset) {
		List<BlockVector3> newEffectList = new ArrayList<>(effectList.size());
		for (BlockVector3 effect : effectList) {
			BlockVector3 translated = effect.clone();
			translated.add(offset);
			newEffectList.add(translated);
		}
		return new ListEffectRange(newEffectList, false);
	}

	private static List<BlockVector3> combineRanges(EffectRange[] ranges) {
		List<BlockVector3> list = new ArrayList<>();
		EffectIterator i = new EffectIterator();
        TIntHashSet keys = new TIntHashSet(8);
		for (EffectRange e : ranges) {
			e.initEffectIterator(i);
			while (i.hasNext()) {
				BlockVector3 v = i.next();
				if (!keys.contains(Level.blockHash(v))) {
				    keys.add(Level.blockHash(v));
					list.add(v.clone());
				}
			}
		}
		return list;
	}
}
