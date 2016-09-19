package cn.nukkit.level;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockFire;
//import cn.nukkit.block.BlockPosition;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.item.EntityFallingBlock;
import cn.nukkit.entity.item.EntityPrimedTNT;
import cn.nukkit.event.block.BlockUpdateEvent;
import cn.nukkit.entity.item.EntityFallingBlock;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.protection.*;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.particle.*;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.*;
import cn.nukkit.network.protocol.ExplodePacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * author: Angelic47
 * Nukkit Project
 */
public class Explosion {
    private final boolean a;
    private final boolean b;
    private final Random c = new Random();
    private final World world;
    private final double posX;
    private final double posY;
    private final double posZ;
    public final Entity source;
    private final float size;
    private final List<BlockPosition> blocks = Lists.newArrayList();
    private final Map<EntityHuman, Vec3D> k = Maps.newHashMap();
    public boolean wasCanceled = false;

    public Explosion(World world, Entity entity, double d0, double d1, double d2, float f, boolean flag, boolean flag1) {
        this.world = world;
        this.source = entity;
        this.size = (float)Math.max((double)f, 0.0);
        this.posX = d0;
        this.posY = d1;
        this.posZ = d2;
        this.a = flag;
        this.b = flag1;
    }

    public void a() {
        int j;
        int i;
        if (this.size < 0.1f) {
            return;
        }
        HashSet<BlockPosition> hashset = Sets.newHashSet();
        int k = 0;
        while (k < 16) {
            i = 0;
            while (i < 16) {
                j = 0;
                while (j < 16) {
                    if (k == 0 || k == 15 || i == 0 || i == 15 || j == 0 || j == 15) {
                        double d0 = (float)k / 15.0f * 2.0f - 1.0f;
                        double d1 = (float)i / 15.0f * 2.0f - 1.0f;
                        double d2 = (float)j / 15.0f * 2.0f - 1.0f;
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        float f = this.size * (0.7f + this.world.random.nextFloat() * 0.6f);
                        double d4 = this.posX;
                        double d5 = this.posY;
                        double d6 = this.posZ;
                        while (f > 0.0f) {
                            BlockPosition blockposition = new BlockPosition(d4, d5, d6);
                            IBlockData iblockdata = this.world.getType(blockposition);
                            if (iblockdata.getMaterial() != Material.AIR) {
                                float f2 = this.source != null ? this.source.a(this, this.world, blockposition, iblockdata) : iblockdata.getBlock().a((Entity)null);
                                f -= (f2 + 0.3f) * 0.3f;
                            }
                            if (f > 0.0f && (this.source == null || this.source.a(this, this.world, blockposition, iblockdata, f)) && blockposition.getY() < 256 && blockposition.getY() >= 0) {
                                hashset.add(blockposition);
                            }
                            d4 += d0 * 0.30000001192092896;
                            d5 += d1 * 0.30000001192092896;
                            d6 += d2 * 0.30000001192092896;
                            f -= 0.22500001f;
                        }
                    }
                    ++j;
                }
                ++i;
            }
            ++k;
        }
        this.blocks.addAll(hashset);
        float f3 = this.size * 2.0f;
        i = NukkitMath.floor(this.posX - (double)f3 - 1.0);
        j = NukkitMath.floor(this.posX + (double)f3 + 1.0);
        int l = NukkitMath.floor(this.posY - (double)f3 - 1.0);
        int i1 = NukkitMath.floor(this.posY + (double)f3 + 1.0);
        int j1 = NukkitMath.floor(this.posZ - (double)f3 - 1.0);
        int k1 = NukkitMath.floor(this.posZ + (double)f3 + 1.0);
        List<Entity> list = this.world.getEntities(this.source, new AxisAlignedBB(i, l, j1, j, i1, k1));
        Vector3 vector3 = new Vector3(this.posX, this.posY, this.posZ);
        int l1 = 0;
        while (l1 < list.size()) {
            double d10;
            double d7;
            double d9;
            double d8;
            double d11;
            Entity entity = list.get(l1);
            if (!entity.bt() && (d7 = entity.f(this.posX, this.posY, this.posZ) / (double)f3) <= 1.0 && (d11 = (double)NukkitMath.sqrt((d8 = entity.locX - this.posX) * d8 + (d9 = entity.locY + (double)entity.getHeadHeight() - this.posY) * d9 + (d10 = entity.locZ - this.posZ) * d10)) != 0.0) {
                d8 /= d11;
                d9 /= d11;
                d10 /= d11;
                double d12 = this.world.a(vector3, entity.getBoundingBox());
                double d13 = (1.0 - d7) * d12;
                //CraftEventFactory.entityDamage = this.source;
                //entity.forceExplosionKnockback = false;
                boolean wasDamaged = entity.damageEntity(DamageSource.explosion(this), (int)((d13 * d13 + d13) / 2.0 * 7.0 * (double)f3 + 1.0));
                //CraftEventFactory.entityDamage = null;
                if (wasDamaged || entity instanceof EntityPrimedTNT || entity instanceof EntityFallingBlock/* || entity.forceExplosionKnockback*/) {
                    EntityHuman entityhuman;
                    double d14 = 1.0;
                    if (entity instanceof EntityLiving) {
                        d14 = EnchantmentProtection.a((EntityLiving)entity, d13);
                    }
                    entity.motX += d8 * d14;
                    entity.motY += d9 * d14;
                    entity.motZ += d10 * d14;
                    if (!(!(entity instanceof EntityHuman) || (entityhuman = (EntityHuman)entity).isSpectator() || entityhuman.z() && entityhuman.abilities.isFlying)) {
                        this.k.put(entityhuman, new Vec3D(d8 * d13, d9 * d13, d10 * d13));
                    }
                }
            }
            ++l1;
        }
    }

    public void a(boolean flag) {
        this.world.a(null, this.posX, this.posY, this.posZ, SoundEffects.bF, SoundCategory.BLOCKS, 4.0f, (1.0f + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2f) * 0.7f);
        if (this.size >= 2.0f && this.b) {
            this.world.addParticle(EnumParticle.EXPLOSION_HUGE, this.posX, this.posY, this.posZ, 1.0, 0.0, 0.0, new int[0]);
        } else {
            this.world.addParticle(EnumParticle.EXPLOSION_LARGE, this.posX, this.posY, this.posZ, 1.0, 0.0, 0.0, new int[0]);
        }
        if (this.b) {
            boolean cancelled;
            float yield;
            List<cn.nukkit.block.Block> bukkitBlocks;
            EntityExplodeEvent event;
            //CraftWorld bworld = this.world.getWorld();
            //CraftEntity explode = this.source == null ? null : this.source.getBukkitEntity();
            Location location = new Location(bworld, this.posX, this.posY, this.posZ);
            ArrayList<cn.nukkit.block.Block> blockList = Lists.newArrayList();
            int i1 = this.blocks.size() - 1;
            while (i1 >= 0) {
                BlockPosition cpos = this.blocks.get(i1);
                cn.nukkit.block.Block bblock = bworld.getBlockAt(cpos.getX(), cpos.getY(), cpos.getZ());
                if (bblock.getType() != cn.nukkit.Material.AIR) {
                    blockList.add(bblock);
                }
                --i1;
            }
            if (explode != null) {
                event = new EntityExplodeEvent(explode, location, blockList, 1.0f / this.size);
                this.world.getServer().getPluginManager().callEvent(event);
                cancelled = event.isCancelled();
                bukkitBlocks = event.blockList();
                yield = event.getYield();
            } else {
                event = new BlockExplodeEvent(location.getBlock(), blockList, 1.0f / this.size);
                this.world.getServer().getPluginManager().callEvent(event);
                cancelled = event.isCancelled();
                bukkitBlocks = event.blockList();
                yield = event.getYield();
            }
            this.blocks.clear();
            for (cn.nukkit.block.Block bblock : bukkitBlocks) {
                BlockPosition coords = new BlockPosition(bblock.getX(), bblock.getY(), bblock.getZ());
                this.blocks.add(coords);
            }
            if (cancelled) {
                this.wasCanceled = true;
                return;
            }
            for (BlockPosition blockposition : this.blocks) {
                IBlockData iblockdata = this.world.getType(blockposition);
                Block block = iblockdata.getBlock();
                if (flag) {
                    double d0 = (float)blockposition.getX() + this.world.random.nextFloat();
                    double d1 = (float)blockposition.getY() + this.world.random.nextFloat();
                    double d2 = (float)blockposition.getZ() + this.world.random.nextFloat();
                    double d3 = d0 - this.posX;
                    double d4 = d1 - this.posY;
                    double d5 = d2 - this.posZ;
                    double d6 = NukkitMath.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                    d3 /= d6;
                    d4 /= d6;
                    d5 /= d6;
                    double d7 = 0.5 / (d6 / (double)this.size + 0.1);
                    this.world.addParticle(EnumParticle.EXPLOSION_NORMAL, (d0 + this.posX) / 2.0, (d1 + this.posY) / 2.0, (d2 + this.posZ) / 2.0, d3 *= (d7 *= (double)(this.world.random.nextFloat() * this.world.random.nextFloat() + 0.3f)), d4 *= d7, d5 *= d7, new int[0]);
                    this.world.addParticle(EnumParticle.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5, new int[0]);
                }
                if (iblockdata.getMaterial() == Material.AIR) continue;
                if (block.a(this)) {
                    block.dropNaturally(this.world, blockposition, this.world.getType(blockposition), yield, 0);
                }
                this.world.setTypeAndData(blockposition, Blocks.AIR.getBlockData(), 3);
                block.wasExploded(this.world, blockposition, this);
            }
        }
        if (this.a) {
            for (BlockPosition blockposition : this.blocks) {
                if (this.world.getType(blockposition).getMaterial() != Material.AIR || !this.world.getType(blockposition.down()).b() || this.c.nextInt(3) != 0 /*|| CraftEventFactory.callBlockIgniteEvent(this.world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this).isCancelled())*/ continue;
                this.world.setTypeUpdate(blockposition, Blocks.FIRE.getBlockData());
            }
        }
    }

    public Map<EntityHuman, Vec3D> b() {
        return this.k;
    }

    public EntityLiving getSource() {
        return this.source == null ? null : (this.source instanceof EntityTNTPrimed ? ((EntityTNTPrimed)this.source).getSource() : (this.source instanceof EntityLiving ? (EntityLiving)this.source : (this.source instanceof EntityFireball ? ((EntityFireball)this.source).shooter : null)));
    }

    public void clearBlocks() {
        this.blocks.clear();
    }

    public List<BlockPosition> getBlocks() {
        return this.blocks;
    }
}
