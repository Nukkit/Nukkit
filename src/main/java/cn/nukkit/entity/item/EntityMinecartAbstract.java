package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.api.API;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockRail;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemMinecart;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.SmokeParticle;
import cn.nukkit.math.MathHelper;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.utils.Rail;
import cn.nukkit.utils.Rail.Orientation;

import static cn.nukkit.api.API.Definition;
import static cn.nukkit.api.API.Usage;

/**
 * Author: Adam Matthew [larryTheCoder]
 * <p>
 * Nukkit Project
 */
public abstract class EntityMinecartAbstract extends EntityVehicle {

    /**
     * Minecart: Nukkit Project
     */
    private String name;
    private final int[][][] blockMSpace = new int[][][]{{{0, 0, -1}, {0, 0, 1}},
            {{-1, 0, 0}, {1, 0, 0}}, {{-1, -1, 0}, {1, 0, 0}}, {{-1, 0, 0},
            {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}},
            {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, {-1, 0, 0}}, {{0, 0, -1},
            {-1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};
    private double currentSpeed = 0;
    protected Block blockInside = null;
    private boolean slowWhenEmpty = false;

    /**
     * Get the type of the minecart id:
     * <p>
     * 0. Minecart empty, 1. Minecart with chest, 2. Minecart with furnace 3.
     * Minecart with TNT, 4. Minecart with Mob spawner, 5. Minecart with hopper,
     * 6. Minecart with command block.
     *
     * @return Integer
     */
    public abstract int getMineId();

    public EntityMinecartAbstract(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public float getHeight() {
        return 0.7F; // Yes
    }

    @Override
    public float getWidth() {
        return 0.98F; // Yes
    }

    @Override
    protected float getDrag() {
        return 0.1F; // Exactly? Not really
    }

    @Override
    protected float getGravity() {
        return 0.5f; // This true!
    }

    @Override
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        prepareDataProperty();
        setName("Minecart");
        setMaxHealth(6);
        setHealth(getMaxHealth());
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (closed) {
            return false;
        }

        int tickDiff = currentTick - lastUpdate;

        if (tickDiff <= 0) {
            return false;
        }
        lastUpdate = currentTick;

        boolean hasUpdate = this.entityBaseTick(tickDiff);

        if (isAlive()) {
            if (this.getHurtTime() > 0) {
                this.setHurtTime(this.getHurtTime() - 1);
            }
            
            if (this.getDamageTaken() > 0.0F) {
                this.setDamageTaken(this.getDamageTaken() - 1.0F);
            }
            
            if (y < -64.0D) {
                kill();
            }
            this.lastX = this.x;
            this.lastY = this.y;
            this.lastZ = this.z;
            motionY -= 0.03999999910593033D;
            int dx = MathHelper.floor(x);
            int dy = MathHelper.floor(y);
            int dz = MathHelper.floor(z);

            // Check if the rail exsits
            if (Rail.isRailBlock(level.getBlockIdAt(dx, dy - 1, dz))) {
                --dy; // check again down
            }

            Block block = level.getBlock(new Vector3(dx, dy, dz)); // get the rail

            // Now start to check if the block is 'Rail'
            if (Rail.isRailBlock(block)) {
                processMovement(dx, dy, dz, (BlockRail) block);
                if (block.getId() == Block.ACTIVATOR_RAIL) {
                    // TNT only explode on ACTIVATOR RAIL!
                    activate(dx, dy, dz, (block.getDamage() & 0x8) != 0);
                }
            } else {
                // control falling and movement (off rail)
                setFalling();
            }

            /////////////////////////////////////////
            // MINECART BEHAVIOR: DIVING & ROTATING//
            /////////////////////////////////////////
            double befX = lastMotionX - motionX;
            double befZ = lastMotionZ - motionZ;

            if (befX * befX + befZ * befZ > 0.001D) {
                yaw = (Math.atan2(befZ, befX) * 180.0D / 3.141592653589793D);
            }

            // N W S E
            if (Rail.isRailBlock(block)) {
                switch (Orientation.byMetadata(((BlockRail) block).getRealMeta())) {
                    case CURVED_SOUTH_EAST:
                    case CURVED_SOUTH_WEST:
                    case CURVED_NORTH_EAST:
                    case CURVED_NORTH_WEST:
                    case STRAIGHT_NORTH_SOUTH: // Recheck rail! Because this is wrong.
                        yaw = 90;
                        pitch = 0;
                        break;
                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                        yaw = 0;
                        pitch = 45;
                        break;
                    case ASCENDING_EAST:
                    case ASCENDING_WEST:
                        yaw = 90;
                        pitch = 45;
                        break;
                    default:
                        pitch = 0;
                        yaw = 0;
                        break;
                }
            } else {
                pitch = 0;
            }

            setRotation(yaw, pitch);

            //////////////////////////////////
            // MINECART BEHAVIOR: COLLIDING //
            //////////////////////////////////
            for (Entity entity : level.getNearbyEntities(this.boundingBox.grow(0.20000000298023224D, 0.0D, 0.20000000298023224D), this)) {
                if (entity != this.linkedEntity && entity instanceof EntityMinecartAbstract) {
                    ((EntityMinecartAbstract) entity).onCollideWithVehicle(this);
                }
            }
            double atan = (Math.atan2(befZ, befX) * 180.0D / 3.141592653589793D);

            Server.getInstance().getLogger().debug("X:" + x);
            Server.getInstance().getLogger().debug("Y:" + y);
            Server.getInstance().getLogger().debug("Z:" + z);
            Server.getInstance().getLogger().debug("motionX:" + motionX);
            Server.getInstance().getLogger().debug("motionY:" + motionY);
            Server.getInstance().getLogger().debug("motionZ:" + motionZ);
            Server.getInstance().getLogger().debug("Yaw:" + yaw);
            Server.getInstance().getLogger().debug("Atan Yaw: " + atan);

            hasUpdate = true;
        }

        // This (code) will check if the entity (mobs|player) is dead!
        if (this.linkedEntity != null && !this.isAlive() || this.linkedEntity != null) {
            if (!this.linkedEntity.isAlive()) {
                if (this.linkedEntity.riding == this) {
                    this.linkedEntity.riding = null;
                }

                this.linkedEntity.setDataFlag(DATA_FLAGS, DATA_FLAG_RIDING, false);
                this.linkedEntity = null;
            } else if (!this.isAlive()) {
                if (this.linkedEntity.riding == this) {
                    this.linkedEntity.riding = null;
                }

                this.linkedEntity.setDataFlag(DATA_FLAGS, DATA_FLAG_RIDING, false);
                this.linkedEntity = null;
            }
        }
        return hasUpdate || !onGround || Math.abs(motionX) > 0.00001 || Math.abs(motionY) > 0.00001 || Math.abs(motionZ) > 0.00001;
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        if (this.invulnerable){
            return false;
        } else {
            this.setHurtDirection(getHurtDirection());
            this.setHurtTime(10);
            this.setDamageTaken(this.getDamageTaken() + source.getDamage() * 10.0F);

            Entity damager = ((EntityDamageByEntityEvent) source).getDamager();
            boolean instantKill = damager instanceof Player
                    ? ((Player) damager).isCreative() : false;
            if (instantKill || this.getDamageTaken() > 40.0F) {
                if (this.linkedEntity != null) {
                    this.dismount(linkedEntity);
                }
                
                if (instantKill) {
                    kill();
                } else {
                    if(level.getGameRules().getBoolean("doEntityDrops")){
                        dropItem();
                    }
                    close();
                }
            }
        }

        return true;
    }

    public void dropItem() {
        level.dropItem(this, new ItemMinecart());
    }
    
    @Override
    public void close() {
        super.close();

        if (linkedEntity instanceof Player) {
            linkedEntity.riding = null;
            linkedEntity = null;
        }

        SmokeParticle particle = new SmokeParticle(this);
        level.addParticle(particle);
    }

    @Override
    public boolean onInteract(Player p, Item item) {
        if (linkedEntity != null) {
            return false;
        }

        if (blockInside == null) {
            this.mount(p); // Simple
        }
        return true;
    }

    @Override
    public void spawnTo(Player player) {
        AddEntityPacket pk = new AddEntityPacket();
        pk.entityUniqueId = getId();
        pk.entityRuntimeId = getId();
        pk.type = getNetworkId();
        pk.x = (float) x;
        pk.y = (float) y;
        pk.z = (float) z;
        pk.speedX = 0;
        pk.speedY = 0;
        pk.speedZ = 0;
        pk.yaw = 0;
        pk.pitch = 0;
        pk.metadata = dataProperties;
        player.dataPacket(pk);

        super.spawnTo(player);
    }

    @Override
    public void onCollideWithVehicle(Entity entity) {
        if (entity != riding) {
            if (entity instanceof EntityLiving
                    && !(entity instanceof EntityHuman)
                    && motionX * motionX + motionZ * motionZ > 0.01D
                    && linkedEntity == null
                    && entity.riding == null
                    && blockInside == null) {
                if (riding == null) {
                    this.mount(entity); // Just like MC logic: Entity will ride minecart
                }
            }

            double motiveX = entity.x - x;
            double motiveZ = entity.z - z;
            double square = motiveX * motiveX + motiveZ * motiveZ;

            if (square >= 9.999999747378752E-5D) {
                square = Math.sqrt(square);
                motiveX /= square;
                motiveZ /= square;
                double next = 1.0D / square;

                if (next > 1.0D) {
                    next = 1.0D;
                }

                motiveX *= next;
                motiveZ *= next;
                motiveX *= 0.10000000149011612D;
                motiveZ *= 0.10000000149011612D;
                motiveX *= 1.0D + (entity.x - x);
                motiveZ *= 1.0D + (entity.z - z);
                if (entity instanceof EntityMinecartAbstract) {
                    motiveX *= 0.5D;
                    motiveZ *= 0.5D;
                    double desinityX = entity.x - x;
                    double desinityZ = entity.z - z;
                    Vector3 vector = slice(new Vector3(desinityX, 0.0D, desinityZ));
                    Vector3 vec = slice(new Vector3((double) MathHelper.cos((float) yaw * 0.017453292F), 0.0D, (double) MathHelper.sin((float) yaw * 0.017453292F)));
                    double desinityXZ = Math.abs(vector.dot(vec));

                    if (desinityXZ < 0.800000011920929D) {
                        return;
                    }

                    double motX = entity.motionX + motionX;
                    double motZ = entity.motionZ + motionZ;

                    if (((EntityMinecartAbstract) entity).getMineId() == 2 && getMineId() != 2) {
                        motionX *= 0.20000000298023224D;
                        motionZ *= 0.20000000298023224D;
                        setMotion(new Vector3(entity.motionX - motiveX, 0.0D, entity.motionZ - motiveZ));
                        entity.motionX *= 0.949999988079071D;
                        entity.motionZ *= 0.949999988079071D;
                    } else if (((EntityMinecartAbstract) entity).getMineId() != 2 && getMineId() == 2) {
                        entity.motionX *= 0.20000000298023224D;
                        entity.motionZ *= 0.20000000298023224D;
                        ((EntityMinecartAbstract) entity).setMotion(motionX + motiveX, 0.0D, motionZ + motiveZ);
                        motionX *= 0.949999988079071D;
                        motionZ *= 0.949999988079071D;
                    } else {
                        motX /= 2.0D;
                        motZ /= 2.0D;
                        motionX *= 0.20000000298023224D;
                        motionZ *= 0.20000000298023224D;
                        setMotion(motX - motiveX, 0.0D, motZ - motiveZ);
                        entity.motionX *= 0.20000000298023224D;
                        entity.motionZ *= 0.20000000298023224D;
                        ((EntityMinecartAbstract) entity).setMotion(motX + motiveX, 0.0D, motZ + motiveZ);
                    }
                } else {
                    double motX = entity.motionX + motionX;
                    double motZ = entity.motionZ + motionZ;
                    motX /= 2.0D;
                    motZ /= 2.0D;
                    // Todo: Recheck & accurate the precise collisions
                    setMotion(motX - motiveX, 0.0D, motZ - motiveZ);
                    entity.setMotion(new Vector3(motiveX / 4.0D, 0.0D, motiveX / 4.0D));
                }
            }
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        saveEntityData();
    }

    protected void activate(int x, int y, int z, boolean flag) {
    }

    protected void setFalling() {
        // The problem is the minecart on ground flag has to be at 0.5D from
        // a block. Also, the minecart are controlled by client, not server.
        // This is the reason why minecart always be in a block!
        // How to fix: adjust the hieght on movement BoundingBox
        motionX = NukkitMath.clamp(motionX, -0.4D, 0.4D);
        motionZ = NukkitMath.clamp(motionZ, -0.4D, 0.4D);
        
        if (onGround) {
            motionX *= 0.5D;
            motionY *= 0.5D;
            motionZ *= 0.5D;
        }

        moveMinecart(motionX, motionY, motionZ, 0.4D);
        if (!onGround) {
            motionX *= 0.949999988079071D;
            motionY *= 0.949999988079071D;
            motionZ *= 0.949999988079071D;
        }
    }

    protected void processMovement(int dx, int dy, int dz, BlockRail block) {
        fallDistance = 0.0F;
        Vector3 vector = getNextRail(x, y, z);

        y = (double) dy;
        boolean isPowered = false;
        boolean isSlowed = false;
        // Adjust the minecart ONLY when ascending rail
        // This is to avoid weird riding experience
        double adjust = 0;

        // My minstake [cant use .equals()]
        if (block.getId() == Block.POWERED_RAIL) {
            isPowered = (block.getDamage() & 0x8) != 0;
            isSlowed = !isPowered;
        }

        switch (Orientation.byMetadata(((BlockRail) block).getRealMeta())) {
            case ASCENDING_NORTH:
                motionX -= 0.0078125D;
                y += 1.0D;
                adjust = -0.2;
                break;
            case ASCENDING_SOUTH:
                motionX += 0.0078125D;
                y += 1.0D;
                adjust = -0.2;
                break;
            case ASCENDING_EAST:
                motionZ += 0.0078125D;
                y += 1.0D;
                adjust = -0.2;
                break;
            case ASCENDING_WEST:
                motionZ -= 0.0078125D;
                y += 1.0D;
                adjust = -0.2;
                break;
        }

        int[][] facing = blockMSpace[block.getRealMeta()];
        double facing1 = (double) (facing[1][0] - facing[0][0]);
        double facing2 = (double) (facing[1][2] - facing[0][2]);
        double speedOnTurns = Math.sqrt(facing1 * facing1 + facing2 * facing2);
        double realFacing = motionX * facing1 + motionZ * facing2;

        if (realFacing < 0.0D) {
            facing1 = -facing1;
            facing2 = -facing2;
        }

        double squareOfFame = Math.sqrt(motionX * motionX + motionZ * motionZ);

        if (squareOfFame > 2.0D) {
            squareOfFame = 2.0D;
        }

        motionX = squareOfFame * facing1 / speedOnTurns;
        motionZ = squareOfFame * facing2 / speedOnTurns;
        double expectedSpeed;
        double playerYawNeg; // PlayerYawNegative
        double playerYawPos; // PlayerYawPositive
        double motion;

        if (linkedEntity != null && linkedEntity instanceof EntityLiving) {
            expectedSpeed = currentSpeed;
            if (expectedSpeed > 0.0D) {
                playerYawNeg = -Math.sin((double) (linkedEntity.yaw * 3.1415927F / 180.0F));
                playerYawPos = Math.cos((double) (linkedEntity.yaw * 3.1415927F / 180.0F));
                motion = motionX * motionX + motionZ * motionZ;
                if (motion < 0.01D) {
                    motionX += playerYawNeg * 0.1D;
                    motionZ += playerYawPos * 0.1D;

                    isSlowed = false;
                }
            }
        }

        //http://minecraft.gamepedia.com/Powered_Rail#Rail
        if (isSlowed) {
            expectedSpeed = Math.sqrt(motionX * motionX + motionZ * motionZ);
            if (expectedSpeed < 0.03D) {
                motionX *= 0.0D;
                motionY *= 0.0D;
                motionZ *= 0.0D;
            } else {
                motionX *= 0.5D;
                motionY *= 0.0D;
                motionZ *= 0.5D;
            }
        }

        playerYawNeg = (double) dx + 0.5D + (double) facing[0][0] * 0.5D;
        playerYawPos = (double) dz + 0.5D + (double) facing[0][2] * 0.5D;
        motion = (double) dx + 0.5D + (double) facing[1][0] * 0.5D;
        double wallOfFame = (double) dz + 0.5D + (double) facing[1][2] * 0.5D;

        facing1 = motion - playerYawNeg;
        facing2 = wallOfFame - playerYawPos;
        double motX;
        double motZ;

        if (facing1 == 0.0D) {
            x = (double) dx + 0.5D;
            expectedSpeed = z - (double) dz;
        } else if (facing2 == 0.0D) {
            z = (double) dz + 0.5D;
            expectedSpeed = x - (double) dx;
        } else {
            motX = x - playerYawNeg;
            motZ = z - playerYawPos;
            expectedSpeed = (motX * facing1 + motZ * facing2) * 2.0D;
        }

        x = playerYawNeg + facing1 * expectedSpeed;
        z = playerYawPos + facing2 * expectedSpeed;
        setPosition(new Vector3(x, y + (double) getHeight(), z));
        motX = motionX;
        motZ = motionZ;
        if (linkedEntity != null || !slowWhenEmpty) {
            motX *= 0.75D;
            motZ *= 0.75D;
        }
        motX = NukkitMath.clamp(motX, -0.4D, 0.4D);
        motZ = NukkitMath.clamp(motZ, -0.4D, 0.4D);

        moveMinecart(motX, 0.0D, motZ, adjust);
        if (facing[0][1] != 0 && MathHelper.floor(x) - dx == facing[0][0] && MathHelper.floor(z) - dz == facing[0][2]) {
            setPosition(new Vector3(x, y + (double) facing[0][1], z));
        } else if (facing[1][1] != 0 && MathHelper.floor(x) - dx == facing[1][0] && MathHelper.floor(z) - dz == facing[1][2]) {
            setPosition(new Vector3(x, y + (double) facing[1][1], z));
        }

        reduce(); // Reduce the minecart speed
        Vector3 vector1 = getNextRail(x, y, z);

        if (vector1 != null && vector != null) {
            double d14 = (vector.y - vector1.y) * 0.05D;

            squareOfFame = Math.sqrt(motionX * motionX + motionZ * motionZ);
            if (squareOfFame > 0.0D) {
                motionX = motionX / squareOfFame * (squareOfFame + d14);
                motionZ = motionZ / squareOfFame * (squareOfFame + d14);
            }

            setPosition(new Vector3(x, vector1.y, z));
        }

        int floorX = MathHelper.floor(x);
        int floorZ = MathHelper.floor(z);

        if (floorX != dx || floorZ != dz) {
            squareOfFame = Math.sqrt(motionX * motionX + motionZ * motionZ);
            motionX = squareOfFame * (double) (floorX - dx);
            motionZ = squareOfFame * (double) (floorZ - dz);
        }

        if (isPowered) {
            double newMovie = Math.sqrt(motionX * motionX + motionZ * motionZ);

            if (newMovie > 0.01D) {
                double nextMovie = 0.06D;

                motionX += motionX / newMovie * nextMovie;
                motionZ += motionZ / newMovie * nextMovie;
            } else if (block.getOrientation() == Orientation.STRAIGHT_NORTH_SOUTH) {
                if (level.getBlock(new Vector3(dx - 1, dy, dz)).isNormalBlock()) {
                    motionX = 0.02D;
                } else if (level.getBlock(new Vector3(dx + 1, dy, dz)).isNormalBlock()) {
                    motionX = -0.02D;
                }
            } else if (block.getOrientation() == Orientation.STRAIGHT_EAST_WEST) {
                if (level.getBlock(new Vector3(dx, dy, dz - 1)).isNormalBlock()) {
                    motionZ = 0.02D;
                } else if (level.getBlock(new Vector3(dx, dy, dz + 1)).isNormalBlock()) {
                    motionZ = -0.02D;
                }
            }
        }

    }

    protected void reduce() {
        if (this.linkedEntity != null || !slowWhenEmpty) {
            this.motionX *= 0.996999979019165D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.996999979019165D;
        } else {
            this.motionX *= 0.9599999785423279D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.9599999785423279D;
        }
    }

    public Vector3 getNextRail(double dx, double dy, double dz) {
        int checkX = MathHelper.floor(dx);
        int checkY = MathHelper.floor(dy);
        int checkZ = MathHelper.floor(dz);

        if (Rail.isRailBlock(level.getBlockIdAt(checkX, checkY - 1, checkZ))) {
            --checkY;
        }

        Block block = level.getBlock(new Vector3(checkX, checkY, checkZ));

        if (Rail.isRailBlock(block)) {
            int l = block.getDamage();

            if (((BlockRail) block).canPowered()) {
                l &= 0x7;
            }

            int[][] facing = blockMSpace[l];
            double rail;
            // Genisys minstake (Doesn't check surrounding more exactly)
            double nextOne = (double) checkX + 0.5D + (double) facing[0][0] * 0.5D;
            double nextTwo = (double) checkY + 0.5D + (double) facing[0][1] * 0.5D;
            double nextThree = (double) checkZ + 0.5D + (double) facing[0][2] * 0.5D;
            double nextFour = (double) checkX + 0.5D + (double) facing[1][0] * 0.5D;
            double nextFive = (double) checkY + 0.5D + (double) facing[1][1] * 0.5D;
            double nextSix = (double) checkZ + 0.5D + (double) facing[1][2] * 0.5D;
            double nextSeven = nextFour - nextOne;
            double nextEight = (nextFive - nextTwo) * 2.0D;
            double nextMax = nextSix - nextThree;

            if (nextSeven == 0.0D) {
                rail = dz - (double) checkZ;
            } else if (nextMax == 0.0D) {
                rail = dx - (double) checkX;
            } else {
                double whatOne = dx - nextOne;
                double whatTwo = dz - nextThree;

                rail = (whatOne * nextSeven + whatTwo * nextMax) * 2.0D;
            }

            dx = nextOne + nextSeven * rail;
            dy = nextTwo + nextEight * rail;
            dz = nextThree + nextMax * rail;
            if (nextEight < 0.0D) {
                ++dy;
            }

            if (nextEight > 0.0D) {
                dy += 0.5D;
            }

            return new Vector3(dx, dy, dz);
        } else {
            return null;
        }
    }

    private void moveMinecart(double motionX, double motionY, double motionZ, double adjust) {
        move(motionX, motionY, motionZ, adjust);
        super.updateMovement();
    }

    /**
     * Used to multiply the minecart current speed
     *
     * @param speed The speed of the minecart that will be calculated
     */
    public void setCurrentSpeed(double speed) {
        this.currentSpeed = speed;
    }

    public void setMotion(double dx, double dy, double dz) {
        setMotion(new Vector3(dx, dy, dz));
        super.updateMovement();
    }

    private void prepareDataProperty() {
        setDataProperty(new IntEntityData(DATA_HEALTH, 6));
        setDataProperty(new IntEntityData(DATA_HURT_TIME, 0));
        setDataProperty(new IntEntityData(DATA_HURT_DIRECTION, 1));
        setDataProperty(new IntEntityData(DATA_DAMAGE_TAKEN, 1));
        if (namedTag.contains("CustomDisplayTile")) {
            if (namedTag.getBoolean("CustomDisplayTile")) {
                int display = namedTag.getInt("DisplayTile");
                int offSet = namedTag.getInt("DisplayOffset");
                setDataProperty(new ByteEntityData(DATA_MINECART_HAS_DISPLAY, 1));
                setDataProperty(new IntEntityData(DATA_MINECART_DISPLAY_BLOCK, display));
                setDataProperty(new IntEntityData(DATA_MINECART_DISPLAY_OFFSET, offSet));
            }
        } else {
            int display = blockInside == null ? 0
                    : blockInside.getId()
                    | blockInside.getDamage() << 16;
            if (display == 0) {
                setDataProperty(new ByteEntityData(DATA_MINECART_HAS_DISPLAY, 0));
                return;
            }
            setDataProperty(new ByteEntityData(DATA_MINECART_HAS_DISPLAY, 1));
            setDataProperty(new IntEntityData(DATA_MINECART_DISPLAY_BLOCK, display));
            setDataProperty(new IntEntityData(DATA_MINECART_DISPLAY_OFFSET, 6));
        }
    }

    private void saveEntityData() {
        boolean hasDisplay = super.getDataPropertyByte(DATA_MINECART_HAS_DISPLAY) == 1
                || blockInside != null;
        int display;
        int offSet;
        namedTag.putBoolean("CustomDisplayTile", hasDisplay);
        if (hasDisplay) {
            display = blockInside.getId()
                    | blockInside.getDamage() << 16;
            offSet = getDataPropertyInt(DATA_MINECART_DISPLAY_OFFSET);
            namedTag.putInt("DisplayTile", display);
            namedTag.putInt("DisplayOffset", offSet);
        }
    }

    private Vector3 slice(Vector3 vec) {
        double ind = Math.sqrt(vec.x * vec.x + vec.y * vec.y + vec.z * vec.z);

        return ind < 1.0E-4D ? new Vector3(0.0D, 0.0D, 0.0D) : new Vector3(vec.x / ind, vec.y / ind, vec.z / ind);
    }

    /**
     * Set the minecart display block!
     *
     * @param block The block that will changed. Set {@code null} for BlockAir
     * @return {@code true} if the block is normal block
     */
    @API(usage = Usage.MAINTAINED, definition = Definition.UNIVERSAL)
    public boolean setDisplayBlock(Block block) {
        if (block != null) {
            if (block.isNormalBlock()) {
                blockInside = block;
                int display = blockInside.getId()
                        | blockInside.getDamage() << 16;
                setDataProperty(new ByteEntityData(DATA_MINECART_HAS_DISPLAY, 1));
                setDataProperty(new IntEntityData(DATA_MINECART_DISPLAY_BLOCK, display));
                setDisplayBlockOffset(6);
            }
        } else {
            // Set block to air (default).
            blockInside = null;
            setDataProperty(new ByteEntityData(DATA_MINECART_HAS_DISPLAY, 0));
            setDataProperty(new IntEntityData(DATA_MINECART_DISPLAY_BLOCK, 0));
            setDisplayBlockOffset(0);
        }
        return true;
    }

    /**
     * Get the minecart display block
     *
     * @return Block of minecart display block
     */
    @API(usage = Usage.STABLE, definition = Definition.UNIVERSAL)
    public Block getDisplayBlock() {
        return blockInside;
    }

    /**
     * Set the block offset.
     *
     * @param offset The offset
     */
    @API(usage = Usage.EXPERIMENTAL, definition = Definition.PLATFORM_NATIVE)
    public void setDisplayBlockOffset(int offset) {
        setDataProperty(new IntEntityData(DATA_MINECART_DISPLAY_OFFSET, offset));
    }

    /**
     * Get the block display offset
     *
     * @return integer
     */
    @API(usage = Usage.EXPERIMENTAL, definition = Definition.UNIVERSAL)
    public int getDisplayBlockOffset() {
        return super.getDataPropertyInt(DATA_MINECART_DISPLAY_OFFSET);
    }

    /**
     * Is the minecart can be slowed when empty?
     *
     * @return boolean
     */
    @API(usage = Usage.EXPERIMENTAL, definition = Definition.UNIVERSAL)
    public boolean isSlowWhenEmpty() {
        return this.slowWhenEmpty;
    }

    /**
     * Set the minecart slowdown flag
     *
     * @param slow The slowdown flag
     */
    @API(usage = Usage.EXPERIMENTAL, definition = Definition.UNIVERSAL)
    public void setSlowWhenEmpty(boolean slow) {
        this.slowWhenEmpty = slow;
    }
}
