package cn.nukkit.entity;

import cn.nukkit.Player;
import cn.nukkit.event.entity.ExplosionPrimeEvent;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;

public class EntityEnderCrystal extends EntityLiving implements EntityExplosive {
    
    public static final int NETWORK_ID = 71;
    
    public int width = 1;
    public int height = 1;
    public int length = 1; // TODO: Size
    
    public EntityEnderCrystal(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.setHealth(1);
        this.setMaxHealth(1);
    }

    @Override
    public String getName() {
        return "Ender Crystal";
    }
    
    @Override
    public void kill() {
        if (!this.isAlive()) {
            return;
        }
        this.explode();
        if (!this.closed) {
            this.close();
        }
    }
    
    @Override
    public boolean setMotion(Vector3 mot) {
        return false;
    }
    
    @Override
    public void explode() {
        this.close();
        ExplosionPrimeEvent ev = new ExplosionPrimeEvent(this, 6);
        this.getServer().getPluginManager().callEvent(ev);
        
        if (!ev.isCancelled()) {
            Explosion explosion = new Explosion(this, ev.getForce(), this);
            if (ev.isBlockBreaking()) {
                explosion.explodeA();
            }
            explosion.explodeB();
        }
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
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
}
