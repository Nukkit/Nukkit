package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemDye;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.utils.DyeColor;

import java.util.Random;

/**
 * @author PikyCZ
 */
public class EntitySquid extends EntityAnimal {

    public static final int NETWORK_ID = 17;

    public EntitySquid(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.95f;
    }

    @Override
    public float getHeight() {
        return 0.95f;
    }

    @Override
    public float getEyeHeight() {
        return 0.7f;
    }

    public Vector3 swimDirection = null;
    public float swimSpeed = 0.1f;

    private int switchDirectionTicker = 0;

    private Random random;
    protected Entity entity;

    @Override
    public void initEntity() {
        super.initEntity();
        this.setMaxHealth(10);
    }

    public void attack(int damage, EntityDamageEvent source) {
        this.attack(damage, source);
        if (source.isCancelled()) {
            return;
        }

        if (source instanceof EntityDamageByEntityEvent) {
            this.swimSpeed = random.nextInt((150) + 350) / 2000;
            Entity e = ((EntityDamageByEntityEvent) source).getDamager();
            if (e != null) {
                this.swimDirection = (new Vector3(this.x - e.x, this.y - e.y, this.z - e.z)).normalize();
            }

            EntityEventPacket pk = new EntityEventPacket();
            pk.eid = this.getId();
            pk.event = EntityEventPacket.SQUID_INK_CLOUD;
            //this.server.broadcastPacket(this.hasSpawned, pk);
        }
    }

    private Vector3 generateRandomDirection() {
        return new Vector3(random.nextInt((-1000) + 1000) / 1000, random.nextInt((-500) + 500) / 1000, random.nextInt((-1000) + 1000) / 1000);
    }

    public boolean entityBaseTick(int tickDiff) {
        tickDiff = 1;
        if (this.closed) {
            return false;
        }

        if (++this.switchDirectionTicker == 100 || this.isCollided) {
            this.switchDirectionTicker = 0;
            if (random.nextInt(100) < 50) {
                this.swimDirection = null;
            }
        }

        boolean hasUpdate = this.entityBaseTick(tickDiff);

        if (this.isAlive()) {
            if (this.y > 62 && this.swimDirection != null) {
                this.swimDirection.y = -0.5;
            }

            boolean inWater = this.isInsideOfWater();

            if (!inWater) {
                this.swimDirection = null;
            } else if (this.swimDirection != null) {
                if (Math.pow(this.motionX, 2) + Math.pow(this.motionY, 2) + Math.pow(this.motionZ, 2) <= this.swimDirection.lengthSquared()) {
                    this.motionX = this.swimDirection.x * this.swimSpeed;
                    this.motionY = this.swimDirection.y * this.swimSpeed;
                    this.motionZ = this.swimDirection.z * this.swimSpeed;
                }
            } else {
                this.swimDirection = this.generateRandomDirection();
                this.swimSpeed = random.nextInt((50) + 100) / 2000;
            }

            // Much math there <3 (Yeah, i love math, @NycuRO)
            double f = Math.sqrt((Math.pow(this.motionX, 2)) + Math.pow(this.motionZ, 2));
            this.yaw = (-Math.atan2(this.motionX, this.motionZ) * 180 / Math.PI);
            this.pitch = (-Math.atan2(f, this.motionY) * 180 / Math.PI);

        }

        return hasUpdate;
    }

    @Override
    public void applyGravity() {
        if (!this.isInsideOfWater()) {
            entity.applyGravity();
        }
    }

    @Override
    public void spawnTo(Player player) {
        AddEntityPacket pk = new AddEntityPacket();
        pk.type = this.getNetworkId();
        pk.entityUniqueId = this.getId();
        pk.entityRuntimeId = this.getId();
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

    @Override
    public Item[] getDrops() {
        int value = random.nextInt(3) + 1;
        return new Item[] {
                new ItemDye(DyeColor.BLACK.getDyeData(), value)
        };
    }

}
