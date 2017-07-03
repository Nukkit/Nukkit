package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;

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
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.AddEntityPacket;

import static cn.nukkit.block.BlockRail.*;
import cn.nukkit.utils.MainLogger;

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
    public MainLogger logger = Server.getInstance().getLogger();

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
            if (isRail(level.getBlock(new Vector3(dx, dy - 1, dz)))) {
                --dy; // check again down
            }

            Block block = level.getBlock(new Vector3(dx, dy, dz)); // get the rail

            // Now start to check if the block is 'Rail'
            if (isRail(block)) {
//                logger.info("IS RAIL!");
                processMovement(dx, dy, dz, block);
                if (block.equals(Block.ACTIVATOR_RAIL)) {
                    activate(dx, dy, dz, (block.getDamage() & 8) != 0);
                }
            } else {
//                logger.info("NOT RAIL!");
                // return slow down minecart
                setSlowdown();
            }

            /////////////////////////////////////////
            // MINECART BEHAVIOR: DIVING & ROTATING//
            /////////////////////////////////////////
            double befX = lastX - x;
            double befZ = lastZ - z;
            int railType = block.getDamage();

            if (befX * befX + befZ * befZ > 0.001D) {
                yaw = (float) (Math.atan2(befZ, befX) * 180.0D / 3.141592653589793D);
            }

            // N W S E
            if (isRail(block)) {
                switch (railType) {
                    case CURVED_SOUTH_EAST:
                    case CURVED_SOUTH_WEST:
                    case CURVED_NORTH_EAST:
                    case CURVED_NORTH_WEST:
                    case STRAIGHT_EAST_WEST:
                        yaw = 90;
                        pitch = 0;
                        break;
                    case STRAIGHT_NORTH_SOUTH:
                        yaw = 0;
                        pitch = 0;
                        break;
                    case SLOPED_ASCENDING_NORTH:
                        yaw = 0;
                        pitch = 45;
                        break;
                    case SLOPED_ASCENDING_WEST:
                        yaw = 90;
                        pitch = 45;
                        break;
                    default:
                        pitch = 0;
                        yaw = 0;
                        break;
                }
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
//            logger.info("X:" + x + "Y:" + y + "Z:" + z);
//            double speed = Math.sqrt(motionX * motionX + motionZ * motionZ);
//            logger.info("Object Speed: " + speed);
//            logger.info("Object YAW: " + yaw);
//            logger.info("Object PITCH: " + pitch);
            // Any suggestion?
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
        if (source instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) source).getDamager();
            if (damager instanceof Player) {
                if (((Player) damager).isCreative()) {
                    kill();
                }
                if (getHealth() <= 0) {
                    if (((Player) damager).isSurvival()) {
                        level.dropItem(this, dropItem());
                    }
                    close();
                }
            }
        }

        EntityEventPacket pk = new EntityEventPacket();
        pk.eid = super.getId();
        pk.event = EntityEventPacket.HURT_ANIMATION;
        Server.broadcastPacket(hasSpawned.values(), pk);

        return true;
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
                motiveX *= 1.0D; // HOW DO I MISSED THIS SIMPLE THING??
                motiveZ *= 1.0D;
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
                        motionX *= 0.18;
                        motionZ *= 0.18;
                        setMotion(motX - motiveX, 0.0D, motZ - motiveZ);
                        entity.motionX *= 0.21;
                        entity.motionZ *= 0.21;
                        ((EntityMinecartAbstract) entity).setMotion(motX + motiveX, 0.0D, motZ + motiveZ);
                    }
                } else {
                    setMotion(-motiveX, 0.0D, -motiveZ);
                }
            }
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        saveEntityData();
    }

    public Item dropItem() {
        return new ItemMinecart();
    }

    protected void activate(int x, int y, int z, boolean flag) {
    }

    protected void setSlowdown() {
        // The problem was minecart height -0.7F from the ground. So add 0.7 to
        // adjust minecart height from ground
        motionX = MathHelper.clamp(motionX, -0.4D, 0.4D);
        motionZ = MathHelper.clamp(motionZ, -0.4D, 0.4D);
        
        if (onGround) {
            motionX *= 0.5D;
            motionY *= 0.5D;
            motionZ *= 0.5D;
        }

        moveMinecart(motionX, motionY, motionZ);
        if (!onGround) {
            motionX *= 0.949999988079071D;
            motionY *= 0.949999988079071D;
            motionZ *= 0.949999988079071D;
        }
    }

    protected void processMovement(int dx, int dy, int dz, Block block) {
        fallDistance = 0.0F;
        Vector3 vector = getNextRail(x, y, z);

        y = (double) dy;
        boolean isPowered = false;
        boolean isSlowed = false;

        if (block.equals(Block.POWERED_RAIL)) {
            isPowered = (block.getDamage() & 0x8) != 0;
            isSlowed = !isPowered;
        }

        switch (block.getDamage()) {
            case SLOPED_ASCENDING_NORTH:
                motionX -= 0.0078125D;
                y += 1.0D;
                break;
            case SLOPED_ASCENDING_SOUTH:
                motionX += 0.0078125D;
                y += 1.0D;
                break;
            case SLOPED_ASCENDING_EAST:
                motionZ += 0.0078125D;
                y += 1.0D;
                break;
            case SLOPED_ASCENDING_WEST:
                motionZ -= 0.0078125D;
                y += 1.0D;
                break;
        }

        int[][] facing = blockMSpace[block.getDamage()];
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
        if (linkedEntity != null) {
            motX *= 0.75D;
            motZ *= 0.75D;
        }
        motX = MathHelper.clamp(motX, -0.4D, 0.4D);
        motZ = MathHelper.clamp(motZ, -0.4D, 0.4D);

        moveMinecart(motX, 0.0D, motZ);
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
            } else if (block.getDamage() == STRAIGHT_NORTH_SOUTH) {
                if (level.getBlock(new Vector3(dx - 1, dy, dz)).isNormalBlock()) {
                    motionX = 0.02D;
                } else if (level.getBlock(new Vector3(dx + 1, dy, dz)).isNormalBlock()) {
                    motionX = -0.02D;
                }
            } else if (block.getDamage() == STRAIGHT_EAST_WEST) {
                if (level.getBlock(new Vector3(dx, dy, dz - 1)).isNormalBlock()) {
                    motionZ = 0.02D;
                } else if (level.getBlock(new Vector3(dx, dy, dz + 1)).isNormalBlock()) {
                    motionZ = -0.02D;
                }
            }
        }

    }

    protected void reduce() {
        if (this.linkedEntity != null) {
            this.motionX *= 0.996999979019165D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.996999979019165D;
        } else {
            this.motionX *= 0.9599999785423279D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.9599999785423279D;
        }
    }

    @SuppressWarnings("UnusedAssignment")
    public Vector3 getNextRail(double dx, double dy, double dz) {
        int checkX = MathHelper.floor(dx);
        int checkY = MathHelper.floor(dy);
        int checkZ = MathHelper.floor(dz);

        if (isRail(level.getBlock(new Vector3(checkX, checkY - 1, checkZ)))) {
            --checkY;
        }

        Block block = level.getBlock(new Vector3(checkX, checkY, checkZ));
        logger.info("ID: "  + block.getId());
        logger.info("Damage: " + block.getDamage());

        if (isRail(block)) {
            int l = block.getDamage();

            dy = (double) checkY;
            if (isRedstonePowered(block)) {
                l &= 7;
            }

            if (l >= 2 && l <= 5) {
                dy = (double) (checkY + 1);
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
                dx = (double) checkX + 0.5D;
                rail = dz - (double) checkZ;
            } else if (nextMax == 0.0D) {
                dz = (double) checkZ + 0.5D;
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

    private void moveMinecart(double motionX, double motionY, double motionZ) {
        move(motionX, motionY, motionZ);
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

    /**
     * Define if the Block is rail
     *
     * @param block Block of the current target
     * @return boolean
     */
    private boolean isRail(Block block) {
        switch (block.getId()) {
            case RAIL:
            case POWERED_RAIL:
            case ACTIVATOR_RAIL:
            case DETECTOR_RAIL:
                return true;
            default:
                return false;
        }
    }

    public void setMotion(double dx, double dy, double dz) {
        setMotion(new Vector3(dx, dy, dz));
        super.updateMovement();
    }

    /**
     * Define if the Block is Redstone-powered rail
     *
     * @param block Block of the current target
     * @return boolean
     */
    private boolean isRedstonePowered(Block block) {
        switch (block.getId()) {
            case POWERED_RAIL:
            case ACTIVATOR_RAIL:
            case DETECTOR_RAIL:
                return true;
            default:
                return false;
        }
    }

    private void prepareDataProperty() {
        // A long journey to wait Mojang changes these Entity Data
        // Notice: These flags will need to be checked after
        //         New version of Minecraft released
        setDataProperty(new IntEntityData(DATA_HEALTH, 6));
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
            offSet = 6; // todo: research
            namedTag.putInt("DisplayTile", display);
            namedTag.putInt("DisplayOffset", offSet);
        }
    }

    private Vector3 slice(Vector3 vec) {
        double ind = Math.sqrt(vec.x * vec.x + vec.y * vec.y + vec.z * vec.z);

        return ind < 1.0E-4D ? new Vector3(0.0D, 0.0D, 0.0D) : new Vector3(vec.x / ind, vec.y / ind, vec.z / ind);
    }

    /**
     * Set the block inside the minecart. ONLY WORKS ON MINECART EMPTY!
     *
     * @param block The block that will changed (Normal blocks only!)
     * @return {@code true} if the block is normal block
     */
    public boolean setBlockInside(Block block) {
        if (!block.isNormalBlock()) {
            return false;
        }
        blockInside = block;
        prepareDataProperty();
        return true;
    }
}
