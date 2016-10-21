package cn.nukkit.utils;

import cn.nukkit.math.BlockVector3;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An Iterator that iterates outwards from a given central 3d integer coordinate.<br> <br> The Manhattan distance from the given center to the coordinates in the sequence increases monotonically and
 * the iterator passes through all integer coordinates.
 *
 * Nukkit Project
 */
public class OutwardIterator extends BlockVector3 implements Iterator<BlockVector3> {
    private final BlockVector3 center;
    private final BlockVector3 step;
    private int distance;
    private int endDistance;
    private boolean hasNext;
    private boolean first = true;

    public OutwardIterator() {
        this(0, 0, 0);
    }

    public OutwardIterator(int x, int y, int z) {
        this(x, y, z, Integer.MAX_VALUE);
    }

    // TODO: make maxDistance of -1 make hasNext false
    public OutwardIterator(int x, int y, int z, int maxDistance) {
        super(x, y, z);
        center = new BlockVector3(x, y, z);
        step = new BlockVector3(0, 0, 0);
        first = true;
        distance = 0;
        this.endDistance = maxDistance;
        this.hasNext = true;
    }

    /**
     * Resets the iterator and positions it at (x, y, z)
     */
    public void reset(int x, int y, int z) {
        super.setX(x);
        super.setY(y);
        super.setZ(z);
        center.setX(x);
        center.setY(y);
        center.setZ(z);
        first = true;
        hasNext = true;
        distance = 0;
    }

    public void reset(int x, int y, int z, int startDistance, int endDistance) {
        this.endDistance = endDistance;
        reset(x, y, z);
        if (startDistance > endDistance) {
            this.hasNext = false;
        } else if (startDistance > 0) {
            // reset to start at distance
            super.setY(y + startDistance - 1);
            first = false;
        }
    }

    public void reset(int x, int y, int z, int endDistance) {
        reset(x, y, z, 0, endDistance);
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public BlockVector3 next() {
        if (!this.hasNext) {
            throw new NoSuchElementException("The Outward Iterator ran out of elements");
        }
        // First block is always the central block
        if (first) {
            step.setX(0);
            step.setZ(0);
            first = false;
            if (this.endDistance <= 0) {
                this.hasNext = false;
            }
        } else {
            int dx = getX() - center.getX();
            int dy = getY() - center.getY();
            int dz = getZ() - center.getZ();

            // Last block was top of layer, move to start of next layer
            if (dx == 0 && dz == 0 && dy >= 0) {
                setY((center.getY() << 1) - getY() - 1);
                step.setX(0);
                step.setZ(0);
                distance++;
            } else if (dx == 0) {
                // Reached end of horizontal slice
                // Move up to next slice
                if (dz >= 0) {
                    step.setX(1);
                    step.setZ(-1);

                    setY(getY() + 1);

                    // Bottom half of layer
                    if (dy < 0) {
                        setZ(getZ() + 1);
                        // Top half of layer
                    } else {
                        setZ(getZ() - 1);
                        // Reached top of layer
                        if (getZ() == center.getZ()) {
                            step.setX(0);
                            step.setZ(0);
                        }
                    }
                    // Change direction (50% of horizontal slice complete)
                } else {
                    step.setX(-1);
                    step.setZ(1);
                }
            } else if (dz == 0) {
                // Change direction (25% of horizontal slice complete)
                if (dx > 0) {
                    step.setX(-1);
                    step.setZ(-1);
                    // Change direction (75% of horizontal slice compete)
                } else {
                    step.setX(1);
                    step.setZ(1);
                }
            }
            add(step);
            if (distance == 0 || (dx == 0 && dz == 1 && dy >= endDistance - 1)) {
                hasNext = false;
            }
        }
        return this;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("This operation is not supported");
    }
}
