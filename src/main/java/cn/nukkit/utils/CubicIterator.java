package cn.nukkit.utils;

import cn.nukkit.math.BlockVector3;

import java.util.Iterator;

/**
 * An Iterator that iterates outwards from a given central 3d integer coordinate.<br> <br> The Manhattan distance from the given center to the coordinates in the sequence increases monotonically and
 * the iterator passes through all integer coordinates.
 *
 * Nukkit Project
 */
public class CubicIterator extends BlockVector3 implements Iterator<BlockVector3> {
	private final BlockVector3 bottom;
	private final BlockVector3 top;
	private boolean hasNext;
	private boolean first = true;

	public CubicIterator() {
		this(0, 0, 0);
	}

	public CubicIterator(int w) {
		this(0, 0, 0, w);
	}

	public CubicIterator(int x, int y, int z) {
		this(x, y, z, Integer.MAX_VALUE);
	}

	public CubicIterator(int x, int y, int z, int w) {
		this(x - w, y - w, z - w, x + w, x + w, x + w);
	}

	public CubicIterator(int bx, int by, int bz, int tx, int ty, int tz) {
		super(bx, by, bz);
		if (bx > tx || by > ty || bz > tz) {
			throw new IllegalArgumentException("Bottom coordinates must be less than top coordinates");
		}
		bottom = new BlockVector3(bx, by, bz);
		top = new BlockVector3(tx, ty, tz);
		first = true;
		this.hasNext = true;
	}

	/**
	 * Resets the iterator to the cuboid from (bx, by, bz) to (tx, ty, tz)
	 */
	public void reset(int bx, int by, int bz, int tx, int ty, int tz) {
		if (bx > tx || by > ty || bz > tz) {
			throw new IllegalArgumentException("Bottom coordinates must be less than top coordinates");
		}
		bottom.setComponents(bx, by, bz);
		super.setComponents(bottom.x, bottom.y, bottom.z);
		top.setComponents(tx, ty, tz);
		first = true;
		hasNext = true;
	}

	public void reset(int x, int y, int z, int w) {
		reset(x - w, y - w, z - w, x + w, y + w, z + w);
	}

	@Override
	public boolean hasNext() {
		return hasNext;
	}

	@Override
	public BlockVector3 next() {
		// First block is always the central block
		int x = getX();
		int y = getY();
		int z = getZ();

		if (first) {
			first = false;
		} else {
			if (x >= top.getX()) {
				setX(x = bottom.getX());
				if (y >= top.getY()) {
					setY(y = bottom.getY());
					if (z >= top.getZ()) {
						throw new IllegalStateException("Iterator reached end");
					} else {
						setZ(++z);
					}
				} else {
					setY(++y);
				}
			} else {
				setX(++x);
			}
		}
		if (x >= top.getX() && z >= top.getZ() && y >= top.getY()) {
			hasNext = false;
		}
		return this;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("This operation is not supported");
	}
}
