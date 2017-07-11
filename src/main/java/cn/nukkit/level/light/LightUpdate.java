package cn.nukkit.level.light;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockVector3;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * author: dktapps
 */

//TODO: make light updates asynchronous
public abstract class LightUpdate {

    protected ChunkManager level;

    protected Queue<Entry> spreadQueue;

    protected Set<BlockVector3> spreadVisited = new HashSet<>();

    protected Queue<Entry> removalQueue;

    protected Set<BlockVector3> removalVisited = new HashSet<>();

    public LightUpdate(ChunkManager level) {
        this.level = level;
        this.removalQueue = new ArrayDeque<>();
        this.spreadQueue = new ArrayDeque<>();
    }

    public void addSpreadNode(int x, int y, int z) {
        this.spreadQueue.add(new Entry(new BlockVector3(x, y, z)));
    }

    public void addRemoveNode(int x, int y, int z, int oldLight) {
        this.spreadQueue.add(new Entry(new BlockVector3(x, y, z), oldLight));
    }

    abstract protected int getLight(int x, int y, int z);

    abstract protected void setLight(int x, int y, int z, int level);

    public void setAndUpdateLight(int x, int y, int z, int newLevel) {

        BlockVector3 index;

        if (spreadVisited.contains(index = Level.blockHash(x, y, z)) || removalVisited.contains(index)) {
            return;
        }

        int oldLevel = this.getLight(x, y, z);

        if (oldLevel != newLevel) {
            this.setLight(x, y, z, newLevel);
            if (oldLevel < newLevel) { //light increased
                this.spreadVisited.add(index);
                this.spreadQueue.add(new Entry(new BlockVector3(x, y, z)));
            } else { //light removed
                this.removalVisited.add(index);
                this.removalQueue.add(new Entry(new BlockVector3(x, y, z), oldLevel));
            }
        }
    }

    public void execute() {
        while (!this.removalQueue.isEmpty()) {
            Entry entry = this.removalQueue.poll();
            int x = entry.pos.x;
            int y = entry.pos.y;
            int z = entry.pos.z;

            int[][] points = new int[][]{
                    {x + 1, y, z},
                    {x - 1, y, z},
                    {x, y + 1, z},
                    {x, y - 1, z},
                    {x, y, z + 1},
                    {x, y, z - 1}
            };

            for (int[] i : points) {
                this.computeRemoveLight(i[0], i[1], i[2], entry.oldLight);
            }
        }

        while (!spreadQueue.isEmpty()) {
            Entry entry = this.spreadQueue.poll();
            int x = entry.pos.x;
            int y = entry.pos.y;
            int z = entry.pos.z;

            int newAdjacentLight = this.getLight(x, y, z);
            if (newAdjacentLight <= 0) {
                continue;
            }

            int[][] points = new int[][]{
                    {x + 1, y, z},
                    {x - 1, y, z},
                    {x, y + 1, z},
                    {x, y - 1, z},
                    {x, y, z + 1},
                    {x, y, z - 1}
            };

            for (int[] i : points) {
                this.computeSpreadLight(i[0], i[1], i[2], newAdjacentLight);
            }
        }
    }

    protected void computeRemoveLight(int x, int y, int z, int oldAdjacentLevel) {
        int current = this.getLight(x, y, z);

        BlockVector3 index;
        if (current != 0 && current < oldAdjacentLevel) {
            this.setLight(x, y, z, 0);

            if (!removalVisited.contains(index = Level.blockHash(x, y, z))) {
                removalVisited.add(index);
                if (current > 1) {
                    this.removalQueue.add(new Entry(new BlockVector3(x, y, z), current));
                }
            }
        } else if (current >= oldAdjacentLevel) {
            if (!spreadVisited.contains(index = Level.blockHash(x, y, z))) {
                spreadVisited.add(index);
                spreadQueue.add(new Entry(new BlockVector3(x, y, z)));
            }
        }
    }

    protected void computeSpreadLight(int x, int y, int z, int newAdjacentLevel) {
        int current = this.getLight(x, y, z);
        int potentialLight = newAdjacentLevel - Block.lightFilter[this.level.getBlockIdAt(x, y, z)];

        if (current < potentialLight) {
            this.setLight(x, y, z, potentialLight);

            BlockVector3 index;
            if (!spreadVisited.contains(index = Level.blockHash(x, y, z))) {
                spreadVisited.add(index);
                if (potentialLight > 1) {
                    spreadQueue.add(new Entry(new BlockVector3(x, y, z)));
                }
            }
        }
    }

    private class Entry {

        public BlockVector3 pos;

        public Integer oldLight;

        public Entry(BlockVector3 pos) {
            this(pos, null);
        }

        public Entry(BlockVector3 pos, Integer light) {
            this.pos = pos;
            this.oldLight = light;
        }
    }
}