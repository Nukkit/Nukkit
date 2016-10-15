package cn.nukkit.level.range;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.utils.CubicIterator;
import cn.nukkit.utils.OutwardIterator;

import java.util.Iterator;
import java.util.List;

/**
 * Nukkit Project
 */
public class EffectIterator implements Iterator<BlockVector3> {
    private final OutwardIterator oi = new OutwardIterator();
    private final CubicIterator ci = new CubicIterator();
    private Iterator<BlockVector3> itr = null;
    private List<BlockVector3> offsetList = null;
    private int index;

    protected void reset() {
        offsetList = null;
        itr = null;
        ci.reset(0, 0, 0, 0);
        oi.reset(0, 0, 0);
    }

    protected void resetAsOutwardIterator(int startRange, int endRange) {
        oi.reset(0, 0, 0, startRange, endRange);
        itr = oi;
        offsetList = null;
    }

    protected void resetAsOutwardIterator(int range) {
        oi.reset(0, 0, 0, range);
        itr = oi;
        offsetList = null;
    }

    protected void resetAsCubicIterator(int range) {
        ci.reset(0, 0, 0, range);
        itr = ci;
        offsetList = null;
    }

    protected void resetAsCubicInterator(int bx, int by, int bz, int tx, int ty, int tz) {
        ci.reset(bx, by, bz, tx, ty, tz);
        itr = ci;
        offsetList = null;
    }

    protected void resetAsList(List<BlockVector3> list) {
        index = 0;
        offsetList = list;
    }

    @Override
    public boolean hasNext() {
        if (offsetList != null) {
            return index < offsetList.size();
        } else {
            return itr.hasNext();
        }
    }

    @Override
    public BlockVector3 next() {
        if (offsetList != null) {
            return offsetList.get(index++);
        } else {
            return itr.next();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("This method is not supported");
    }
}
