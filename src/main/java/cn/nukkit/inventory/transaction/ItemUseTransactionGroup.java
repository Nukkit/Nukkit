package cn.nukkit.inventory.transaction;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockDoor;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityExpBottle;
import cn.nukkit.entity.item.EntityPotion;
import cn.nukkit.entity.projectile.EntityEgg;
import cn.nukkit.entity.projectile.EntityEnderPearl;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.entity.projectile.EntitySnowball;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.sound.LaunchSound;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.UpdateBlockPacket;

public class ItemUseTransactionGroup extends BaseTransactionGroup {

    private final int action;
    private final BlockVector3 blockPos;
    private final int face;
    private final int slot;
    private final Item item;
    private final Vector3f playerPos;
    private final Vector3f clickPos;

    public ItemUseTransactionGroup(int action, BlockVector3 blockPos, int face, int slot, Item item, Vector3f playerPos, Vector3f clickPos) {
        this.action = action;
        this.blockPos = blockPos;
        this.face = face;
        this.slot = slot;
        this.item = item;
        this.playerPos = playerPos;
        this.clickPos = clickPos;
    }

    @Override
    public boolean executeOn(Player player, boolean force) {
        if (this.hasExecuted || (!force && !this.canExecute(player))) {
            return false;
        }

        player.craftingType = Player.CRAFTING_SMALL;

        player.setDataFlag(Player.DATA_FLAGS, Player.DATA_FLAG_ACTION, false);

        Vector3 blockVector = new Vector3(blockPos.x, blockPos.y, blockPos.z);
        PlayerInventory inventory = player.getInventory();
        BlockFace face;

        switch (this.action) {
            case ITEM_USE_ACTION_PLACE:
                face = BlockFace.fromIndex(this.face);
                if (player.canInteract(blockVector.add(0.5, 0.5, 0.5), player.isCreative() ? 13 : 7)) {
                    if (player.isCreative()) {
                        Item i = inventory.getItemInHand();
                        if (player.level.usePlaceOn(blockVector, i, face, clickPos.x, clickPos.y, clickPos.z, player) != null) {
                            return true;
                        }
                    } else if (inventory.getItemInHand().deepEquals(item)) {
                        Item i = inventory.getItemInHand();
                        Item oldItem = i.clone();
                        //TODO: Implement adventure mode checks
                        if ((i = player.level.usePlaceOn(blockVector, i, face, clickPos.x, clickPos.y, clickPos.z, player)) != null) {
                            if (!i.deepEquals(oldItem) || i.getCount() != oldItem.getCount()) {
                                inventory.setItemInHand(i);
                                inventory.sendHeldItem(player.getViewers().values());
                            }
                            return true;
                        }
                    }
                }
                return false;

            case ITEM_USE_ACTION_USE:
                if (this.face >= 0 && this.face <= 5) {
                    face = BlockFace.fromIndex(this.face);
                    if (player.canInteract(blockVector.add(0.5, 0.5, 0.5), player.isCreative() ? 13 : 7)) {
                        if (player.isCreative()) {
                            Item i = inventory.getItemInHand();
                            if (player.level.useItemOn(blockVector, i, face, clickPos.x, clickPos.y, clickPos.z, player) != null) {
                                return true;
                            }
                        } else if (inventory.getItemInHand().deepEquals(item)) {
                            Item i = inventory.getItemInHand();
                            Item oldItem = i.clone();
                            //TODO: Implement adventure mode checks
                            if ((i = player.level.useItemOn(blockVector, i, face, clickPos.x, clickPos.y, clickPos.z, player)) != null) {
                                if (!i.deepEquals(oldItem) || i.getCount() != oldItem.getCount()) {
                                    inventory.setItemInHand(i);
                                    inventory.sendHeldItem(player.getViewers().values());
                                }
                                return true;
                            }
                        }
                    }

                    inventory.sendHeldItem(player);

                    if (blockVector.distanceSquared(player) > 10000) {
                        return false;
                    }

                    Block target = player.level.getBlock(blockVector);
                    Block block = target.getSide(face);

                    if (target instanceof BlockDoor) {
                        BlockDoor door = (BlockDoor) target;

                        Block part;

                        if ((door.getDamage() & 0x08) > 0) { //up
                            part = target.down();

                            if (part.getId() == target.getId()) {
                                target = part;
                            }
                        }
                        player.level.sendBlocks(new Player[]{player}, new Block[]{target, block}, UpdateBlockPacket.FLAG_ALL_PRIORITY);
                    }
                } else if (this.face == -1) {
                    Vector3 aimPos = new Vector3(
                            -Math.sin(player.yaw / 180d * Math.PI) * Math.cos(player.pitch / 180d * Math.PI),
                            -Math.sin(player.pitch / 180d * Math.PI),
                            Math.cos(player.yaw / 180d * Math.PI) * Math.cos(player.pitch / 180d * Math.PI)
                    );

                    Item i;
                    if (player.isCreative() || inventory.getItemInHand().deepEquals(item)) {
                        i = inventory.getItemInHand();
                    } else {
                        inventory.sendHeldItem(player);
                        return false;
                    }

                    PlayerInteractEvent playerInteractEvent = new PlayerInteractEvent(player, i, aimPos, null, PlayerInteractEvent.Action.RIGHT_CLICK_AIR);

                    Server.getInstance().getPluginManager().callEvent(playerInteractEvent);

                    if (playerInteractEvent.isCancelled()) {
                        inventory.sendHeldItem(player);
                        return false;
                    }

                    if (i.getId() == Item.SNOWBALL) {
                        CompoundTag nbt = new CompoundTag()
                                .putList(new ListTag<DoubleTag>("Pos")
                                        .add(new DoubleTag("", player.x))
                                        .add(new DoubleTag("", player.y + player.getEyeHeight()))
                                        .add(new DoubleTag("", player.z)))
                                .putList(new ListTag<DoubleTag>("Motion")
                                        .add(new DoubleTag("", -Math.sin(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI)))
                                        .add(new DoubleTag("", -Math.sin(player.pitch / 180 * Math.PI)))
                                        .add(new DoubleTag("", Math.cos(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI))))
                                .putList(new ListTag<FloatTag>("Rotation")
                                        .add(new FloatTag("", (float) player.yaw))
                                        .add(new FloatTag("", (float) player.pitch)));

                        float f = 1.5f;
                        EntitySnowball snowball = new EntitySnowball(player.chunk, nbt, player);

                        snowball.setMotion(snowball.getMotion().multiply(f));
                        if (player.isSurvival()) {
                            i.setCount(i.getCount() - 1);
                            inventory.setItemInHand(i.getCount() > 0 ? i : Item.get(Item.AIR));
                        }
                        ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent(snowball);
                        Server.getInstance().getPluginManager().callEvent(projectileLaunchEvent);
                        if (projectileLaunchEvent.isCancelled()) {
                            snowball.kill();
                        } else {
                            snowball.spawnToAll();
                            player.level.addSound(new LaunchSound(player), player.getViewers().values());
                        }
                    } else if (i.getId() == Item.EGG) {
                        CompoundTag nbt = new CompoundTag()
                                .putList(new ListTag<DoubleTag>("Pos")
                                        .add(new DoubleTag("", player.x))
                                        .add(new DoubleTag("", player.y + player.getEyeHeight()))
                                        .add(new DoubleTag("", player.z)))
                                .putList(new ListTag<DoubleTag>("Motion")
                                        .add(new DoubleTag("", -Math.sin(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI)))
                                        .add(new DoubleTag("", -Math.sin(player.pitch / 180 * Math.PI)))
                                        .add(new DoubleTag("", Math.cos(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI))))
                                .putList(new ListTag<FloatTag>("Rotation")
                                        .add(new FloatTag("", (float) player.yaw))
                                        .add(new FloatTag("", (float) player.pitch)));

                        float f = 1.5f;
                        EntityEgg egg = new EntityEgg(player.chunk, nbt, player);

                        egg.setMotion(egg.getMotion().multiply(f));
                        if (player.isSurvival()) {
                            i.setCount(i.getCount() - 1);
                            inventory.setItemInHand(i.getCount() > 0 ? i : Item.get(Item.AIR));
                        }
                        ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent(egg);
                        Server.getInstance().getPluginManager().callEvent(projectileLaunchEvent);
                        if (projectileLaunchEvent.isCancelled()) {
                            egg.kill();
                        } else {
                            egg.spawnToAll();
                            player.level.addSound(new LaunchSound(player), player.getViewers().values());
                        }
                    } else if (i.getId() == Item.ENDER_PEARL && (Server.getInstance().getTick() - player.getLastEnderPearlThrowingTick()) >= 20) {
                        CompoundTag nbt = new CompoundTag()
                                .putList(new ListTag<DoubleTag>("Pos")
                                        .add(new DoubleTag("", player.x))
                                        .add(new DoubleTag("", player.y + player.getEyeHeight()))
                                        .add(new DoubleTag("", player.z)))
                                .putList(new ListTag<DoubleTag>("Motion")
                                        .add(new DoubleTag("", -Math.sin(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI)))
                                        .add(new DoubleTag("", -Math.sin(player.pitch / 180 * Math.PI)))
                                        .add(new DoubleTag("", Math.cos(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI))))
                                .putList(new ListTag<FloatTag>("Rotation")
                                        .add(new FloatTag("", (float) player.yaw))
                                        .add(new FloatTag("", (float) player.pitch)));

                        float f = 1.5f;
                        EntityEnderPearl enderPearl = new EntityEnderPearl(player.chunk, nbt, player);

                        enderPearl.setMotion(enderPearl.getMotion().multiply(f));
                        if (player.isSurvival()) {
                            i.setCount(i.getCount() - 1);
                            inventory.setItemInHand(i.getCount() > 0 ? i : Item.get(Item.AIR));
                        }
                        ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent(enderPearl);
                        Server.getInstance().getPluginManager().callEvent(projectileLaunchEvent);
                        if (projectileLaunchEvent.isCancelled()) {
                            enderPearl.kill();
                        } else {
                            enderPearl.spawnToAll();
                            player.level.addSound(new LaunchSound(player), player.getViewers().values());
                        }
                        player.onThrowEnderPearl();
                    } else if (i.getId() == Item.EXPERIENCE_BOTTLE) {
                        CompoundTag nbt = new CompoundTag()
                                .putList(new ListTag<DoubleTag>("Pos")
                                        .add(new DoubleTag("", player.x))
                                        .add(new DoubleTag("", player.y + player.getEyeHeight()))
                                        .add(new DoubleTag("", player.z)))
                                .putList(new ListTag<DoubleTag>("Motion")
                                        .add(new DoubleTag("", -Math.sin(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI)))
                                        .add(new DoubleTag("", -Math.sin(player.pitch / 180 * Math.PI)))
                                        .add(new DoubleTag("", Math.cos(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI))))
                                .putList(new ListTag<FloatTag>("Rotation")
                                        .add(new FloatTag("", (float) player.yaw))
                                        .add(new FloatTag("", (float) player.pitch)))
                                .putInt("Potion", i.getDamage());
                        double f = 1.5;
                        Entity bottle = new EntityExpBottle(player.chunk, nbt, player);
                        bottle.setMotion(bottle.getMotion().multiply(f));
                        if (player.isSurvival()) {
                            i.setCount(i.getCount() - 1);
                            inventory.setItemInHand(i.getCount() > 0 ? i : Item.get(Item.AIR));
                        }
                        EntityProjectile bottleEntity = (EntityProjectile) bottle;
                        ProjectileLaunchEvent projectileEv = new ProjectileLaunchEvent(bottleEntity);
                        Server.getInstance().getPluginManager().callEvent(projectileEv);
                        if (projectileEv.isCancelled()) {
                            bottle.kill();
                        } else {
                            bottle.spawnToAll();
                            player.level.addSound(new LaunchSound(player), player.getViewers().values());
                        }
                    } else if (i.getId() == Item.SPLASH_POTION) {
                        CompoundTag nbt = new CompoundTag()
                                .putList(new ListTag<DoubleTag>("Pos")
                                        .add(new DoubleTag("", player.x))
                                        .add(new DoubleTag("", player.y + player.getEyeHeight()))
                                        .add(new DoubleTag("", player.z)))
                                .putList(new ListTag<DoubleTag>("Motion")
                                        .add(new DoubleTag("", -Math.sin(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI)))
                                        .add(new DoubleTag("", -Math.sin(player.pitch / 180 * Math.PI)))
                                        .add(new DoubleTag("", Math.cos(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI))))
                                .putList(new ListTag<FloatTag>("Rotation")
                                        .add(new FloatTag("", (float) player.yaw))
                                        .add(new FloatTag("", (float) player.pitch)))
                                .putShort("PotionId", i.getDamage());
                        double f = 1.5;
                        Entity bottle = new EntityPotion(player.chunk, nbt, player);
                        bottle.setMotion(bottle.getMotion().multiply(f));
                        if (player.isSurvival()) {
                            i.setCount(i.getCount() - 1);
                            inventory.setItemInHand(i.getCount() > 0 ? i : Item.get(Item.AIR));
                        }
                        EntityPotion bottleEntity = (EntityPotion) bottle;
                        ProjectileLaunchEvent projectileEv = new ProjectileLaunchEvent(bottleEntity);
                        Server.getInstance().getPluginManager().callEvent(projectileEv);
                        if (projectileEv.isCancelled()) {
                            bottle.kill();
                        } else {
                            bottle.spawnToAll();
                            player.level.addSound(new LaunchSound(player), player.getViewers().values());
                        }
                    }
                    player.setDataFlag(Player.DATA_FLAGS, Player.DATA_FLAG_ACTION, true);
                    player.startAction();
                }
                break;

            case ITEM_USE_ACTION_DESTROY:
                Item i = player.getInventory().getItemInHand();

                Item oldItem = i.clone();

                if (player.canInteract(blockVector.add(0.5, 0.5, 0.5), player.isCreative() ? 13 : 7) && (i = player.level.useBreakOn(blockVector, i, player, true)) != null) {
                    if (player.isSurvival()) {
                        player.getFoodData().updateFoodExpLevel(0.025);
                        if (!i.deepEquals(oldItem) || i.getCount() != oldItem.getCount()) {
                            inventory.setItemInHand(i);
                            inventory.sendHeldItem(player.getViewers().values());
                        }
                    }
                    return true;
                }

                inventory.sendContents(player);
                Block target = player.level.getBlock(blockVector);
                BlockEntity blockEntity = player.level.getBlockEntity(blockVector);

                player.level.sendBlocks(new Player[]{player}, new Block[]{target}, UpdateBlockPacket.FLAG_ALL_PRIORITY);

                inventory.sendHeldItem(player);

                if (blockEntity instanceof BlockEntitySpawnable) {
                    ((BlockEntitySpawnable) blockEntity).spawnTo(player);
                }
                return false;
        }
        return true;
    }

    @Override
    public boolean canExecute(Player player) {
        return player.spawned && player.isAlive();
    }

    public int getAction() {
        return action;
    }

    public BlockVector3 getBlockPos() {
        return blockPos;
    }

    public int getFace() {
        return face;
    }

    public int getSlot() {
        return slot;
    }

    public Item getItem() {
        return item;
    }

    public Vector3f getPlayerPos() {
        return playerPos;
    }

    public Vector3f getClickPos() {
        return clickPos;
    }

    @Override
    public int getType() {
        return TYPE_ITEM_USE;
    }
}
