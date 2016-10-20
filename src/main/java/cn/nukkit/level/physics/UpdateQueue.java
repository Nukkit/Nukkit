package cn.nukkit.level.physics;

import cn.nukkit.utils.map.TByteShortByteKeyedObjectHashMap;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TByteArrayList;
import gnu.trove.list.array.TIntArrayList;

public class UpdateQueue {
	private TByteShortByteKeyedObjectHashMap<TIntList> map = new TByteShortByteKeyedObjectHashMap<>();
	private TByteArrayList xArray = new TByteArrayList();
	private TByteArrayList yArray = new TByteArrayList();
	private TByteArrayList zArray = new TByteArrayList();
	private int y;
	private int z;
	private int maxSize = 0;
	private int maxMapSize = 0;

	public void add(int x, int y, int z) {
		TIntList list = map.get(x, y & 0xFF, z);
		if (list != null) {
			TIntIterator i = list.iterator();
			while (i.hasNext()) {
				int index = i.next();
				if (
						(xArray.get(index) & 0xFF) == (x & 0xFF) &&
								(yArray.get(index) & 0xFF) == (y & 0xFF) &&
								(zArray.get(index) & 0xFF) == (z & 0xFF)
						) {
					return;
				}
			}
		} else {
			list = new TIntArrayList();
			map.put(x, y & 0xFF, z, list);
			if (map.size() > maxMapSize) {
				maxMapSize = map.size();
			}
		}
		int size = xArray.size();
		if (size > maxSize) {
			maxSize = size;
		}
		list.add(size);
		xArray.add((byte) x);
		yArray.add((byte) y);
		zArray.add((byte) z);
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
		x = xArray.removeAt(index) & 0xFF;
		y = yArray.removeAt(index) & 0xFF;
		z = zArray.removeAt(index) & 0xFF;

		if (maxSize > 10 && index < (maxSize >> 1)) {
			xArray.trimToSize();
			yArray.trimToSize();
			zArray.trimToSize();
			maxSize = xArray.size();
		}

		TIntList list = map.get(x, y & 0xFF, z);
		if (list == null || !list.remove(index)) {
			throw new IllegalStateException("Index was not in list, or list was null");
		}
		if (list.isEmpty()) {
			if (map.remove(x, y & 0xFF, z) == null) {
				throw new IllegalStateException("Removed update location was not in HashSet");
			}
			if (maxMapSize > 5 && map.size() < (maxMapSize >> 1)) {
				map.compact();
				maxMapSize = map.size();
			}
		}
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
