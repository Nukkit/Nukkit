package cn.nukkit.level.physics;

import gnu.trove.list.array.TIntArrayList;

public class UpdateQueue {
	private TIntArrayList xArray = new TIntArrayList();
	private TIntArrayList yArray = new TIntArrayList();
	private TIntArrayList zArray = new TIntArrayList();
	private int y;
	private int z;

	public void add(int x, int y, int z) {
		xArray.add(x);
		yArray.add(y);
		zArray.add(z);
	}

	public boolean hasNext() {
		return !xArray.isEmpty();
	}

	/**
	 * Gets the next x coordinate.  This method updates the internal array indexes and should only be called if hasNext returns true
	 *
	 * @return the next x coordinate
	 */
	public int getX() {
		int x;
		int index = xArray.size() - 1;
		x = xArray.removeAt(index);
		y = yArray.removeAt(index);
		z = zArray.removeAt(index);
		return x;
	}

	/**
	 * Gets the y coordinate
	 *
	 * @return the y coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets the z coordinate
	 *
	 * @return the z coordinate
	 */
	public int getZ() {
		return z;
	}
}
