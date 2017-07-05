package cn.nukkit.entity.item;

import cn.nukkit.block.*;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemMinecartTNT;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.SmokeParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import java.util.Random;

/**
 * Author: Adam Matthew [larryTheCoder]
 * <p>
 * Nukkit Project.
 */
public class EntityMinecartTNT extends EntityMinecartAbstract implements EntityExplosive {

    public static final int NETWORK_ID = 97; //wtf?
    private int fuse = 0;
    private boolean activated;

    public EntityMinecartTNT(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.fuse = namedTag.getInt("TNTFuse");
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_POWERED, fuse != 0);
    }

    @Override
    public boolean onUpdate(int currentDiff) {
        boolean hasUpdate = super.onUpdate(currentDiff);
        if (activated) {
            if (this.fuse > 0) {
                --this.fuse;
                this.level.addParticle(new SmokeParticle(new Vector3(this.x, this.y + 0.5D, this.z)));
            } else if (this.fuse == 0) {
                this.explode(this.motionX * this.motionX + this.motionZ * this.motionZ);
            }

            if (this.positionChanged) {
                double square = this.motionX * this.motionX + this.motionZ * this.motionZ;

                if (square >= 0.009999999776482582D) {
                    this.explode(square);
                }
            }
        }

        return hasUpdate || !onGround || Math.abs(motionX) > 0.00001 || Math.abs(motionY) > 0.00001 || Math.abs(motionZ) > 0.00001;

    }

    @Override
    public void activate(int i, int j, int k, boolean flag) {
        this.fuse = 80;
        this.activated = true;

        this.setDataFlag(DATA_FLAGS, DATA_FLAG_POWERED, true);
        this.setDataProperty(new IntEntityData(DATA_FUSE_LENGTH, fuse));
    }

    @Override
    public void explode() {
        explode(0);
    }
    
    public void explode(double square) {
        double root = Math.sqrt(square);

        if (root > 5.0D) {
            root = 5.0D;
        }

        EntityExplosionPrimeEvent event = new EntityExplosionPrimeEvent(this, (4.0D + new Random().nextDouble() * 1.5D * root));
        server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        Explosion explosion = new Explosion(this, event.getForce(), this);
        if (event.isBlockBreaking()) {
            explosion.explodeA();
        }
        explosion.explodeB();
        kill();
    }

    @Override
    public Item dropItem() {
        return new ItemMinecartTNT();
    }

    @Override
    public int getMineId() {
        return 3;
    }

    @Override
    public int getNetworkId() {
        return EntityMinecartTNT.NETWORK_ID;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        super.namedTag.putInt("TNTFuse", this.fuse);
    }
}
