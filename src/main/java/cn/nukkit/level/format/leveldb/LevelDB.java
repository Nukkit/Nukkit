package cn.nukkit.level.format.leveldb;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.ChunkSection;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.format.generic.BaseLevelProvider;
import cn.nukkit.level.format.leveldb.key.FlagsKey;
import cn.nukkit.level.format.leveldb.key.TerrainKey;
import cn.nukkit.level.format.leveldb.key.VersionKey;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class LevelDB extends BaseLevelProvider {

    protected DB db;

    protected CompoundTag levelData;

    public LevelDB(Level level, String path) throws IOException {
        super(level, path);
    }

    @Override
    public CompoundTag initLevelData() throws IOException {
        CompoundTag levelData;
        try (FileInputStream stream = new FileInputStream(this.getPath() + "level.dat")) {
            stream.skip(8);
            levelData = NBTIO.read(stream, ByteOrder.LITTLE_ENDIAN);
            if (levelData == null) {
                throw new IOException("LevelData can not be null");
            }
        } catch (IOException e) {
            throw new LevelException("Invalid level.dat");
        }

        if (!levelData.contains("generatorName")) {
            levelData.putString("generatorName", Generator.getGenerator("DEFAULT").getSimpleName().toLowerCase());
        }

        if (!levelData.contains("generatorOptions")) {
            levelData.putString("generatorOptions", "");
        }

        try {
            this.db = Iq80DBFactory.factory.open(new File(this.getPath() + "/db"), new Options().createIfMissing(true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return levelData;
    }

    public static String getProviderName() {
        return "leveldb";
    }

    public static byte getProviderOrder() {
        return ORDER_ZXY;
    }

    public static boolean usesChunkSection() {
        return false;
    }

    public static boolean isValid(String path) {
        return new File(path + "/level.dat").exists() && new File(path + "/db").isDirectory();
    }

    public static void generate(String path, String name, long seed, Class<? extends Generator> generator) throws IOException {
        generate(path, name, seed, generator, new ConcurrentHashMap<>(8, 0.9f, 1));
    }

    public static void generate(String path, String name, long seed, Class<? extends Generator> generator, Map<String, String> options) throws IOException {
        if (!new File(path + "/db").exists()) {
            new File(path + "/db").mkdirs();
        }

        CompoundTag levelData = new CompoundTag("")
                .putLong("currentTick", 0)
                .putInt("DayCycleStopTime", -1)
                .putInt("GameType", 0)
                .putInt("Generator", Generator.getGeneratorType(generator))
                .putBoolean("hasBeenLoadedInCreative", false)
                .putLong("LastPlayed", System.currentTimeMillis() / 1000)
                .putString("LevelName", name)
                .putFloat("lightningLevel", 0)
                .putInt("lightningTime", new Random().nextInt())
                .putInt("limitedWorldOriginX", 128)
                .putInt("limitedWorldOriginY", 70)
                .putInt("limitedWorldOriginZ", 128)
                .putInt("Platform", 0)
                .putFloat("rainLevel", 0)
                .putInt("rainTime", new Random().nextInt())
                .putLong("RandomSeed", seed)
                .putByte("spawnMobs", 0)
                .putInt("SpawnX", 128)
                .putInt("SpawnY", 70)
                .putInt("SpawnZ", 128)
                .putInt("storageVersion", 4)
                .putLong("Time", 0)
                .putLong("worldStartCount", ((long) Integer.MAX_VALUE) & 0xffffffffL);

        byte[] data = NBTIO.write(levelData, ByteOrder.LITTLE_ENDIAN);
        BinaryStream outputStream = new BinaryStream();
        outputStream.write(Binary.writeLInt(3));
        outputStream.write(Binary.writeLInt(data.length));
        outputStream.write(data);

        Utils.writeFile(path + "level.dat", new ByteArrayInputStream(outputStream.toByteArray()));

        DB db = Iq80DBFactory.factory.open(new File(path + "/db"), new Options().createIfMissing(true));
        db.close();
    }

    @Override
    public void saveLevelData() {
        try {
            byte[] data = NBTIO.write(levelData, ByteOrder.LITTLE_ENDIAN);
            BinaryStream outputStream = new BinaryStream();
            outputStream.write(Binary.writeLInt(3));
            outputStream.write(Binary.writeLInt(data.length));
            outputStream.write(data);

            Utils.writeFile(path + "level.dat", new ByteArrayInputStream(outputStream.toByteArray()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AsyncTask requestChunkTask(int x, int z) {
        FullChunk chunk = this.getChunk(x, z, false);
        if (chunk == null) {
            throw new ChunkException("Invalid Chunk sent");
        }

        byte[] tiles = new byte[0];

        if (!chunk.getBlockEntities().isEmpty()) {
            List<CompoundTag> tagList = new ArrayList<>();

            for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                if (blockEntity instanceof BlockEntitySpawnable) {
                    tagList.add(((BlockEntitySpawnable) blockEntity).getSpawnCompound());
                }
            }

            try {
                tiles = NBTIO.write(tagList, ByteOrder.LITTLE_ENDIAN);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Map<Integer, Integer> extra = chunk.getBlockExtraDataArray();
        BinaryStream extraData;
        if (!extra.isEmpty()) {
            extraData = new BinaryStream();
            extraData.putLInt(extra.size());
            for (Integer key : extra.values()) {
                extraData.putLInt(key);
                extraData.putLShort(extra.get(key));
            }
        } else {
            extraData = null;
        }

        BinaryStream stream = new BinaryStream();
        stream.put(chunk.getBlockIdArray());
        stream.put(chunk.getBlockDataArray());
        stream.put(chunk.getBlockSkyLightArray());
        stream.put(chunk.getBlockLightArray());
        for (int height : chunk.getHeightMapArray()) {
            stream.putByte((byte) (height & 0xff));
        }
        for (int color : chunk.getBiomeColorArray()) {
            stream.put(Binary.writeInt(color));
        }
        if (extraData != null) {
            stream.put(extraData.getBuffer());
        } else {
            stream.putLInt(0);
        }
        stream.put(tiles);

        this.getLevel().chunkRequestCallback(x, z, stream.getBuffer());

        return null;
    }

    @Override
    public BaseFullChunk loadChunk(long index, int chunkX, int chunkZ, boolean create) {
        this.level.timings.syncChunkLoadDataTimer.startTiming();
        Chunk chunk = this.readChunk(chunkX, chunkZ);
        if (chunk == null) {
            if (create) {
                chunk = Chunk.getEmptyChunk(chunkX, chunkZ, this);
            }
        } else {
            this.chunks.put(index, chunk);
        }
        this.level.timings.syncChunkLoadDataTimer.stopTiming();
        return chunk;
    }

    private Chunk readChunk(int chunkX, int chunkZ) {
        byte[] data;
        if (!this.chunkExists(chunkX, chunkZ) || (data = this.db.get(TerrainKey.create(chunkX, chunkZ).toArray())) == null) {
            return null;
        }

        byte[] flags = this.db.get(FlagsKey.create(chunkX, chunkZ).toArray());
        if (flags == null) {
            flags = new byte[]{0x03};
        }

        return Chunk.fromBinary(
                Binary.appendBytes(
                        Binary.writeLInt(chunkX),
                        Binary.writeLInt(chunkZ),
                        data,
                        flags)
                , this);
    }

    private void writeChunk(Chunk chunk) {
        byte[] binary = chunk.toBinary(true);
        this.db.put(TerrainKey.create(chunk.getX(), chunk.getZ()).toArray(), Binary.subBytes(binary, 8, binary.length - 1));
        this.db.put(FlagsKey.create(chunk.getX(), chunk.getZ()).toArray(), Binary.subBytes(binary, binary.length - 1));
        this.db.put(VersionKey.create(chunk.getX(), chunk.getZ()).toArray(), new byte[]{0x02});
    }

    @Override
    public void saveChunk(int X, int Z) {
        if (this.isChunkLoaded(X, Z)) {
            this.writeChunk((Chunk) this.getChunk(X, Z));
        }
    }

    public DB getDatabase() {
        return db;
    }

    public static ChunkSection createChunkSection(int Y) {
        return null;
    }

    private boolean chunkExists(int chunkX, int chunkZ) {
        return this.db.get(VersionKey.create(chunkX, chunkZ).toArray()) != null;
    }

    @Override
    public boolean isChunkGenerated(int chunkX, int chunkZ) {
        return this.chunkExists(chunkX, chunkZ) && this.getChunk(chunkX, chunkZ, false) != null;

    }

    @Override
    public boolean isChunkPopulated(int chunkX, int chunkZ) {
        return this.getChunk(chunkX, chunkZ) != null;
    }

    @Override
    public synchronized void close() {
        this.unloadChunks();
        try {
            this.db.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.level = null;
    }

    @Override
    public boolean isRaining() {
        return this.levelData.getFloat("rainLevel") > 0;
    }

    @Override
    public void setRaining(boolean raining) {
        this.levelData.putFloat("rainLevel", raining ? 1.0f : 0);
    }

    @Override
    public boolean isThundering() {
        return this.levelData.getFloat("lightningLevel") > 0;
    }

    @Override
    public void setThundering(boolean thundering) {
        this.levelData.putFloat("lightningLevel", thundering ? 1.0f : 0);
    }

    @Override
    public int getThunderTime() {
        return this.levelData.getInt("lightningTime");
    }

    @Override
    public void setThunderTime(int thunderTime) {
        this.levelData.putInt("lightningTime", thunderTime);
    }

    @Override
    public long getCurrentTick() {
        return this.levelData.getLong("currentTick");
    }

    @Override
    public void setCurrentTick(long currentTick) {
        this.levelData.putLong("currentTick", currentTick);
    }

    @Override
    public long getTime() {
        return this.levelData.getLong("Time");
    }

    @Override
    public void setTime(long value) {
        this.levelData.putLong("Time", value);
    }

    @Override
    public void doGarbageCollection() {

    }
}
