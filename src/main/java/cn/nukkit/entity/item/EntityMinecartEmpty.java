package cn.nukkit.entity.item;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockRail;
import cn.nukkit.item.Item;
import cn.nukkit.Player;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.EntityEventPacket;

/**
 * Created by Snake1999 on 2016/1/30.
 * Package cn.nukkit.entity.item in project Nukkit.
 */
public class EntityMinecartEmpty extends EntityVehicle {

    public static final int NETWORK_ID = 84;

    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_CHEST = 2;
    public static final int TYPE_HOPPER = 3;
    public static final int TYPE_TNT = 4;
    
    public static final int STATE_INITIAL = 0;
    public static final int STATE_ON_RAIL = 1;
    public static final int STATE_OFF_RAIL = 2;
    
    public static final int DATA_VEHICLE_DISPLAY_BLOCK = 20;
    public static final int DATA_VEHICLE_DISPLAY_DATA = 20;
    public static final int DATA_VEHICLE_DISPLAY_OFFSET = 21;
    public static final int DATA_VEHICLE_CUSTOM_DISPLAY = 22;
    
    private state = Minecart::STATE_INITIAL;
    private direction = -1;
    private moveVector = [];
    private requestedPosition = null;
    
    @Override
    public float getHeight() {
        return 0.7f;
    }

    @Override
    public float getWidth() {
        return 0.98f;
    }

    @Override
    protected float getDrag() {
        return 0.1f;
    }

    @Override
    protected float getGravity() {
        return 0.5f;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }
    
    @Override
    public isMoving() {
        return false;
    }
    
    @Override
    public moveSpeed() {
        return 0.4f;
    }
    
    @Override
    public String getName() {
        return this.getNameTag();
    }
    
    @Override
    public int getType() {
        return self.TYPE_NORMAL;
    }

    public EntityMinecartEmpty(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected void initEntity() {
        setMaxHealth(1);
        setHealth(this->getMaxHealth());
        moveVector[Entity::NORTH] = new Vector3(-1, 0, 0);
        moveVector[Entity::SOUTH] = new Vector3(1, 0, 0);
		moveVector[Entity::EAST] = new Vector3(0, 0, -1);
		moveVector[Entity::WEST] = new Vector3(0, 0, 1);
        super.initEntity();
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed !== false) {
			return false;
		}
        tickDiff = currentTick - this.lastUpdate;
		if (tickDiff <= 1) {
			return false;
		}
        this.lastUpdate = currentTick;
        this.timings.startTiming();
        hasUpdate = false;
        if (this.isAlive()) {
			p = this.getLinkedEntity();
			if (p instanceof Player) {
				if (this.state == Minecart::STATE_INITIAL) {
					this.checkIfOnRail();
				} else if (this.state == Minecart::STATE_ON_RAIL) {
					hasUpdate = this.forwardOnRail(p);
					this.updateMovement();
				}
			}
		}
        this.timings.stopTiming ();
        return hasUpdate || ! this.onGround || abs ( this.motionX ) > 0.00001 || abs ( this.motionY ) > 0.00001 || abs ( this.motionZ ) > 0.00001;
        return super.onUpdate(currentTick);
    }
    
    @Override
    private void checkIfOnRail() {
		for (y = -1; y != 2 && this.state == Minecart::STATE_INITIAL; y++) {
			positionToCheck = this.temporalVector.setComponents(this.x, this.y + y, this.z);
			Block block = this.level.getBlock(positionToCheck);
			if (this.isRail(block)) {
				minecartPosition = positionToCheck.floor().add(0.5, 0, 0.5);
				this.setPosition(minecartPosition);    // Move minecart to center of rail
				this.state = Minecart::STATE_ON_RAIL;
			}
		}
		if (this.state != Minecart::STATE_ON_RAIL) {
			this.state = Minecart::STATE_OFF_RAIL;
		}
	}
    
    @Override
    private void isRail(rail) {
		return (rail != null && rail.getId(), {Block::RAIL, Block::ACTIVATOR_RAIL, Block::DETECTOR_RAIL, Block::POWERED_RAIL}));
	}
    
    @Override
    private void getCurrentRail() {
		Block block = this.getLevel().getBlock(this);
		if (this.isRail(block)) {
			return block;
		}
		// Rail could be one block below descending down
		down = this.temporalVector.setComponents(this.x, this.y - 1, this.z);
		Block block = this.getLevel().getBlock(down);
		if (this.isRail(block)) {
			return block;
		}
		return null;
	}
    
   /**
	 * Attempt to move forward on rail given the direction the cart is already moving, or if not moving based
	 * on the direction the player is looking.
	 * @param Player $player Player riding the minecart.
	 * @return boolean True if minecart moved, false otherwise.
	 */
    @Override
	private void forwardOnRail(Player player) {
		if (this.direction == -1) {
			candidateDirection = player.getDirection();
		} else {
			candidateDirection = this.direction;
		}
		rail = this.getCurrentRail();
		if (rail != null) {
			railType = rail.getDamage ();
			nextDirection = this.getDirectionToMove(railType, candidateDirection);
			if (nextDirection != -1) {
				this.direction = nextDirection;
				moved = this.checkForVertical(railType, nextDirection);
				if (!moved) {
					return this.moveIfRail();
				} else {
					return true;
				}
			} else {
				this.direction = -1;  // Was not able to determine direction to move, so wait for player to look in valid direction
			}
		} else {
			// Not able to find rail
			this.state = Minecart::STATE_INITIAL;
		}
		return false;
	}
    
    
    	/**
	 * Determine the direction the minecart should move based on the candidate direction (current direction
	 * minecart is moving, or the direction the player is looking) and the type of rail that the minecart is
	 * on.
	 * @param RailType $railType Type of rail the minecart is on.
	 * @param Direction $candidateDirection Direction minecart already moving, or direction player looking.
	 * @return Direction The direction the minecart should move.
	 */
    @Override
	private void getDirectionToMove(railType, candidateDirection) {
		switch(railType) {
			case Rail::STRAIGHT_NORTH_SOUTH:
			case Rail::SLOPED_ASCENDING_NORTH:
			case Rail::SLOPED_ASCENDING_SOUTH:
				switch(candidateDirection){
					case Entity::NORTH:
					case Entity::SOUTH:
						return candidateDirection;
				}
				break;
			case Rail::STRAIGHT_EAST_WEST:
			case Rail::SLOPED_ASCENDING_EAST:
			case Rail::SLOPED_ASCENDING_WEST:
				switch(candidateDirection){
					case Entity::WEST:
					case Entity::EAST:
						return candidateDirection;
				}
				break;
			case Rail::CURVED_SOUTH_EAST:
				switch(candidateDirection){
					case Entity::SOUTH:
					case Entity::EAST:
						return candidateDirection;
					case Entity::NORTH:
						return this.checkForTurn(candidateDirection, Entity::EAST);
					case Entity::WEST:
						return this.checkForTurn(candidateDirection, Entity::SOUTH);
				}
				break;
			case Rail::CURVED_SOUTH_WEST:
				switch(candidateDirection){
					case Entity::SOUTH:
					case Entity::WEST:
						return candidateDirection;
					case Entity::NORTH:
						return this->checkForTurn(candidateDirection, Entity::WEST);
					case Entity::EAST:
						return this.checkForTurn(candidateDirection, Entity::SOUTH);
				}
				break;
			case Rail::CURVED_NORTH_WEST:
				switch (candidateDirection) {
					case Entity::NORTH:
					case Entity::WEST:
						return candidateDirection;
					case Entity::SOUTH:
						return this->checkForTurn(candidateDirection, Entity::WEST);
					case Entity::EAST:
						return this->checkForTurn(candidateDirection, Entity::NORTH);
				}
				break;
			case Rail::CURVED_NORTH_EAST:
				switch (candidateDirection) {
					case Entity::NORTH:
					case Entity::EAST:
						return candidateDirection;
					case Entity::SOUTH:
						return this->checkForTurn(candidateDirection, Entity::EAST);
					case Entity::WEST:
						return this->checkForTurn(candidateDirection, Entity::NORTH);
				}
				break;
		}
		return -1;
	}
    
    	/**
	 * Need to alter direction on curves halfway through the turn and reset the minecart to be in the middle of
	 * the rail again so as not to collide with nearby blocks.
	 * @param Direction $currentDirection Direction minecart currently moving
	 * @param Direction $newDirection Direction minecart should turn once has hit the halfway point.
	 * @return Direction Either the current direction or the new direction depending on haw far across the rail the
	 * minecart is.
	 */
    @Override
	private void checkForTurn(currentDirection, newDirection) {
		switch(currentDirection) {
			case Entity::NORTH:
				diff = this.x - this.getFloorX();
				if (diff != 0 && diff <= .5) {
					dx = (this.getFloorX() + .5) - this.x;
					this.move(dx, 0, 0);
					return newDirection;
				}
				break;
			case Entity::SOUTH:
				diff = this.x - this.getFloorX();
				if (diff != 0 && diff >= .5) {
					dx = (this.getFloorX() + .5) - this.x;
					this.move(dx, 0, 0);
					return newDirection;
				}
				break;
			case Entity::EAST:
				diff = this.z - this.getFloorZ();
				if (diff != 0 && diff <= .5) {
					dz = (this.getFloorZ() + .5) - this.z;
					this.move(0, 0, dz);
					return newDirection;
				}
				break;
			case Entity::WEST:
				diff = this.z - this.getFloorZ();
				if (diff != 0 && diff >= .5) {
					dz = dz = (this.getFloorZ() + .5) - this.z;
					this.move(0, 0, dz);
					return newDirection;
				}
				break;
		}
		return currentDirection;
	}
    
    @Override
    private void checkForVertical(railType, currentDirection) {
		switch (railType) {
			case Rail::SLOPED_ASCENDING_NORTH:
				switch(currentDirection){
					case Entity::NORTH:
						// Headed north up
						diff = this.x - this.getFloorX();
						if (diff != 0 && diff <= .5) {
							dx = (this.getFloorX() - .1) - this.x;
							this.move(dx, 1, 0);
							return true;
						}
						break;
					case ENTITY::SOUTH:
						// Headed south down
						diff = this.x - this.getFloorX();
						if ($diff !== 0 and $diff >= .5) {
							$dx = ($this->getFloorX() + 1 ) - $this->x;
							$this->move($dx, -1, 0);
							return true;
						}
						break;
				}
				break;
			case Rail::SLOPED_ASCENDING_SOUTH:
				switch($currentDirection){
					case Entity::SOUTH:
						// Headed south up
						$diff = $this->x - $this->getFloorX();
						if ($diff !== 0 and $diff >= .5) {
							$dx = ($this->getFloorX() + 1 ) - $this->x;
							$this->move($dx, 1, 0);
							return true;
						}
						break;
					case Entity::NORTH:
						// Headed north down
						$diff = $this->x - $this->getFloorX();
						if ($diff !== 0 and $diff <= .5) {
							$dx = ($this->getFloorX() - .1) - $this->x;
							$this->move($dx, -1, 0);
							return true;
						}
						break;
				}
				break;
			case Rail::SLOPED_ASCENDING_EAST:
				switch($currentDirection){
					case Entity::EAST:
						// Headed east up
						$diff = $this->z - $this->getFloorZ();
						if ($diff !== 0 and $diff <= .5) {
							$dz = ($this->getFloorZ() - .1) - $this->z;
							$this->move(0, 1, $dz);
							return true;
						}
						break;
					case Entity::WEST:
						// Headed west down
						$diff = $this->z - $this->getFloorZ();
						if ($diff !== 0 and $diff >= .5) {
							$dz = ($this->getFloorZ() + 1) - $this->z;
							$this->move(0, -1, $dz);
							return true;
						}
						break;
				}
				break;
			case Rail::SLOPED_ASCENDING_WEST:
				switch($currentDirection){
					case Entity::WEST:
						// Headed west up
						$diff = $this->z - $this->getFloorZ();
						if ($diff !== 0 and $diff >= .5) {
							$dz = ($this->getFloorZ() + 1) - $this->z;
							$this->move(0, 1, $dz);
							return true;
						}
						break;
					case Entity::EAST:
						// Headed east down
						$diff = $this->z - $this->getFloorZ();
						if ($diff !== 0 and $diff <= .5) {
							$dz = ($this->getFloorZ() - .1) - $this->z;
							$this->move(0, -1, $dz);
							return true;
						}
						break;
				}
				break;
		}
		return false;
	}


    @Override
    public void spawnTo(Player player) {
        AddEntityPacket pk = new AddEntityPacket();
        pk.eid = this.getId();
        pk.type = EntityMinecartEmpty.NETWORK_ID;
        pk.x = (float) this.x;
        pk.y = (float) this.y;
        pk.z = (float) this.z;
        pk.speedX = 0;
        pk.speedY = 0;
        pk.speedZ = 0;
        pk.yaw = 0;
        pk.pitch = 0;
        pk.metadata = this.dataProperties;
        player.dataPacket(pk);

        super.spawnTo(player);
    }

    @Override
    public void attack(EntityDamageEvent source) {
        super.attack(source);
        if (source.isCancelled()) return;

        EntityEventPacket pk = new EntityEventPacket();
        pk.eid = this.id;
        pk.event = EntityEventPacket.HURT_ANIMATION;
        for (Player aPlayer : this.getLevel().getPlayers().values()) {
            aPlayer.dataPacket(pk);
        }
    }

}
