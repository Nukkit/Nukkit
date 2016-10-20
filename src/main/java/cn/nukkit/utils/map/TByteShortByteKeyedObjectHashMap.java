package cn.nukkit.utils.map;


import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;

import java.util.Collection;

/**
 * A simplistic map that supports a 3 bytes for keys, using a trove int int hashmap in the backend.
 */
public class TByteShortByteKeyedObjectHashMap<K> {
    protected final TIntObjectHashMap<K> map;

    public TByteShortByteKeyedObjectHashMap() {
        map = new TIntObjectHashMap<>(5);
    }

    public TByteShortByteKeyedObjectHashMap(int capacity) {
        map = new TIntObjectHashMap<>(capacity);
    }

    public K put(int key1, int key2, int key3, K value) {
        int key = key(key1, key2, key3);
        return map.put(key, value);
    }

    public K get(int key1, int key2, int key3) {
        int key = key(key1, key2, key3);
        return map.get(key);
    }

    public boolean containsKey(int key1, int key2, int key3) {
        int key = key(key1, key2, key3);
        return map.containsKey(key);
    }

    public void clear() {
        map.clear();
    }

    public void compact() {
        map.compact();
    }

    public boolean containsValue(int val) {
        return map.containsValue(val);
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public TIntObjectIterator<K> iterator() {
        return map.iterator();
    }

    public TIntSet keySet() {
        return map.keySet();
    }

    public int[] keys() {
        return map.keys();
    }

    public K remove(int key1, int key2, int key3) {
        int key = key(key1, key2, key3);
        return map.remove(key);
    }

    public int size() {
        return map.size();
    }

    public Collection<K> valueCollection() {
        return map.valueCollection();
    }

    /**
     * Creates a long key from 2 bytes and a short
     *
     * @param key1 a <code>byte</code> value
     * @param key2 a <code>short</code> value
     * @param key3 a <code>byte</code> value
     * @return a long which is the concatenation of key1, key2 and key3
     */
    public static int key(int key1, int key2, int key3) {
        return (key1 & 0xFF) << 24 | (key3 & 0xFF) << 16 | key2 & 0xFFFF;
    }

    /**
     * Gets the first 8-bit integer value from a long key
     *
     * @param key to get from
     * @return the first 8-bit integer value in the key
     */
    public static byte key1(int key) {
        return (byte) (key >> 24);
    }

    /**
     * Gets the second 16-bit integer value from a long key
     *
     * @param key to get from
     * @return the second 16-bit integer value in the key
     */
    public static short key2(int key) {
        return (short) key;
    }

    /**
     * Gets the third 8-bit integer value from a long key
     *
     * @param key to get from
     * @return the third 8-bit integer value in the key
     */
    public static byte key3(int key) {
        return (byte) (key >> 16);
    }
}
