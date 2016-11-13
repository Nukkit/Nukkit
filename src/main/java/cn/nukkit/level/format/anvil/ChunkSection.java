package cn.nukkit.level.format.anvil;

import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.BinaryStream;

import java.nio.ByteBuffer;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ChunkSection implements cn.nukkit.level.format.ChunkSection {

    private final int y;
    private byte[] blocks;
    private byte[] data;
    private byte[] blockLight;
    private byte[] skyLight;

    public ChunkSection(CompoundTag nbt) {
        this.y = nbt.getByte("Y");
        this.blocks = nbt.getByteArray("Blocks");
        this.data = nbt.getByteArray("Data");
        this.blockLight = nbt.getByteArray("BlockLight");
        this.skyLight = nbt.getByteArray("SkyLight");
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getBlockId(int x, int y, int z) {
        return this.blocks[(x << 8) + (z << 4) + y] & 0xff;
    }

    @Override
    public void setBlockId(int x, int y, int z, int id) {
        this.blocks[(x << 8) + (z << 4) + y] = (byte) (id & 0xFF);
    }

    @Override
    public int getBlockData(int x, int y, int z) {
        int b = this.data[(x << 8) + (z << 4) + y] & 0xff;
        if ((x & 1) == 0) {
            return b & 0x0f;
        }
        return b >> 4;
    }

    @Override
    public void setBlockData(int x, int y, int z, int data) {
        int i = (x << 8) + (z << 4) + y;
        int old = this.data[i] & 0xff;
        if ((x & 1) == 0) {
            this.data[i] = (byte) (((((old & 0xf0) | data & 0x0f)) & 0xff) & 0xff);
        } else {
            this.data[i] = (byte) (((((data & 0x0f) << 4) | (old & 0x0f)) & 0xff) & 0xff);
        }
    }

    @Override
    public int getFullBlock(int x, int y, int z) {
        int i = (x << 8) + (z << 4) + y;
        int block = this.blocks[i] & 0xff;
        int data = this.data[i] & 0xff;
        if ((x & 1) == 0) {
            return (block << 4) | (data & 0x0F);
        }
        return (block << 4) | (data >> 4);
    }

    @Override
    public boolean setBlock(int x, int y, int z) {
        return setBlock(x, y, z, null, null);
    }

    @Override
    public boolean setBlock(int x, int y, int z, Integer blockId) {
        return setBlock(x, y, z, blockId, null);
    }

    @Override
    public boolean setBlock(int x, int y, int z, Integer blockId, Integer meta) {
        int i = (x << 8) + (z << 4) + y;
        boolean changed = false;
        if (blockId != null) {
            byte id = (byte) (blockId & 0xff);
            if (this.blocks[i] != id) {
                this.blocks[i] = id;
                changed = true;
            }
        }

        if (meta != null) {
            int old = this.data[i] & 0xff;
            if ((x & 1) == 0) {
                this.data[i] = (byte) ((((old & 0xf0) | meta & 0x0f)) & 0xff);
                if (!meta.equals(old & 0x0f)) {
                    changed = true;
                }
            } else {
                this.data[i] = (byte) ((((meta & 0x0f) << 4) | (old & 0x0f)) & 0xff);
                if (!meta.equals((old & 0xf0) >> 4)) {
                    changed = true;
                }
            }
        }

        return changed;
    }

    @Override
    public int getBlockSkyLight(int x, int y, int z) {
        int sl = this.skyLight[(x << 8) + (z << 4) + y] & 0xff;
        if ((x & 1) == 0) {
            return sl & 0x0f;
        }
        return sl >> 4;
    }

    @Override
    public void setBlockSkyLight(int x, int y, int z, int level) {
        int i = (x << 8) + (z << 4) + y;
        int old = this.skyLight[i] & 0xff;
        if ((x & 1) == 0) {
            this.skyLight[i] = (byte) (((old & 0xf0) | (level & 0x0f)) & 0xff);
        } else {
            this.skyLight[i] = (byte) ((((level & 0x0f) << 4) | (old & 0x0f)) & 0xff);
        }
    }

    @Override
    public int getBlockLight(int x, int y, int z) {
        int l = this.blockLight[(x << 8) + (z << 4) + y] & 0xff;
        if ((x & 1) == 0) {
            return l & 0x0f;
        }
        return l >> 4;
    }

    @Override
    public void setBlockLight(int x, int y, int z, int level) {
        int i = (x << 8) + (z << 4) + y;
        int old = this.blockLight[i] & 0xff;
        if ((x & 1) == 0) {
            this.blockLight[i] = (byte) (((old & 0xf0) | (level & 0x0f)) & 0xff);
        } else {
            this.blockLight[i] = (byte) ((((level & 0x0f) << 4) | (old & 0x0f)) & 0xff);
        }
    }

    @Override
    public byte[] getBlockIdColumn(int x, int z) {
        int i = (x << 8) + (z << 4);
        byte[] column = new byte[16];
        for (int y = 0; y < 16; y++) {
            column[y] = this.blocks[y + i];
        }
        return column;
    }

    @Override
    public byte[] getBlockDataColumn(int x, int z) {
        int i = (x << 8) + (z << 4);
        byte[] column = new byte[16];
        for (int y = 0; y < 16; y++) {
            column[y] = this.data[y + i];
        }
        return column;
    }

    @Override
    public byte[] getBlockSkyLightColumn(int x, int z) {
        int i = (x << 8) + (z << 4);
        byte[] column = new byte[16];
        for (int y = 0; y < 16; y++) {
            column[y] = this.skyLight[y + i];
        }
        return column;
    }

    @Override
    public byte[] getBlockLightColumn(int x, int z) {
        int i = (x << 8) + (z << 4);
        byte[] column = new byte[16];
        for (int y = 0; y < 16; y++) {
            column[y] = this.blockLight[y + i];
        }
        return column;
    }

    @Override
    public byte[] getIdArray() {
        return this.blocks;
    }

    @Override
    public byte[] getDataArray() {
        return this.data;
    }

    @Override
    public byte[] getSkyLightArray() {
        return this.skyLight;
    }

    @Override
    public byte[] getLightArray() {
        return this.blockLight;
    }

    @Override
    public boolean isAllAir() {
        for (byte b : this.blocks) {
            if ((b & 0xff) != 0x00) {
                return false;
            }
        }
        return true;
    }

    @Override
    public byte[] getBytes() {
        BinaryStream stream = new BinaryStream();
        stream.put(this.blocks);
        stream.put(this.data);
        stream.put(this.skyLight);
        stream.put(this.blockLight);
        return stream.getBuffer();
    }

    @Override
    public ChunkSection clone() {
        ChunkSection section;
        try {
            section = (ChunkSection) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
        section.skyLight = this.skyLight.clone();
        section.blockLight = this.blockLight.clone();
        section.blocks = this.blocks.clone();
        section.data = this.data.clone();
        return section;
    }
}
