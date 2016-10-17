package cn.nukkit.level.format.generic;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.LevelProvider;
import cn.nukkit.level.generator.biome.Biome;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.NumberTag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class BaseFullChunk implements FullChunk {
    protected final Map<Long, Entity> entities = new ConcurrentHashMap<>(8, 0.9f, 1);

    protected final Map<Long, BlockEntity> tiles = new ConcurrentHashMap<>(8, 0.9f, 1);

    protected final Map<Integer, BlockEntity> tileList = new ConcurrentHashMap<>(8, 0.9f, 1);

    protected int[] biomeColors;

    protected byte[] blocks;

    protected byte[] data;

    protected byte[] skyLight;

    protected byte[] blockLight;

    protected int[] heightMap;

    protected List<CompoundTag> NBTtiles;

    protected List<CompoundTag> NBTentities;

    protected Map<Integer, Integer> extraData = new ConcurrentHashMap<>(8, 0.9f, 1);

    protected LevelProvider provider;
    protected Class<? extends LevelProvider> providerClass;

    private int x;
    private int z;
    private long hash;

    protected LongAdder changes = new LongAdder();

    protected boolean isInit = false;

    @Override
    public BaseFullChunk clone() {
        BaseFullChunk chunk;
        try {
            chunk = (BaseFullChunk) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
        if (this.biomeColors != null) {
            chunk.biomeColors = this.getBiomeColorArray().clone();
        }

        if (this.blocks != null) {
            chunk.blocks = this.blocks.clone();
        }

        if (this.data != null) {
            chunk.data = this.data.clone();
        }

        if (this.skyLight != null) {
            chunk.skyLight = this.skyLight.clone();
        }

        if (this.blockLight != null) {
            chunk.blockLight = this.blockLight.clone();
        }

        if (this.heightMap != null) {
            chunk.heightMap = this.getHeightMapArray().clone();
        }

        return chunk;
    }

    protected void checkOldBiomes(byte[] data) {
        if (data.length != 256) {
            return;
        }
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                Biome biome = Biome.getBiome(data[(z << 4) + x] & 0xff);
                this.setBiomeId(x, z, biome.getId());
                int c = biome.getColor();
                this.setBiomeColor(x, z, c >> 16, (c >> 8) & 0xff, c & 0xff);
            }
        }
    }

    public void initChunk() {
        if (this.getProvider() != null && !this.isInit) {
            if (this.NBTentities != null && !this.NBTentities.isEmpty()) {
                this.getProvider().getLevel().timings.syncChunkLoadEntitiesTimer.startTiming();
                for (CompoundTag nbt : NBTentities) {
                    if (!nbt.contains("id")) {
                        this.setChanged();
                        continue;
                    }
                    ListTag pos = nbt.getList("Pos");
                    if ((((NumberTag) pos.get(0)).getData().intValue() >> 4) != this.x || ((((NumberTag) pos.get(2)).getData().intValue() >> 4) != this.z)) {
                        this.changes.add(1);
                        continue;
                    }
                    Entity entity = Entity.createEntity(nbt.getString("id"), this, nbt);
                    if (entity != null) {
                        entity.spawnToAll();
                    } else {
                        this.changes.add(1);
                        continue;
                    }
                }
                this.getProvider().getLevel().timings.syncChunkLoadEntitiesTimer.stopTiming();
            }

            if (this.NBTtiles != null && !this.NBTtiles.isEmpty()) {
                this.getProvider().getLevel().timings.syncChunkLoadBlockEntitiesTimer.startTiming();
                for (CompoundTag nbt : NBTtiles) {
                    if (nbt != null) {
                        if (!nbt.contains("id")) {
                            this.changes.add(1);
                            continue;
                        }
                        if ((nbt.getInt("x") >> 4) != this.x || ((nbt.getInt("z") >> 4) != this.z)) {
                            this.changes.add(1);
                            continue;
                        }
                        BlockEntity blockEntity = BlockEntity.createBlockEntity(nbt.getString("id"), this, nbt);
                        if (blockEntity == null) {
                            this.changes.add(1);
                            continue;
                        }
                    }
                }

                this.getProvider().getLevel().timings.syncChunkLoadBlockEntitiesTimer.stopTiming();
            }
            this.NBTentities = null;
            this.NBTtiles = null;
            this.isInit = true;
        }
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getZ() {
        return z;
    }

    public void setX(int x) {
        this.x = x;
        this.hash = Level.chunkHash(x, z);
    }

    public void setZ(int z) {
        this.z = z;
        this.hash = Level.chunkHash(x, z);
    }

    @Override
    public long getIndex() {
        return hash;
    }

    @Override
    public void setPosition(int x, int z) {
        this.x = x;
        this.z = z;
        this.hash = Level.chunkHash(x, z);
    }

    @Override
    public LevelProvider getProvider() {
        return provider;
    }

    @Override
    public void setProvider(LevelProvider provider) {
        this.provider = provider;
    }

    @Override
    public int getBiomeId(int x, int z) {
        return (this.getBiomeColorArray()[(z << 4) + x] & 0xFF000000) >> 24;
    }

    @Override
    public void setBiomeId(int x, int z, int biomeId) {
        this.changes.add(1);
        this.getBiomeColorArray()[(z << 4) + x] = (this.getBiomeColorArray()[(z << 4) + x] & 0xFFFFFF) | (biomeId << 24);
    }

    @Override
    public int[] getBiomeColor(int x, int z) {
        int color = this.biomeColors[(z << 4) | x] & 0xFFFFFF;
        return new int[]{color >> 16, (color >> 8) & 0xFF, color & 0xFF};
    }

    @Override
    public void setBiomeColor(int x, int z, int R, int G, int B) {
        this.changes.add(1);
        this.getBiomeColorArray()[(z << 4) + x] = (this.getBiomeColorArray()[(z << 4) + x] & 0xFF000000) | ((R & 0xFF) << 16) | ((G & 0xFF) << 8) | (B & 0XFF);
    }

    @Override
    public int getHeightMap(int x, int z) {
        return this.heightMap[(z << 4) + x];
    }

    @Override
    public void setHeightMap(int x, int z, int value) {
        this.heightMap[(z << 4) + x] = value;
    }

    @Override
    public void recalculateHeightMap() {
        for (int z = 0; z < 16; ++z) {
            for (int x = 0; x < 16; ++x) {
                this.setHeightMap(x, z, this.getHighestBlockAt(x, z, false));
            }
        }
    }

    @Override
    public int getBlockExtraData(int x, int y, int z) {
        int index = Level.chunkBlockHash(x, y, z);
        if (this.extraData.containsKey(index)) {
            return this.extraData.get(index);
        }

        return 0;
    }

    @Override
    public void setBlockExtraData(int x, int y, int z, int data) {
        if (data == 0) {
            this.extraData.remove(Level.chunkBlockHash(x, y, z));
        } else {
            this.extraData.put(Level.chunkBlockHash(x, y, z), data);
        }

        this.setChanged(true);
    }

    @Override
    public void populateSkyLight() {
        for (int z = 0; z < 16; ++z) {
            for (int x = 0; x < 16; ++x) {
                int top = this.getHeightMap(x, z);
                for (int y = 127; y > top; --y) {
                    this.setBlockSkyLight(x, y, z, 15);
                }
                for (int y = top; y >= 0; --y) {
                    if (Block.solid[this.getBlockId(x, y, z)]) {
                        break;
                    }
                    this.setBlockSkyLight(x, y, z, 15);
                }
                this.setHeightMap(x, z, this.getHighestBlockAt(x, z, false));
            }
        }
    }

    @Override
    public int getHighestBlockAt(int x, int z) {
        return this.getHighestBlockAt(x, z, true);
    }

    @Override
    public int getHighestBlockAt(int x, int z, boolean cache) {
        if (cache) {
            int h = this.getHeightMap(x, z);
            if (h != 0 && h != 127) {
                return h;
            }
        }
        byte[] column = this.getBlockIdColumn(x, z);
        for (int y = 127; y >= 0; --y) {
            if (column[y] != 0x00) {
                this.setHeightMap(x, z, y);
                return y;
            }
        }
        return 0;
    }

    @Override
    public void addEntity(Entity entity) {
        this.entities.put(entity.getId(), entity);
        if (!(entity instanceof Player) && this.isInit) {
            this.changes.add(1);
        }
    }

    @Override
    public void removeEntity(Entity entity) {
        this.entities.remove(entity.getId());
        if (!(entity instanceof Player) && this.isInit) {
            this.changes.add(1);
        }
    }

    @Override
    public void addBlockEntity(BlockEntity blockEntity) {
        this.tiles.put(blockEntity.getId(), blockEntity);
        int index = (((int) blockEntity.z & 0x0f) << 12) | (((int) blockEntity.x & 0x0f) << 8) | ((int) blockEntity.y & 0xff);
        if (this.tileList.containsKey(index) && !this.tileList.get(index).equals(blockEntity)) {
            this.tileList.get(index).close();
        }
        this.tileList.put(index, blockEntity);
        if (this.isInit) {
            this.changes.add(1);
        }
    }

    @Override
    public void removeBlockEntity(BlockEntity blockEntity) {
        this.tiles.remove(blockEntity.getId());
        int index = ((blockEntity.getFloorZ() & 0x0f) << 12) | ((blockEntity.getFloorX() & 0x0f) << 8) | (blockEntity.getFloorY() & 0xff);
        this.tileList.remove(index);
        if (this.isInit) {
            this.changes.add(1);
        }
    }

    @Override
    public Map<Long, Entity> getEntities() {
        return entities;
    }

    @Override
    public Map<Long, BlockEntity> getBlockEntities() {
        return tiles;
    }

    @Override
    public Map<Integer, Integer> getBlockExtraDataArray() {
        return this.extraData;
    }

    @Override
    public BlockEntity getTile(int x, int y, int z) {
        int index = (z << 12) | (x << 8) | y;
        return this.tileList.containsKey(index) ? this.tileList.get(index) : null;
    }

    @Override
    public boolean isLoaded() {
        return this.getProvider() != null && this.getProvider().isChunkLoaded(this.getX(), this.getZ());
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
    public boolean unload() throws Exception {
        return this.unload(true, true);
    }

    @Override
    public boolean unload(boolean save) throws Exception {
        return this.unload(save, true);
    }

    @Override
    public boolean unload(boolean save, boolean safe) {
        LevelProvider level = this.getProvider();
        if (level == null) {
            return true;
        }
        if (save && this.resetChanged() > 0) {
            level.saveChunk(this.getX(), this.getZ());
        }
        if (safe) {
            for (Entity entity : this.getEntities().values()) {
                if (entity instanceof Player) {
                    return false;
                }
            }
        }
        for (Entity entity : new ArrayList<>(this.getEntities().values())) {
            if (entity instanceof Player) {
                continue;
            }
            entity.close();
        }

        for (BlockEntity blockEntity : new ArrayList<>(this.getBlockEntities().values())) {
            blockEntity.close();
        }
        this.provider = null;
        return true;
    }

    @Override
    public byte[] getBlockIdArray() {
        return this.blocks;
    }

    @Override
    public byte[] getBlockDataArray() {
        return this.data;
    }

    @Override
    public byte[] getBlockSkyLightArray() {
        return this.skyLight;
    }

    @Override
    public byte[] getBlockLightArray() {
        return this.blockLight;
    }

    @Override
    public byte[] getBiomeIdArray() {
        byte[] ids = new byte[this.getBiomeColorArray().length];
        for (int i = 0; i < this.getBiomeColorArray().length; i++) {
            int d = this.getBiomeColorArray()[i];
            ids[i] = (byte) ((d & 0xFF000000) >> 24);
        }
        return ids;
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
    public boolean hasChanged() {
        return this.changes.longValue() > 0;
    }

    public long getChanges() {
        return this.changes.sum();
    }

    @Override
    public void setChanged() {
        this.setChanged(true);
    }

    @Override
    public void setChanged(boolean changed) {
        if (changed) {
            this.changes.add(1);
        } else {
            this.changes.add(this.changes.sum());
        }
    }

    @Override
    public long resetChanged() {
        long sum = this.changes.sum();
        this.changes.add(-sum);
        return sum;
    }

    @Override
    public byte[] toFastBinary() {
        return this.toBinary();
    }

    @Override
    public boolean isLightPopulated() {
        return true;
    }

    @Override
    public void setLightPopulated() {
        this.setLightPopulated(true);
    }

    @Override
    public void setLightPopulated(boolean value) {

    }


    @Override
    public int getBlockId(int x, int y, int z) {
        return this.blocks[(x << 11) | (z << 7) | y] & 0xff;
    }

    @Override
    public void setBlockId(int x, int y, int z, int id) {
        this.blocks[(x << 11) | (z << 7) | y] = (byte) (id);
        this.changes.add(1);
    }

    @Override
    public int getBlockData(int x, int y, int z) {
        int b = this.data[(x << 10) | (z << 6) | (y >> 1)] & 0xff;
        if ((y & 1) == 0) {
            return b & 0x0f;
        } else {
            return b >> 4;
        }
    }

    @Override
    public void setBlockData(int x, int y, int z, int data) {
        int i = (x << 10) | (z << 6) | (y >> 1);
        int old = this.data[i] & 0xff;
        if ((y & 1) == 0) {
            this.data[i] = (byte) ((old & 0xf0) | (old & 0x0f));
        } else {
            this.data[i] = (byte) (((data & 0x0f) << 4) | (old & 0x0f));
        }
        this.changes.add(1);
    }

    @Override
    public int getFullBlock(int x, int y, int z) {
        int i = (x << 11) | (z << 7) | y;
        int block = this.blocks[i] & 0xff;
        synchronized (this) {
            int data = this.data[i >> 1] & 0xff;
            if ((y & 1) == 0) {
                return (block << 4) | (data & 0x0f);
            } else {
                return (block << 4) | (data >> 4);
            }
        }
    }

    @Override
    public boolean setBlock(int x, int y, int z, int blockId) {
        int i = (x << 11) | (z << 7) | y;
        byte id = (byte) (blockId);
        if (this.blocks[i] != id) {
            this.blocks[i] = id;
            return true;
        }
        return false;
    }

    @Override
    public Block getAndSetBlock(int x, int y, int z, Block block) {
        int i = (x << 11) | (z << 7) | y;
        int idPrevious = this.blocks[i] & 0xFF;
        if (idPrevious != block.getId()) {
            this.changes.add(1);
            this.blocks[i] = (byte) block.getId();
            if (Block.mightHaveMeta(block.getId())) {
                i >>= 1;
                int old = this.data[i] & 0xff;
                if ((y & 1) == 0) {
                    int previousData = (old & 0xf0);
                    this.data[i] = (byte) ((old & 0xf0) | (block.getDamage() & 0x0f));
                    return Block.fullList[(idPrevious << 4) + previousData];
                } else {
                    int previousData = (old & 0x0f);
                    this.data[i] = (byte) (((block.getDamage() & 0x0f) << 4) | previousData);
                    return Block.fullList[(idPrevious << 4) + previousData];
                }
            }
            return Block.fullList[idPrevious << 4];
        } else if (Block.mightHaveMeta(idPrevious & 0xFF)) {
            i >>= 1;
            int old = this.data[i] & 0xff;
            if ((y & 1) == 0) {
                int previousData = (old & 0xf0);
                if (previousData != block.getDamage()) {
                    this.changes.add(1);
                }
                this.data[i] = (byte) ((old & 0xf0) | (block.getDamage() & 0x0f));
                return Block.fullList[(idPrevious << 4) + previousData];
            } else {
                int previousData = (old & 0x0f);
                if (previousData != block.getDamage()) {
                    this.changes.add(1);
                }
                this.data[i] = (byte) (((block.getDamage() & 0x0f) << 4) | previousData);
                return Block.fullList[(idPrevious << 4) + previousData];
            }
        }
        return block;
    }

    @Override
    public boolean setBlock(int x, int y, int z, int blockId, int meta) {
        int i = (x << 11) | (z << 7) | y;
        boolean changed = false;
        int idPrevious = this.blocks[i] & 0xFF;
        if (idPrevious != blockId) {
            this.blocks[i] = (byte) blockId;
            this.changes.add(1);
            if (Block.mightHaveMeta(blockId)) {
                i >>= 1;
                int old = this.data[i] & 0xff;
                if ((y & 1) == 0) {
                    this.data[i] = (byte) ((old & 0xf0) | (meta & 0x0f));
                } else {
                    this.data[i] = (byte) (((meta & 0x0f) << 4) | (old & 0x0f));
                }
            }
        } else if (Block.mightHaveMeta(idPrevious & 0xFF)) {
            i >>= 1;
            int old = this.data[i] & 0xff;
            if ((y & 1) == 0) {
                this.data[i] = (byte) ((old & 0xf0) | (meta & 0x0f));
                if ((old & 0x0f) != meta) {
                    this.changes.add(1);
                }
            } else {
                this.data[i] = (byte) (((meta & 0x0f) << 4) | (old & 0x0f));
                if (meta != ((old & 0xf0) >> 4)) {
                    this.changes.add(1);
                }
            }
        }
        return changed;
    }

    @Override
    public int getBlockSkyLight(int x, int y, int z) {
        int sl = this.skyLight[(x << 10) | (z << 6) | (y >> 1)] & 0xff;
        if ((y & 1) == 0) {
            return sl & 0x0f;
        } else {
            return sl >> 4;
        }
    }

    @Override
    public void setBlockSkyLight(int x, int y, int z, int level) {
        int i = (x << 10) | (z << 6) | (y >> 1);
        int old = this.skyLight[i] & 0xff;
        if ((y & 1) == 0) {
            this.skyLight[i] = (byte) ((old & 0xf0) | (level & 0x0f));
        } else {
            this.skyLight[i] = (byte) (((level & 0x0f) << 4) | (old & 0x0f));
        }
        this.changes.add(1);
    }

    @Override
    public int getBlockLight(int x, int y, int z) {
        int b = this.blockLight[(x << 10) | (z << 6) | (y >> 1)] & 0xff;
        if ((y & 1) == 0) {
            return b & 0x0f;
        } else {
            return b >> 4;
        }
    }

    @Override
    public void setBlockLight(int x, int y, int z, int level) {
        int i = (x << 10) | (z << 6) | (y >> 1);
        int old = this.blockLight[i] & 0xff;
        if ((y & 1) == 0) {
            this.blockLight[i] = (byte) ((old & 0xf0) | (level & 0x0f));
        } else {
            this.blockLight[i] = (byte) (((level & 0x0f) << 4) | (old & 0x0f));
        }
        this.changes.add(1);
    }

    @Override
    public byte[] getBlockIdColumn(int x, int z) {
        byte[] b = new byte[128];
        System.arraycopy(this.blocks, (x << 11) + (z << 7), b, 0, 128);
        return b;
    }

    @Override
    public byte[] getBlockDataColumn(int x, int z) {
        byte[] b = new byte[64];
        System.arraycopy(this.data, (x << 10) + (z << 6), b, 0, 64);
        return b;
    }

    @Override
    public byte[] getBlockSkyLightColumn(int x, int z) {
        byte[] b = new byte[64];
        System.arraycopy(this.skyLight, (x << 10) + (z << 6), b, 0, 64);
        return b;
    }

    @Override
    public byte[] getBlockLightColumn(int x, int z) {
        byte[] b = new byte[64];
        System.arraycopy(this.blockLight, (x << 10) + (z << 6), b, 0, 64);
        return b;
    }

}
