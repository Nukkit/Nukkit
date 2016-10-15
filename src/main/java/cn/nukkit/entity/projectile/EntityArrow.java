package cn.nukkit.entity.projectile;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.CriticalParticle;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.AddEntityPacket;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class EntityArrow extends EntityProjectile {
    public static final int NETWORK_ID = 80;

    public static final int DATA_SOURCE_ID = 17;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.5f;
    }

    @Override
    public float getLength() {
        return 0.5f;
    }

    @Override
    public float getHeight() {
        return 0.5f;
    }

    @Override
    public float getGravity() {
        return 0.05f;
    }

    public boolean aim(Entity target, double speed) {
        double g = 20;
        double v = speed * 20;

        double dx = Math.abs(getX() - target.getX());
        double dz = Math.abs(getZ() - target.getZ());

        double dy = (target.getY() - getY() + target.getHeight()) / 2;

        double dh = Math.sqrt(dx * dx + dz * dz);
        if (dh > 32) {
            dh += Math.sqrt(dh);
        }

        double pitch = Math.atan( (v * v - Math.sqrt(v * v * v * v - g * (g * dh * dh + 2 * 2 * dy * v * v))) / ( g * dh ) );// * (180/3.14159265359);

        if (Double.isNaN(pitch)) {
            return false;
        }
        double time = dh/v;
        Vector3 targetVelocity = target.subtract(this).add(target.getMotion().multiply(time * 20));
        targetVelocity.y = (Math.tan(pitch) * Math.sqrt(dh * dh + dy * dy));
        setMotion(targetVelocity.normalize().multiply(speed));
        return true;
    }

    @Override
    public float getDrag() {
        return 0.01f;
    }

    @Override
    protected double getDamage() {
        return namedTag.contains("damage") ? namedTag.getDouble("damage") : 2;
    }

    protected float gravity = 0.05f;
    protected float drag = 0.01f;

    protected double damage = 2;

    protected boolean isCritical;

    public EntityArrow(Level level, Vector3 pos) {
        this(level.getChunk(pos.getFloorX() >> 4, pos.getFloorZ() >> 4), getDefaultNBT(pos, 0));
    }

    private static CompoundTag getDefaultNBT(Vector3 pos, int damage) {
        return new CompoundTag()
                .putList(new ListTag<DoubleTag>("Pos")
                        .add(new DoubleTag("", pos.x))
                        .add(new DoubleTag("", pos.y + 0.35f))
                        .add(new DoubleTag("", pos.z)))
                .putList(new ListTag<DoubleTag>("Motion")
                        .add(new DoubleTag("", -Math.sin(0 / 180 * Math.PI) * Math.cos(0 / 180 * Math.PI)))
                        .add(new DoubleTag("", -Math.sin(0 / 180 * Math.PI)))
                        .add(new DoubleTag("", Math.cos(0 / 180 * Math.PI) * Math.cos(0 / 180 * Math.PI))))
                .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("", (float) 0))
                        .add(new FloatTag("", (float) 0)))
                .putShort("Fire", 0)
                .putDouble("damage", damage);
    }

    public EntityArrow(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public EntityArrow(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        this(chunk, nbt, shootingEntity, false);
    }

    public EntityArrow(FullChunk chunk, CompoundTag nbt, Entity shootingEntity, boolean critical) {
        super(chunk, nbt, shootingEntity);
        this.isCritical = critical;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        this.timing.startTiming();

        boolean hasUpdate = super.onUpdate(currentTick);

        if (!this.hadCollision && this.isCritical) {
            NukkitRandom random = new NukkitRandom();
            this.level.addParticle(new CriticalParticle(this.add(
                    this.getWidth() / 2 + ((double) NukkitMath.randomRange(random, -100, 100)) / 500,
                    this.getHeight() / 2 + ((double) NukkitMath.randomRange(random, -100, 100)) / 500,
                    this.getWidth() / 2 + ((double) NukkitMath.randomRange(random, -100, 100)) / 500)));
        } else if (this.onGround) {
            this.isCritical = false;
        }

        if (this.age > 1200) {
            this.kill();
            hasUpdate = true;
        }

        this.timing.stopTiming();

        return hasUpdate;
    }

    @Override
    public void spawnTo(Player player) {
        AddEntityPacket pk = new AddEntityPacket();
        pk.type = EntityArrow.NETWORK_ID;
        pk.eid = this.getId();
        pk.x = (float) this.x;
        pk.y = (float) this.y;
        pk.z = (float) this.z;
        pk.speedX = (float) this.motionX;
        pk.speedY = (float) this.motionY;
        pk.speedZ = (float) this.motionZ;
        pk.metadata = this.dataProperties;
        player.dataPacket(pk);

        super.spawnTo(player);
    }
}
