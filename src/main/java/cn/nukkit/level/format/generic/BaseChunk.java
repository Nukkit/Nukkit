package cn.nukkit.level.format.generic;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.format.Chunk;
import cn.nukkit.level.format.ChunkSection;
import cn.nukkit.level.format.LevelProvider;
import cn.nukkit.utils.ChunkException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * author: MagicDroidX
 * Nukkit Project
 */

public abstract class BaseChunk extends BaseFullChunk implements Chunk {

    protected ChunkSection[] sections = new ChunkSection[SECTION_COUNT];

    @Override
    public BaseChunk clone() {
        BaseChunk chunk = (BaseChunk) super.clone();
        chunk.biomeColors = this.getBiomeColorArray().clone();
        chunk.heightMap = this.getHeightMapArray().clone();
        if (sections != null && sections[0] != null) {
            chunk.sections = new ChunkSection[sections.length];
            for (int i = 0; i < sections.length; i++) {
                chunk.sections[i] = sections[i].clone();
            }
        }
        return chunk;
    }

    @Override
    public int getFullBlock(int x, int y, int z) {
        return this.sections[y >> 4].getFullBlock(x, y & 0x0f, z);
    }

    @Override
    public Block getAndSetBlock(int x, int y, int z, Block block) {
        int Y = y >> 4;
        try {
            this.changes.add(1);
            return this.sections[Y].getAndSetBlock(x, y & 0x0f, z, block);
        } catch (ChunkException e) {
            try {
                this.setInternalSection(Y, (ChunkSection) this.providerClass.getMethod("createChunkSection", int.class).invoke(this.providerClass, Y));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                Server.getInstance().getLogger().logException(e1);
            }
            return this.sections[Y].getAndSetBlock(x, y & 0x0f, z, block);
        }
    }
    @Override
    public boolean setBlock(int x, int y, int z, int blockId) {
        return this.setBlock(x, y, z, blockId, 0);
    }

    @Override
    public boolean setBlock(int x, int y, int z, int blockId, int meta) {
        int Y = y >> 4;
        try {
            this.changes.add(1);
            return this.sections[Y].setBlock(x, y & 0x0f, z, blockId, meta);
        } catch (ChunkException e) {
            try {
                this.setInternalSection(Y, (ChunkSection) this.providerClass.getMethod("createChunkSection", int.class).invoke(this.providerClass, Y));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                Server.getInstance().getLogger().logException(e1);
            }
            return this.sections[Y].setBlock(x, y & 0x0f, z, blockId, meta);
        }
    }

    @Override
    public int getBlockId(int x, int y, int z) {
        return this.sections[y >> 4].getBlockId(x, y & 0x0f, z);
    }

    @Override
    public void setBlockId(int x, int y, int z, int id) {
        int Y = y >> 4;
        try {
            this.changes.add(1);
            this.sections[Y].setBlockId(x, y & 0x0f, z, id);
        } catch (ChunkException e) {
            try {
                this.setInternalSection(Y, (ChunkSection) this.providerClass.getMethod("createChunkSection", int.class).invoke(this.providerClass, Y));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                Server.getInstance().getLogger().logException(e1);
            }
            this.sections[Y].setBlockId(x, y & 0x0f, z, id);
        }
    }

    @Override
    public int getBlockData(int x, int y, int z) {
        return this.sections[y >> 4].getBlockData(x, y & 0x0f, z);
    }

    @Override
    public void setBlockData(int x, int y, int z, int data) {
        int Y = y >> 4;
        try {
            this.changes.add(1);
            this.sections[Y].setBlockData(x, y & 0x0f, z, data);
        } catch (ChunkException e) {
            try {
                this.setInternalSection(Y, (ChunkSection) this.providerClass.getMethod("createChunkSection", int.class).invoke(this.providerClass, Y));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                Server.getInstance().getLogger().logException(e1);
            }
            this.sections[Y].setBlockData(x, y & 0x0f, z, data);
        }
    }

    @Override
    public int getBlockSkyLight(int x, int y, int z) {
        return this.sections[y >> 4].getBlockSkyLight(x, y & 0x0f, z);
    }

    @Override
    public void setBlockSkyLight(int x, int y, int z, int level) {
        int Y = y >> 4;
        try {
            this.changes.add(1);
            this.sections[Y].setBlockSkyLight(x, y & 0x0f, z, level);
        } catch (ChunkException e) {
            try {
                this.setInternalSection(Y, (ChunkSection) this.providerClass.getMethod("createChunkSection", int.class).invoke(this.providerClass, Y));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                Server.getInstance().getLogger().logException(e1);
            }
            this.sections[Y].setBlockSkyLight(x, y & 0x0f, z, level);
        }
    }

    @Override
    public int getBlockLight(int x, int y, int z) {
        return this.sections[y >> 4].getBlockLight(x, y & 0x0f, z);
    }

    @Override
    public void setBlockLight(int x, int y, int z, int level) {
        int Y = y >> 4;
        try {
            this.changes.add(1);
            this.sections[Y].setBlockLight(x, y & 0x0f, z, level);
        } catch (ChunkException e) {
            try {
                this.setInternalSection(Y, (ChunkSection) this.providerClass.getMethod("createChunkSection", int.class).invoke(this.providerClass, Y));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                Server.getInstance().getLogger().logException(e1);
            }
            this.sections[Y].setBlockLight(x, y & 0x0f, z, level);
        }
    }

    @Override
    public byte[] getBlockIdColumn(int x, int z) {
        ByteBuffer buffer = ByteBuffer.allocate(128);
        for (int y = 0; y < SECTION_COUNT; y++) {
            buffer.put(this.sections[y].getBlockIdColumn(x, z));
        }
        return buffer.array();
    }

    @Override
    public byte[] getBlockDataColumn(int x, int z) {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        for (int y = 0; y < SECTION_COUNT; y++) {
            buffer.put(this.sections[y].getBlockDataColumn(x, z));
        }
        return buffer.array();
    }

    @Override
    public byte[] getBlockSkyLightColumn(int x, int z) {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        for (int y = 0; y < SECTION_COUNT; y++) {
            buffer.put(this.sections[y].getBlockSkyLightColumn(x, z));
        }
        return buffer.array();
    }

    @Override
    public byte[] getBlockLightColumn(int x, int z) {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        for (int y = 0; y < SECTION_COUNT; y++) {
            buffer.put(this.sections[y].getBlockLightColumn(x, z));
        }
        return buffer.array();
    }

    @Override
    public boolean isSectionEmpty(float fY) {
        return this.sections[(int) fY] instanceof EmptyChunkSection;
    }

    @Override
    public ChunkSection getSection(float fY) {
        return this.sections[(int) fY];
    }

    @Override
    public boolean setSection(float fY, ChunkSection section) {
        byte[] emptyIdArray = new byte[4096];
        byte[] emptyDataArray = new byte[2048];
        Arrays.fill(emptyIdArray, (byte) 0x00);
        Arrays.fill(emptyDataArray, (byte) 0x00);
        if (Arrays.equals(emptyIdArray, section.getIdArray()) && Arrays.equals(emptyDataArray, section.getDataArray())) {
            this.sections[(int) fY] = new EmptyChunkSection((int) fY);
        } else {
            this.sections[(int) fY] = section;
        }
        this.changes.add(1);
        return true;
    }

    private void setInternalSection(float fY, ChunkSection section) {
        this.sections[(int) fY] = section;
        this.changes.add(1);
    }

    @Override
    public boolean load() throws IOException {
        return this.load(true);
    }

    @Override
    public boolean load(boolean generate) throws IOException {
        return this.getProvider() != null && this.getProvider().getChunk(this.getX(), this.getZ(), true) != null;
    }

    @Override
    public byte[] getBlockIdArray() {
        ByteBuffer buffer = ByteBuffer.allocate(4096 * SECTION_COUNT);
        for (int y = 0; y < SECTION_COUNT; y++) {
            buffer.put(this.sections[y].getIdArray());
        }
        return buffer.array();
    }

    @Override
    public byte[] getBlockDataArray() {
        ByteBuffer buffer = ByteBuffer.allocate(2048 * SECTION_COUNT);
        for (int y = 0; y < SECTION_COUNT; y++) {
            buffer.put(this.sections[y].getDataArray());
        }
        return buffer.array();
    }

    @Override
    public byte[] getBlockSkyLightArray() {
        ByteBuffer buffer = ByteBuffer.allocate(2048 * SECTION_COUNT);
        for (int y = 0; y < SECTION_COUNT; y++) {
            buffer.put(this.sections[y].getSkyLightArray());
        }
        return buffer.array();
    }

    @Override
    public byte[] getBlockLightArray() {
        ByteBuffer buffer = ByteBuffer.allocate(2048 * SECTION_COUNT);
        for (int y = 0; y < SECTION_COUNT; y++) {
            buffer.put(this.sections[y].getLightArray());
        }
        return buffer.array();
    }

    @Override
    public ChunkSection[] getSections() {
        return sections;
    }

    @Override
    public int[] getBiomeColorArray() {
        return this.biomeColors;
    }

    @Override
    public int[] getHeightMapArray() {
        return this.heightMap;
    }

    @Override
    public LevelProvider getProvider() {
        return this.provider;
    }
}
