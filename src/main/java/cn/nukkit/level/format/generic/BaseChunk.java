package cn.nukkit.level.format.generic;

import cn.nukkit.Server;
import cn.nukkit.level.format.Chunk;
import cn.nukkit.level.format.LevelProvider;
import cn.nukkit.level.format.SubChunk;
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

    protected SubChunk[] subChunks = new SubChunk[SUB_CHUNK_COUNT];

    @Override
    public BaseChunk clone() {
        BaseChunk chunk = (BaseChunk) super.clone();
        chunk.biomeColors = this.getBiomeColorArray().clone();
        chunk.heightMap = this.getHeightMapArray().clone();
        if (subChunks != null && subChunks[0] != null) {
            chunk.subChunks = new SubChunk[subChunks.length];
            for (int i = 0; i < subChunks.length; i++) {
                chunk.subChunks[i] = subChunks[i].clone();
            }
        }
        return chunk;
    }

    @Override
    public int getFullBlock(int x, int y, int z) {
        return this.subChunks[y >> 4].getFullBlock(x, y & 0x0f, z);
    }

    @Override
    public boolean setBlock(int x, int y, int z) {
        return this.setBlock(x, y, z, null, null);
    }

    @Override
    public boolean setBlock(int x, int y, int z, Integer blockId) {
        return this.setBlock(x, y, z, blockId, null);
    }

    @Override
    public boolean setBlock(int x, int y, int z, Integer blockId, Integer meta) {
        int id = blockId == null ? 0 : blockId;
        int damage = meta == null ? 0 : meta;
        try {
            this.hasChanged = true;
            return this.subChunks[y >> 4].setBlock(x, y & 0x0f, z, id, damage);
        } catch (ChunkException e) {
            int Y = y >> 4;
            try {
                this.setInternalSubChunk(Y, (SubChunk) this.providerClass.getMethod("createSubChunk", int.class).invoke(this.providerClass, Y));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                Server.getInstance().getLogger().logException(e1);
            }
            return this.subChunks[y >> 4].setBlock(x, y & 0x0f, z, id, damage);
        }
    }

    @Override
    public int getBlockId(int x, int y, int z) {
        return this.subChunks[y >> 4].getBlockId(x, y & 0x0f, z);
    }

    @Override
    public void setBlockId(int x, int y, int z, int id) {
        try {
            this.subChunks[y >> 4].setBlockId(x, y & 0x0f, z, id);
            this.hasChanged = true;
        } catch (ChunkException e) {
            int Y = y >> 4;
            try {
                this.setInternalSubChunk(Y, (SubChunk) this.providerClass.getMethod("createSubChunk", int.class).invoke(this.providerClass, Y));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                Server.getInstance().getLogger().logException(e1);
            }
            this.setBlockId(x, y, z, id);
        }
    }

    @Override
    public int getBlockData(int x, int y, int z) {
        return this.subChunks[y >> 4].getBlockData(x, y & 0x0f, z);
    }

    @Override
    public void setBlockData(int x, int y, int z, int data) {
        try {
            this.subChunks[y >> 4].setBlockData(x, y & 0x0f, z, data);
            this.hasChanged = true;
        } catch (ChunkException e) {
            int Y = y >> 4;
            try {
                this.setInternalSubChunk(Y, (SubChunk) this.providerClass.getMethod("createSubChunk", int.class).invoke(this.providerClass, Y));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                Server.getInstance().getLogger().logException(e1);
            }
            this.setBlockData(x, y, z, data);
        }
    }

    @Override
    public int getBlockSkyLight(int x, int y, int z) {
        return this.subChunks[y >> 4].getBlockSkyLight(x, y & 0x0f, z);
    }

    @Override
    public void setBlockSkyLight(int x, int y, int z, int level) {
        try {
            this.subChunks[y >> 4].setBlockSkyLight(x, y & 0x0f, z, level);
            this.hasChanged = true;
        } catch (ChunkException e) {
            int Y = y >> 4;
            try {
                this.setInternalSubChunk(Y, (SubChunk) this.providerClass.getMethod("createSubChunk", int.class).invoke(this.providerClass, Y));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                Server.getInstance().getLogger().logException(e1);
            }
            this.setBlockSkyLight(x, y, z, level);
        }
    }

    @Override
    public int getBlockLight(int x, int y, int z) {
        return this.subChunks[y >> 4].getBlockLight(x, y & 0x0f, z);
    }

    @Override
    public void setBlockLight(int x, int y, int z, int level) {
        try {
            this.subChunks[y >> 4].setBlockLight(x, y & 0x0f, z, level);
            this.hasChanged = true;
        } catch (ChunkException e) {
            int Y = y >> 4;
            try {
                this.setInternalSubChunk(Y, (SubChunk) this.providerClass.getMethod("createSubChunk", int.class).invoke(this.providerClass, Y));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                Server.getInstance().getLogger().logException(e1);
            }
            this.setBlockLight(x, y, z, level);
        }
    }

    @Override
    public byte[] getBlockIdColumn(int x, int z) {
        ByteBuffer buffer = ByteBuffer.allocate(256);
        for (int y = 0; y < SUB_CHUNK_COUNT; y++) {
            buffer.put(this.subChunks[y].getBlockIdColumn(x, z));
        }
        return buffer.array();
    }

    @Override
    public byte[] getBlockDataColumn(int x, int z) {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        for (int y = 0; y < SUB_CHUNK_COUNT; y++) {
            buffer.put(this.subChunks[y].getBlockDataColumn(x, z));
        }
        return buffer.array();
    }

    @Override
    public byte[] getBlockSkyLightColumn(int x, int z) {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        for (int y = 0; y < SUB_CHUNK_COUNT; y++) {
            buffer.put(this.subChunks[y].getBlockSkyLightColumn(x, z));
        }
        return buffer.array();
    }

    @Override
    public byte[] getBlockLightColumn(int x, int z) {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        for (int y = 0; y < SUB_CHUNK_COUNT; y++) {
            buffer.put(this.subChunks[y].getBlockLightColumn(x, z));
        }
        return buffer.array();
    }

    @Override
    public boolean isSubChunkEmpty(float fY) {
        return this.subChunks[(int) fY] instanceof EmptySubChunk;
    }

    @Override
    public SubChunk getSubChunk(float fY) {
        return this.subChunks[(int) fY];
    }

    @Override
    public boolean setSubChunk(float fY, SubChunk subChunk) {
        byte[] emptyIdArray = new byte[4096];
        byte[] emptyDataArray = new byte[2048];
        if (Arrays.equals(emptyIdArray, subChunk.getIdArray()) && Arrays.equals(emptyDataArray, subChunk.getDataArray())) {
            this.subChunks[(int) fY] = new EmptySubChunk((int) fY);
        } else {
            this.subChunks[(int) fY] = subChunk;
        }
        this.hasChanged = true;
        return true;
    }

    private void setInternalSubChunk(float fY, SubChunk subChunk) {
        this.subChunks[(int) fY] = subChunk;
        this.hasChanged = true;
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
        ByteBuffer buffer = ByteBuffer.allocate(4096 * SUB_CHUNK_COUNT);
        for (int y = 0; y < SUB_CHUNK_COUNT; y++) {
            buffer.put(this.subChunks[y].getIdArray());
        }
        return buffer.array();
    }

    @Override
    public byte[] getBlockDataArray() {
        ByteBuffer buffer = ByteBuffer.allocate(2048 * SUB_CHUNK_COUNT);
        for (int y = 0; y < SUB_CHUNK_COUNT; y++) {
            buffer.put(this.subChunks[y].getDataArray());
        }
        return buffer.array();
    }

    @Override
    public byte[] getBlockSkyLightArray() {
        ByteBuffer buffer = ByteBuffer.allocate(2048 * SUB_CHUNK_COUNT);
        for (int y = 0; y < SUB_CHUNK_COUNT; y++) {
            buffer.put(this.subChunks[y].getSkyLightArray());
        }
        return buffer.array();
    }

    @Override
    public byte[] getBlockLightArray() {
        ByteBuffer buffer = ByteBuffer.allocate(2048 * SUB_CHUNK_COUNT);
        for (int y = 0; y < SUB_CHUNK_COUNT; y++) {
            buffer.put(this.subChunks[y].getLightArray());
        }
        return buffer.array();
    }

    @Override
    public SubChunk[] getSubChunks() {
        return subChunks;
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
