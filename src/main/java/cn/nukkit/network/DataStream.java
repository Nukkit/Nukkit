package cn.nukkit.network;

import cn.nukkit.item.Item;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOf;
import static java.util.Arrays.copyOfRange;

/**
 * This is a stream support input and output without conflict.
 * Compatible with MineCraftPocketEdition' s data protocol.
 */
public class DataStream {

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private byte[] buffer;
    private int    cursor;
    private int    offset;

    /**
     * Initialization with 32 bit size buffer.
     */
    public DataStream() {
        this(32);
    }

    /**
     * Initialization with given buffer size.
     *
     * @param size The buffer size.
     */
    public DataStream(int size) {
        this.buffer = new byte[size];
    }

    /**
     * Initialization with given buffer.
     *
     * @param buffer The buffer handle by this stream.
     */
    public DataStream(byte[] buffer) {
        if (buffer == null) {
            throw new NullPointerException();
        }
        this.buffer = buffer;
        this.cursor = buffer.length;
    }

    /**
     * Bounding a bew buffer and reset cursor and offset.
     *
     * @param buffer The buffer to be set.
     */
    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
        this.cursor = this.offset = 0;
    }

    /**
     * Creates a newly allocated byte array. Its size is the current
     * size of this output stream and the valid contents of the buffer
     * have been copied into it.
     *
     * @return  the current contents of this output stream.
     */
    public byte[] toByteArray() {
        return copyOf(buffer, cursor);
    }

    /**
     * @return The value of read offset.
     */
    public int getOffset() {
        return offset;
    }

    /**
     * @param offset The value set as read offset.
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * @param cursor The value add on putByte cursor.
     */
    public void addCursor(int cursor) {
        this.cursor += cursor;
    }

    /**
     * @return The value of putByte cursor.
     */
    public int getCursor() {
        return cursor;
    }

    /**
     * @param cursor The value set as putByte cursor.
     */
    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    /**
     * @param array The <code>byte</code> array to be written.
     */
    public void put(byte[] array) {
        if (cursor + array.length >= buffer.length) {
            grow(array.length);
        }
        arraycopy(array, 0, buffer, cursor, array.length);
        addCursor(array.length);
    }

    /**
     * putByte a byte into this stream.
     *
     * @param b The byte value.
     */
    public void putByte(int b) {
        if (cursor == buffer.length) {
            grow(8); /** 8 bit at least.*/
        }
        buffer[cursor++] = ((byte) b);
    }

    /**
     * @param value The <code>char</code> to be written.
     */
    public void putChar(int value) {
        putByte(value >>> 8 & 0xFF);
        putByte(value & 0xFF);
    }

    /**
     * @param value The <code>short</code> to be written.
     */
    public void putShort(int value) {
        putByte(value >>> 8 & 0xFF);
        putByte(value & 0xFF);
    }

    /**
     * @param value The <code>int</code> to be written.
     */
    public void putInt(int value) {
        putByte((value >>> 24 & 0xFF));
        putByte((value >>> 16 & 0xFF));
        putByte((value >>> 8 & 0xFF));
        putByte((value & 0xFF));
    }

    /**
     * @param value The <code>float</code> to be written.
     */
    public void putFloat(float value) {
        putInt(Float.floatToIntBits(value));
    }

    /**
     * @param value The <code>double</code> to be written.
     */
    public void putDouble(double value) {
        putLong(Double.doubleToLongBits(value));
    }

    /**
     * @param value The <code>int</code> value to be written.
     */
    public void putLong(long value) {
        putByte((int) (value >>> 56));
        putByte((int) (value >>> 48));
        putByte((int) (value >>> 40));
        putByte((int) (value >>> 32));
        putByte((int) (value >>> 24));
        putByte((int) (value >>> 16));
        putByte((int) (value >>> 8));
        putByte((int) (value));
    }

    /**
     * @param value The <code>String</code> value to be written.
     * @throws IllegalArgumentException If <code>String</code> value too long.
     */
    public void putString(String value) {
        byte[] array = value.getBytes(StandardCharsets.UTF_8);
        int length = array.length;
        if (length > Short.MAX_VALUE) {
            throw new IllegalArgumentException("String value too long!");
        }
        putShort(array.length);
        put(array);
    }

    public void putUUID(UUID uuid) {
        putLong(uuid.getMostSignificantBits());
        putLong(uuid.getLeastSignificantBits());
    }

    /**
     * @param item The <code>Item</code> value to be written.
     */
    public void putItem(Item item) {
        if (item.getId() == 0) {
            putShort(0);
        } else {
            putShort(item.getId());
            putByte(item.getCount());
            putShort(item.getDamage() == null ? -1 : item.getDamage());
            byte[] tag = item.getCompoundTag();
            if (tag == null) {
                putShort(0);
            } else {
                putShort(tag.length);
                put(tag);
            }
        }
    }

    /**
     * Return a byte array contains data from buffer.
     *
     * @param size The size of request byte array.
     * @return The copy of request byte array.
     */
    public byte[] get(int size) {
        if (offset + size <= cursor) {
            return copyOfRange(buffer, offset, offset += size);
        }
        throw new ArrayIndexOutOfBoundsException(offset + size);
    }

    public byte getByte() {
        return ((byte) read());
    }

    public char getChar() {
        return (char) ((read() << 8) + read());
    }

    /**
     * @return The <code>short</code> value.
     */
    public short getShort() {
        return (short) ((read() << 8) + read());
    }

    /**
     * @return The <code>double</code> value.
     */
    public double getDouble() {
        return Double.longBitsToDouble(getLong());
    }

    /**
     * @return The <code>float</code> value.
     */
    public float getFloat() {
        return Float.intBitsToFloat(getInt());
    }

    /**
     * @return The <code>int</code> value.
     */
    public int getInt() {
        return (read() << 24) + (read() << 16) + (read() << 8) + read();
    }

    /**
     * @return The <code>long</code> value.
     */
    public long getLong() {
        return (((long)  read() << 56) +
                ((long) (read() & 255) << 48) +
                ((long) (read() & 255) << 40) +
                ((long) (read() & 255) << 32) +
                ((long) (read() & 255) << 24) +
                ((read() & 255) << 16) +
                ((read() & 255) << 8) +
                ( read() & 255)
        );
    }

    /**
     * @return Return a <code>String</code> value from this stream.
     */
    public String getString() {
        return new String(get(getShort()), StandardCharsets.UTF_8);
    }

    /**
     * @return Return a <code>UUID</code> value from this stream.
     */
    public UUID getUUID() {
        return new UUID(getLong(), getLong());
    }

    public Item getItem() {
        int id = getShort();
        if (  id < 1  ) {
            return new Item(0, 0, 0);
        }
        int count = getByte();
        int meta = getShort();
        int length = getShort();
        byte[] tagCompound = length > 0 ? get(length) : new byte[]{};
        return Item.get(id, meta, count, tagCompound);
    }

    public boolean hasRemain() {
        return cursor > offset;
    }

    /**
     * Read the next byte of data from this stream. The value
     * byte is returned as an <code>int</code> in the range
     * <code>0</code> to <code>255</code>. If no byte is available
     * because the end of the stream has been reached, a
     * <code>ArrayIndexOutOfBoundsException</code> will be throw.
     *
     * @return the next byte of data.
     * @throws ArrayIndexOutOfBoundsException
     */
    public int read() {
        if (cursor == offset) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        return (buffer[offset++] & 0xFF);
    }

    private void grow(int size) {
        int j = buffer.length << 1;
        if (j < buffer.length)
            j = cursor + size;
        if (j > MAX_ARRAY_SIZE)
            throw new OutOfMemoryError("Array size too big!");
        else
            buffer = copyOf(buffer, j);
    }

}
