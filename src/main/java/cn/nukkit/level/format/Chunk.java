package cn.nukkit.level.format;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public interface Chunk extends FullChunk {
    byte SUB_CHUNK_COUNT = 16;

    boolean isSubChunkEmpty(float fY);

    SubChunk getSubChunk(float fY);

    boolean setSubChunk(float fY, SubChunk subChunk);

    SubChunk[] getSubChunks();

    class Entry {
        public final int chunkX;
        public final int chunkZ;

        public Entry(int chunkX, int chunkZ) {
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
        }
    }
}
