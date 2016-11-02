package cn.nukkit.level.range;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.VectorMath;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class EffectRangeImpl implements EffectRange {
    private static int regionMax = 256 - 1;//Region max size
    private double maxX;
    private double maxY;
    private double maxZ;
    private double minX;
    private double minY;
    private double minZ;

    protected EffectRangeImpl(double range) {
        this(range, range, range);
    }

    protected EffectRangeImpl(double x, double y, double z) {
        this(x, y, z, -x, -y, -z);
    }

    protected EffectRangeImpl(double maxX, double maxY, double maxZ, double minX, double minY, double minZ) {
        if (maxX < minX || maxY < minY || maxZ < minZ) {
            throw new IllegalArgumentException("MaxX/Y/Z must be greater than MinX/Y/Z");
        }
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
    }

    protected EffectRangeImpl(Iterable<BlockVector3> blocks) {
        this(blocks.iterator());
    }

    protected EffectRangeImpl(Iterator<BlockVector3> blocks) {
        double mxx = 0;
        double mxy = 0;
        double mxz = 0;
        double mnx = 0;
        double mny = 0;
        double mnz = 0;
        boolean first = true;
        while (blocks.hasNext()) {
            BlockVector3 next = blocks.next();
            double x = next.getX();
            double y = next.getY();
            double z = next.getZ();
            if (first) {
                first = false;
                mxx = x;
                mnx = x;
                mxy = y;
                mny = y;
                mxz = z;
                mnz = z;
                continue;
            }
            if (x > mxx) {
                mxx = x;
            } else if (x < mnx) {
                mnx = x;
            }
            if (z > mxz) {
                mxz = z;
            } else if (z < mnz) {
                mnz = z;
            }
            if (y > mxy) {
                mxy = y;
            } else if (y < mny) {
                mny = y;
            }
        }
        this.maxX = mxx;
        this.maxY = mxy;
        this.maxZ = mxz;
        this.minX = mnx;
        this.minY = mny;
        this.minZ = mnz;
        if (maxX < minX || maxY < minY || maxZ < minZ) {
            throw new IllegalArgumentException("MaxX/Y/Z must be greater than MinX/Y/Z");
        }
    }

    @Override
    public EffectIterator iterator() {
        EffectIterator iter = new EffectIterator();
        this.initEffectIterator(iter);
        return iter;
    }

    @Override
    public abstract void initEffectIterator(EffectIterator i);

    @Override
    public boolean isRegionLocal(int x, int y, int z) {
        x &= 255;
        y &= 255;
        z &= 255;
        return !(x + maxX > regionMax || y + maxY > regionMax || z + maxZ > regionMax || x + minX < 0 || y + minY < 0 || z + minZ < 0);
    }

    @Override
    public EffectRange translate(int side) {
        return translate(VectorMath.getSideOffset(side));
    }

    @Override
    public EffectRange translate(BlockVector3 offset) {
        // Basic implementation, should be overridden if a better method is available
        List<BlockVector3> blocks = new ArrayList<>();
        EffectIterator iter = new EffectIterator();
        this.initEffectIterator(iter);
        while (iter.hasNext()) {
            BlockVector3 translated = iter.next().clone();
            translated.add(offset);
            blocks.add(translated);
        }
        return new ListEffectRange(blocks, false);
    }
}
