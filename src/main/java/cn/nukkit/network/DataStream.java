package cn.nukkit.network;

import java.nio.charset.StandardCharsets;

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
     * Creates a newly allocated byte array. Its size is the current
     * size of this output stream and the valid contents of the buffer
     * have been copied into it.
     *
     * @return  the current contents of this output stream.
     */
    public byte[] getByteArray() {
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
     * @param cursor The value add on write cursor.
     */
    public void addCursor(int cursor) {
        this.cursor += cursor;
    }

    /**
     * @return The value of write cursor.
     */
    public int getCursor() {
        return cursor;
    }

    /**
     * @param cursor The value set as write cursor.
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
     * @param value The <code>char</code> to be written.
     */
    public void put(char value) {
        write(value >>> 8 & 0xFF);
        write(value       & 0xFF);
    }

    /**
     * @param value The <code>short</code> to be written.
     */
    public void put(short value) {
        write(value >>> 8 & 0xFF);
        write(value       & 0xFF);
    }

    /**
     * @param value The <code>int</code> to be written.
     */
    public void put(int value) {
        write((value >>> 24 & 0xFF));
        write((value >>> 16 & 0xFF));
        write((value >>> 8 & 0xFF));
        write((value & 0xFF));
    }

    /**
     * @param value The <code>float</code> to be written.
     */
    public void put(float value) {
        put(Float.floatToIntBits(value));
    }

    /**
     * @param value The <code>double</code> to be written.
     */
    public void put(double value) {
        put(Double.doubleToLongBits(value));
    }

    /**
     * @param value The <code>int</code> value to be written.
     */
    public void put(long value) {
        write((byte) (value >>> 56));
        write((byte) (value >>> 48));
        write((byte) (value >>> 40));
        write((byte) (value >>> 32));
        write((byte) (value >>> 24));
        write((byte) (value >>> 16));
        write((byte) (value >>> 8));
        write((byte) (value));
    }

    /**
     * @param value The <code>String</code> value to be written.
     */
    public void put(String value) {
        byte[] array = value.getBytes(StandardCharsets.UTF_8);
        put((short) array.length);
        put((array));
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
        return Float.intBitsToFloat(getInteger());
    }

    /**
     * @return The <code>int</code> value.
     */
    public int getInteger() {
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
     * @return The <code>String</code> value to be written.
     */
    public String getString() {
        return new String(get(getShort()), StandardCharsets.UTF_8);
    }

    /**
     * Write a byte into this stream.
     *
     * @param b The byte value.
     */
    public void write(int b) {
        if (cursor == buffer.length) {
            grow(8); /** 8 bit at least.*/
        }
        buffer[cursor++] = ((byte) b);
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
