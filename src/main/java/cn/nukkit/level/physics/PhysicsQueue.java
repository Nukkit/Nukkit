package cn.nukkit.level.physics;

import cn.nukkit.level.Level;
import cn.nukkit.level.range.EffectIterator;
import cn.nukkit.level.range.EffectRange;
import cn.nukkit.math.BlockVector3;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Nukkit Project
 */
public class PhysicsQueue {
    private Level level;
    private ConcurrentLinkedQueue<PhysicsUpdate> asyncQueue = new ConcurrentLinkedQueue<>();
    private UpdateQueue updateQueue = new UpdateQueue();

    public PhysicsQueue(Level level) {
        this.level = level;
    }

    public boolean commitAsyncQueue() {
        boolean updated = false;
        PhysicsUpdate update;
        EffectIterator ei = new EffectIterator();
        while ((update = asyncQueue.poll()) != null) {
            updated = true;
            update.getRange().initEffectIterator(ei);
            int x = update.getX();
            int y = update.getY();
            int z = update.getZ();
            while (ei.hasNext()) {
                BlockVector3 v = ei.next();
                int ox = x + v.getX();
                int oy = y + v.getY();
                int oz = z + v.getZ();
                queueForUpdate(ox, oy, oz);
            }
        }
        return updated;
    }

    public void queueForUpdateAsync(int x, int y, int z, EffectRange range) {
        asyncQueue.add(new PhysicsUpdate(x, y, z, range));
    }

    public void queueForUpdate(int x, int y, int z) {
        updateQueue.add(x, y, z);
    }

    public UpdateQueue getUpdateQueue() {
        return updateQueue;
    }
}
