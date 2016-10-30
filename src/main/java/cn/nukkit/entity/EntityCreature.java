package cn.nukkit.entity;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.entity.ai.MobAI;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import java.util.Map;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class EntityCreature extends EntityLiving {

    private MobAI intelligence;

    // Target can be block or entity
    public Vector3 target;

    public EntityCreature(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    public void setAI(MobAI intelligence) {
        this.intelligence = intelligence;
    }

    public MobAI getAI() {
        return intelligence;
    }

    public Vector3 getTarget() {
        if (intelligence != null) {
            if (!isTargetValid(target)) {
                target = intelligence.findTarget();
            }
            return target != null ? target : this;
        }
        return null;
    }

    public double getSpeed() {
        return 1;
    }


    @Override
    protected void updateMovement(){
        if(this.lastX != this.x || this.lastY != this.y || this.lastZ != this.z || this.lastYaw != this.yaw || this.lastPitch != this.pitch){
            this.lastX = this.x;
            this.lastY = this.y;
            this.lastZ = this.z;
            this.lastYaw = this.yaw;
            this.lastPitch = this.pitch;
            this.addMovement(this.x, this.y, this.z, this.yaw, this.pitch, this.yaw);
        }
    }

    public boolean isTargetValid(Vector3 target) {
        return intelligence != null ? intelligence.isValidTarget(target) : false;
    }

    public void setTarget(Vector3 target) {
        this.target = target;
    }

    @Override
    public void attack(EntityDamageEvent source){
        if(source instanceof EntityDamageByEntityEvent) {
            Entity attacker = ((EntityDamageByEntityEvent) source).getDamager();
            if (this.attackTime > 0 && attacker instanceof Player){
                source.setCancelled(true);
                return;
            }
            if (intelligence != null) {
                if (attacker instanceof EntityProjectile && ((EntityProjectile) attacker).shootingEntity != null) {
                    attacker = ((EntityProjectile) attacker).shootingEntity;
                }
                intelligence.processAttacker(attacker);
            }
        }
        super.attack(source);
        this.target = null;
        this.attackTime = 7;
    }

    public boolean attackTarget(EntityCreature target) {
        return false;
    }

    protected boolean jump(double dx, double dz){
        if(this.motionY == this.getGravity() * 2){
            return this.level.getBlock(new Vector3(NukkitMath.floorDouble(this.x), (int) this.y, NukkitMath.floorDouble(this.z))) instanceof BlockLiquid;
        }else{
            if(this.level.getBlock(new Vector3(NukkitMath.floorDouble(this.x), (int) (this.y + 0.8), NukkitMath.floorDouble(this.z))) instanceof BlockLiquid){
                this.motionY = this.getGravity() * 2;
                return true;
            }
        }

        if(!this.onGround){
            return false;
        }
        if(this.motionY <= this.getGravity() * 6){
            this.motionY = this.getGravity() * 6;
        }else{
            this.motionY += this.getGravity() * 0.25;
        }
        return true;
    }

    @Override
    public boolean onUpdate(int currentTick){
        if (this.closed) {
            return false;
        }
        if (!this.isAlive()) {
            ++this.deadTicks;
            if (this.deadTicks >= 10) {
                this.despawnFromAll();
                if (!this.isPlayer) {
                    this.close();
                }
            }
            return this.deadTicks < 10;
        }
        int tickDiff = currentTick - this.lastUpdate;
        if (tickDiff <= 0) {
            return false;
        }
        this.lastUpdate = currentTick;
        boolean hasUpdate = this.entityBaseTick(tickDiff) | this.calculateAndPerformMove(tickDiff) != null;
        if (target != null && target instanceof EntityCreature) {
            attackTarget((EntityCreature) target);
        }
        return hasUpdate;
    }


    public Player getClosestPlayer(double requiredDistance) {
        double min = requiredDistance * requiredDistance;
        Player closest = null;
        for (Map.Entry<Long, Player> entry : level.getPlayers().entrySet()) {
            Player player = entry.getValue();
            double distance = player.distanceSquared(this);
            if (distance < min) {
                min = distance;
                closest = player;
            }
        }
        return closest;
    }

    public void defaultMovement(int tickDiff) {
        calculateAndPerformFlow(tickDiff);
        this.motionY -= this.getGravity() * tickDiff;
        this.updateMovement();
    }

    private boolean calculateAndPerformFlow(int tickDiff) {
        Block block = level.getBlock(this);
        int blockId = block.getId();
        this.move(this.motionX * tickDiff, this.motionY, this.motionZ * tickDiff);
        if (block instanceof BlockLiquid) {
            Vector3 flow = ((BlockLiquid) block).getFlowVector();
            this.motionX += flow.x * 0.05 * tickDiff;
            this.motionY += (this.getGravity() * 1.1 + flow.y * 0.05) * tickDiff;
            this.motionZ += flow.x * 0.05 * tickDiff;
            return true;
        } else {
            return false;
        }
    }

    public Vector3 calculateAndPerformMove(int tickDiff){
        if(this.attackTime > 0) {
            this.defaultMovement(tickDiff);
            return null;
        }
        if (intelligence == null) {
            this.defaultMovement(tickDiff);
            return null;
        }

        if (!isTargetValid(target)) {
            target = intelligence.findTarget();
            if (target == null && !isTargetValid(target)) {
                this.defaultMovement(tickDiff);
                return null;
            }
        }
        Vector3 move = intelligence.calculateNextMove(target);
        if (move == this || move == null) {
            this.defaultMovement(tickDiff);
            return null;
        }

        double x = move.x - this.x;
        double y = move.y - this.y;
        double z = move.z - this.z;

        double diff = Math.abs(x) + Math.abs(z);
        if (hasGravity()) {
            if (move == target && (this.distance(move) <= (this.getWidth() + 0.0d) / 2 + 0.05)) {
                this.motionX = 0;
                this.motionZ = 0;
            } else {
                this.motionX = this.getSpeed() * 0.15 * (x / diff);
                this.motionZ = this.getSpeed() * 0.15 * (z / diff);
                this.yaw = Math.toDegrees(-Math.atan2(x / diff, z / diff));
                this.pitch = y == 0 ? 0 : Math.toDegrees(-Math.atan2(y, Math.sqrt(x * x + z * z)));
            }
            double dx = this.motionX * tickDiff;
            double dz = this.motionZ * tickDiff;
            boolean hasJumped;
            if (move.getFloorY() > getFloorY()) {
                hasJumped = this.jump(dx, dz);
            } else {
                hasJumped = false;
            }
            Vector2 be = new Vector2(this.x + dx, this.z + dz);
            this.move(dx, this.motionY * tickDiff, dz);
            Vector2 af = new Vector2(this.x, this.z);
            if(!hasJumped){
                if(this.onGround){
                    this.motionY = 0;
                }else if(this.motionY < -this.getGravity() * 4){
                    if(!(this.level.getBlock(new Vector3(NukkitMath.floorDouble(this.x), (int) (this.y + 0.8), NukkitMath.floorDouble(this.z))) instanceof BlockLiquid)){
                        this.motionY -= this.getGravity() * 1;
                    }
                }else{
                    this.motionY -= this.getGravity() * tickDiff;
                }
            }
        } else {
            if (move == target && (this.distance(move) <= (this.getWidth() + 0.0d) / 2 + 0.05)) {
                this.motionX = 0;
                this.motionZ = 0;
                this.motionY = 0;
            } else {
                this.motionX = this.getSpeed() * 0.15 * (x / diff);
                this.motionZ = this.getSpeed() * 0.15 * (z / diff);
                this.motionY = this.getSpeed() * 0.15 * (y / diff);
                this.yaw = Math.toDegrees(-Math.atan2(x / diff, z / diff));
                this.pitch = y == 0 ? 0 : Math.toDegrees(-Math.atan2(y, Math.sqrt(x * x + z * z)));
            }
            double dx = this.motionX * tickDiff;
            double dy = this.motionY * tickDiff;
            double dz = this.motionZ * tickDiff;
            this.move(dx, dy, dz);
        }
        calculateAndPerformFlow(tickDiff);
        this.updateMovement();
        return this.target;
    }
}
