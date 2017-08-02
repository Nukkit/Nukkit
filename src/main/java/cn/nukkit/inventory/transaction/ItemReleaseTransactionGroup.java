package cn.nukkit.inventory.transaction;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.BlockAir;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemArrow;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.sound.LaunchSound;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;

import java.util.Random;

public class ItemReleaseTransactionGroup extends BaseTransactionGroup {

    private final int action;
    private final int slot;
    private final Item item;
    private final Vector3f playerPos;

    public ItemReleaseTransactionGroup(int action, int slot, Item item, Vector3f playerPos) {
        this.action = action;
        this.slot = slot;
        this.item = item;
        this.playerPos = playerPos;
    }

    @Override
    public boolean executeOn(Player player, boolean force) {
        if (this.hasExecuted || (!force && !this.canExecute(player))) {
            return false;
        }
        player.craftingType = Player.CRAFTING_SMALL;
        PlayerInventory inventory = player.getInventory();

        switch (this.action) {
            case ITEM_RELEASE_ACTION_RELEASE:
                if (item.getId() <= 0) {
                    return false;
                }

                Item i = (player.isCreative() || inventory.contains(item)) ? item : inventory.getItemInHand();
                PlayerDropItemEvent dropItemEvent = new PlayerDropItemEvent(player, i);
                Server.getInstance().getPluginManager().callEvent(dropItemEvent);
                if (dropItemEvent.isCancelled()) {
                    inventory.sendContents(player);
                    return false;
                }

                if (!player.isCreative()) {
                    inventory.removeItem(i);
                }
                Vector3 motion = player.getDirectionVector().multiply(0.4);

                player.level.dropItem(player.add(0, 1.3, 0), i, motion, 40);

                player.setDataFlag(Player.DATA_FLAGS, Player.DATA_FLAG_ACTION, false);
                break;
            case ITEM_RELEASE_ACTION_USE:
                if (player.getStartActionTick() > -1 && player.getDataFlag(Player.DATA_FLAGS, Player.DATA_FLAG_ACTION)) {
                    if (inventory.getItemInHand().getId() == Item.BOW) {

                        Item bow = inventory.getItemInHand();
                        ItemArrow itemArrow = new ItemArrow();
                        if (player.isSurvival() && !inventory.contains(itemArrow)) {
                            inventory.sendContents(player);
                            break;
                        }

                        double damage = 2;
                        boolean flame = false;

                        if (bow.hasEnchantments()) {
                            Enchantment bowDamage = bow.getEnchantment(Enchantment.ID_BOW_POWER);

                            if (bowDamage != null && bowDamage.getLevel() > 0) {
                                damage += 0.25 * (bowDamage.getLevel() + 1);
                            }

                            Enchantment flameEnchant = bow.getEnchantment(Enchantment.ID_BOW_FLAME);
                            flame = flameEnchant != null && flameEnchant.getLevel() > 0;
                        }

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
                                        .add(new FloatTag("", (player.yaw > 180 ? 360 : 0) - (float) player.yaw))
                                        .add(new FloatTag("", (float) -player.pitch)))
                                .putShort("Fire", player.isOnFire() || flame ? 45 * 60 : 0)
                                .putDouble("damage", damage);

                        int diff = (Server.getInstance().getTick() - player.getStartActionTick());
                        double p = (double) diff / 20;

                        double f = Math.min((p * p + p * 2) / 3, 1) * 2;
                        EntityShootBowEvent entityShootBowEvent = new EntityShootBowEvent(player, bow, new EntityArrow(player.chunk, nbt, player, f == 2), f);

                        if (f < 0.1 || diff < 5) {
                            entityShootBowEvent.setCancelled();
                        }

                        Server.getInstance().getPluginManager().callEvent(entityShootBowEvent);
                        if (entityShootBowEvent.isCancelled()) {
                            entityShootBowEvent.getProjectile().kill();
                            inventory.sendContents(player);
                        } else {
                            entityShootBowEvent.getProjectile().setMotion(entityShootBowEvent.getProjectile().getMotion().multiply(entityShootBowEvent.getForce()));
                            if (player.isSurvival()) {
                                Enchantment infinity;

                                if (!bow.hasEnchantments() || (infinity = bow.getEnchantment(Enchantment.ID_BOW_INFINITY)) == null || infinity.getLevel() <= 0)
                                    inventory.removeItem(itemArrow);

                                if (!bow.isUnbreakable()) {
                                    Enchantment durability = bow.getEnchantment(Enchantment.ID_DURABILITY);
                                    if (!(durability != null && durability.getLevel() > 0 && (100 / (durability.getLevel() + 1)) <= new Random().nextInt(100))) {
                                        bow.setDamage(bow.getDamage() + 1);
                                        if (bow.getDamage() >= 385) {
                                            inventory.setItemInHand(new ItemBlock(new BlockAir(), 0, 0));
                                        } else {
                                            inventory.setItemInHand(bow);
                                        }
                                    }
                                }
                            }
                            if (entityShootBowEvent.getProjectile() instanceof EntityProjectile) {
                                ProjectileLaunchEvent projectev = new ProjectileLaunchEvent(entityShootBowEvent.getProjectile());
                                Server.getInstance().getPluginManager().callEvent(projectev);
                                if (projectev.isCancelled()) {
                                    entityShootBowEvent.getProjectile().kill();
                                } else {
                                    entityShootBowEvent.getProjectile().spawnToAll();
                                    player.level.addSound(new LaunchSound(player), player.getViewers().values());
                                }
                            } else {
                                entityShootBowEvent.getProjectile().spawnToAll();
                            }
                        }
                    }
                }
                //milk removed here, see the section of food
                player.stopAction();
                player.setDataFlag(Player.DATA_FLAGS, Player.DATA_FLAG_ACTION, false);
                break;
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

    public int getSlot() {
        return slot;
    }

    public Item getItem() {
        return item;
    }

    public Vector3f getPlayerPos() {
        return playerPos;
    }

    @Override
    public int getType() {
        return TYPE_ITEM_RELEASE;
    }
}
