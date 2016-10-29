package cn.nukkit.utils;

import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.UUID;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BinaryStream extends OutputStream{
    public int offset;
    public byte[] buffer;
    protected final ArrayDeque<byte[]> buffers = new ArrayDeque<>();
    protected final byte[] shortBuffer = new byte[2];
    protected final byte[] intBuffer = new byte[4];
    protected final byte[] longBuffer = new byte[8];
    protected int count;

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    public BinaryStream() {
        this.buffer = new byte[32];
        this.offset = 0;
        this.count = 0;
    }

    public BinaryStream(int buffer) {
        this(new byte[buffer]);
    }

    public BinaryStream(byte[] buffer) {
        this(buffer, 0);
    }

    public BinaryStream(byte[] buffer, int offset) {
        this.buffer = buffer;
        this.offset = offset;
        this.count = 0;
    }

    public int getBlockSize() {
        return 1024;
    }

    public void reset() {
        if (offset != 0 || !this.buffers.isEmpty()) {
            setBuffer(buffer == null ? new byte[35] : buffer);
        }
    }

    public final void setBuffer(byte[] buffer) {
        if (!buffers.isEmpty()) {
            buffers.clear();
        }
        this.offset = 0;
        this.count = 0;
        this.buffer = buffer;
    }

    public final void setBuffer(byte[] buffer, int offset) {
        this.setBuffer(buffer);
        this.setOffset(offset);
    }

    private final void addBuffer() {
        buffers.addLast(buffer);
        count += buffer.length;
        buffer = new byte[getBlockSize()];
        offset = 0;
    }

    public final int getOffset() {
        return offset;
    }

    public final void setOffset(int offset) {
        this.offset = offset;
    }

    public final byte[][] getBuffers() {
        byte[][] res;
        if (offset > 0) {
            byte[] buf2 = new byte[offset];
            System.arraycopy(buffer, 0, buf2, 0, offset);
            buffer = buf2;
            res = new byte[buffers.size() + 1][];
            res[res.length - 1] = buffer;
        } else {
            res = new byte[buffers.size()][];
        }
        int i = 0;
        for (byte[] bytes : buffers) {
            res[i++] = bytes;
        }
        return res;
    }

    public final byte[] toByteArray() {
        return getBuffer();
    }

    public byte[] getBuffer() {
        if (buffers.isEmpty()) {
            if (buffer.length == offset) {
                return buffer;
            }
            buffer = Arrays.copyOfRange(buffer, 0, offset);
            return buffer;
        }
        byte[] data = new byte[getCount()];

        // Check if we have a list of buffers
        int pos = 0;

        if (buffers != null) {
            for (byte[] bytes : buffers) {
                System.arraycopy(bytes, 0, data, pos, bytes.length);
                pos += bytes.length;
            }
        }

        // write the internal buffer directly
        System.arraycopy(buffer, 0, data, pos, offset);

        this.offset = count + offset;
        this.count = 0;
        this.buffer = data;
        this.buffers.clear();
        return this.buffer;
    }

    public byte[] getRawBuffer() {
        if (buffers.isEmpty()) {
            return buffer;
        }
        byte[] data = new byte[getCount()];
        // Check if we have a list of buffers
        int pos = 0;

        if (buffers != null) {
            for (byte[] bytes : buffers) {
                System.arraycopy(bytes, 0, data, pos, bytes.length);
                pos += bytes.length;
            }
        }
        // write the internal buffer directly
        System.arraycopy(buffer, 0, data, pos, buffer.length);
        this.offset = count + offset;
        this.count = 0;
        this.buffer = data;
        this.buffers.clear();
        return this.buffer;
    }

    public final int getCount() {
        return count + offset;
    }

    public final byte[] get() {
        return this.get(this.buffer.length - this.offset);
    }

    public final byte[] get(int len) {
        if (len < 0) {
            this.offset = buffer.length - 1;
            return new byte[0];
        }
        len = Math.min(len, buffer.length - this.offset);
        this.offset += len;
        return Arrays.copyOfRange(this.buffer, this.offset - len, this.offset);
    }

    public final byte[] get(int len, byte[] useBuffer) {
        if (len < 0) {
            this.offset = this.buffer.length - 1;
            return new byte[0];
        }
        len = Math.min(len, this.buffer.length - this.offset);
        this.offset += len;
        System.arraycopy(this.buffer, this.offset - len, useBuffer, 0, len);
        return useBuffer;
    }

    public final void put(byte[] b) {
        if (b.length > buffer.length) {
            if (offset > 0) {
                if (offset != buffer.length) {
                    byte[] buf2 = new byte[offset];
                    System.arraycopy(buffer, 0, buf2, 0, offset);
                    buffer = buf2;
                }
                buffers.addLast(buffer);
                count += buffer.length;
            }
            buffer = b;
            offset = b.length;
        } else {
            put(b, 0, b.length);
        }
    }

    @Override
    public final void write(int b) {
        put(b);
    }

    @Override
    public final void write(byte[] b) {
        put(b);
    }

    @Override
    public final void write(byte[] b, int off, int len) {
        put(b, off, len);
    }

    public final void put(int datum) {
        if (offset == buffer.length) {
            addBuffer();
        }
        // store the byte
        buffer[offset++] = (byte) datum;
    }

    public final void put(byte[] data, int offset, int length) {
        if ((offset < 0) || ((offset + length) > data.length) || (length < 0)) {
            throw new IndexOutOfBoundsException();
        } else {
            if ((this.offset + length) > buffer.length) {
                int copyLength;

                do {
                    if (this.offset == buffer.length) {
                        addBuffer();
                    }
                    copyLength = Math.min(buffer.length - this.offset, length);
                    System.arraycopy(data, offset, buffer, this.offset, copyLength);
                    offset += copyLength;
                    this.offset += copyLength;
                    length -= copyLength;
                } while (length > 0);
            } else {
                // Copy in the subarray
                System.arraycopy(data, offset, buffer, this.offset, length);
                this.offset += length;
            }
        }
    }

    public final long getLong() {
        return Binary.readLong(this.get(8, longBuffer));
    }

    public final void putLong(long l) {
        this.put(Binary.writeLong(l));
    }

    public final int getInt() {
        return Binary.readInt(this.get(4, intBuffer));
    }

    public final void putInt(int i) {
        this.put(Binary.writeInt(i));
    }

    public final long getLLong() {
        return Binary.readLLong(this.get(8, longBuffer));
    }

    public final void putLLong(long l) {
        this.put(Binary.writeLLong(l));
    }

    public final int getLInt() {
        return Binary.readLInt(this.get(4, intBuffer));
    }

    public final void putLInt(int i) {
        this.put(Binary.writeLInt(i));
    }

    public final int getShort() {
        return Binary.readShort(this.get(2, shortBuffer));
    }

    public final void putShort(int s) {
        this.put(Binary.writeShort(s));
    }

    public final short getSignedShort() {
        return Binary.readSignedShort(this.get(2, shortBuffer));
    }

    public final void putSignedShort(short s) {
        this.put(Binary.writeShort(s));
    }

    public final int getLShort() {
        return Binary.readLShort(this.get(2, shortBuffer));
    }

    public final void putLShort(int s) {
        this.put(Binary.writeLShort(s));
    }

    public final short getSignedLShort() {
        return Binary.readSignedLShort(this.get(2, shortBuffer));
    }

    public final void putSignedLShort(short s) {
        this.put(Binary.writeLShort(s));
    }

    public final float getFloat() {
        return Binary.readFloat(this.get(4, intBuffer));
    }

    public final void putFloat(float v) {
        this.put(Binary.writeFloat(v));
    }

    public final float getLFloat() {
        return Binary.readLFloat(this.get(4, intBuffer));
    }

    public final void putLFloat(float v) {
        this.put(Binary.writeLFloat(v));
    }

    public final int getTriad() {
        return Binary.readTriad(this.get(3));
    }

    public final void putTriad(int triad) {
        this.put(Binary.writeTriad(triad));
    }

    public final int getLTriad() {
        return Binary.readLTriad(this.get(3));
    }

    public final void putLTriad(int triad) {
        this.put(Binary.writeLTriad(triad));
    }

    public final byte getSignedByte() {
        return this.buffer[this.offset++];
    }

    public final boolean getBoolean() {
        return this.getByte() == 0x01;
    }

    public final void putBoolean(boolean bool) {
        this.putByte((byte) (bool ? 1 : 0));
    }

    public final int getByte() {
        return this.buffer[this.offset++] & 0xff;
    }

    public final void putByte(byte b) {
        this.put(new byte[]{b});
    }

    public final byte[][] getDataArray() {
        return this.getDataArray(10);
    }

    public final byte[][] getDataArray(int len) {
        byte[][] data = new byte[len][];
        for (int i = 0; i < len && !this.feof(); ++i) {
            data[i] = this.get(this.getTriad());
        }
        return data;
    }

    public final void putDataArray(byte[][] data) {
        for (byte[] v : data) {
            this.putTriad(v.length);
            this.put(v);
        }
    }

    public final void putUUID(UUID uuid) {
        this.put(Binary.writeUUID(uuid));
    }

    public final UUID getUUID() {
        return Binary.readUUID(this.get(16));
    }

    public final void putSkin(Skin skin) {
        this.putString(skin.getModel());
        this.putShort(skin.getData().length);
        this.put(skin.getData());
    }

    public Skin getSkin() {
        String modelId = this.getString();
        byte[] skinData = this.get(this.getShort());
        return new Skin(skinData, modelId);
    }

    public final Item getSlot() {
        short id = this.getSignedShort();

        if (id <= 0) {
            return Item.get(0, 0, 0);
        }
        int cnt = this.getByte();

        int data = this.getShort();

        int nbtLen = this.getLShort();

        byte[] nbt = new byte[0];
        if (nbtLen > 0) {
            nbt = this.get(nbtLen);
        }

        return Item.get(
                id, data, cnt, nbt
        );
    }

    public final void putSlot(Item item) {
        if (item == null || item.getId() == 0) {
            this.putShort(0);
            return;
        }

        this.putShort(item.getId());
        this.putByte((byte) (item.getCount() & 0xff));
        this.putShort(!item.hasMeta() ? -1 : item.getDamage());

        byte[] nbt = item.getCompoundTag();
        this.putLShort(nbt.length);
        this.put(nbt);
    }

    public final String getString() {
        return new String(this.get(this.getShort()), StandardCharsets.UTF_8);
    }

    public final void putString(String string) {
        byte[] b = string.getBytes(StandardCharsets.UTF_8);
        this.putShort(b.length);
        this.put(b);
    }

    public final boolean feof() {
        return this.offset < 0 || this.offset >= this.buffer.length;
    }
}
