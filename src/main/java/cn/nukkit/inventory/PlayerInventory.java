package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.BlockAir;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.EntityHumanType;
import cn.nukkit.event.entity.EntityArmorChangeEvent;
import cn.nukkit.event.entity.EntityInventoryChangeEvent;
import cn.nukkit.event.player.PlayerItemHeldEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.network.protocol.*;
import cn.nukkit.network.protocol.types.ContainerIds;

import java.util.Collection;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class PlayerInventory extends BaseInventory {

    protected int itemInHandIndex = 0;

    protected final int[] hotbar;

    public PlayerInventory(EntityHumanType player) {
        super(player, InventoryType.PLAYER);
        this.hotbar = new int[this.getHotbarSize()];
        for (int i = 0; i < this.hotbar.length; i++) {
            this.hotbar[i] = i;
        }
    }

    @Override
    public int getSize() {
        return super.getSize() - 4;
    }

    @Override
    public void setSize(int size) {
        super.setSize(size + 4);
        this.sendContents(this.getViewers());
    }

    public boolean equipItem(int hotbarSlot) {
        return equipItem(hotbarSlot, null);
    }

    /**
     * Called when a client equips a hotbar inventorySlot. This method should not be used by plugins.
     * This method will call PlayerItemHeldEvent.
     *
     * @param hotbarSlot    Number of the hotbar inventorySlot to equip.
     * @param inventorySlot Inventory inventorySlot to map to the specified hotbar inventorySlot. Supply null to make no change to the link.
     * @return bool if the equipment change was successful, false if not.
     */
    public boolean equipItem(int hotbarSlot, Integer inventorySlot) {
        if (inventorySlot == null) {
            inventorySlot = this.getHotbarSlotIndex(hotbarSlot);
        }

        if (hotbarSlot < 0 || hotbarSlot >= this.getHotbarSize() || inventorySlot < -1 || inventorySlot >= this.getSize()) {
            this.sendContents(this.getViewers());
            return false;
        }

        Item item;

        if (inventorySlot == -1) {
            item = Item.get(Item.AIR, 0, 0);
        } else {
            item = this.getItem(inventorySlot);
        }

        if (this.getHolder() instanceof Player) {
            PlayerItemHeldEvent ev = new PlayerItemHeldEvent((Player) this.getHolder(), item, inventorySlot, hotbarSlot);
            this.getHolder().getLevel().getServer().getPluginManager().callEvent(ev);

            if (ev.isCancelled()) {
                this.sendContents(this.getViewers());
                return false;
            }
        }

        this.setHotbarSlotIndex(hotbarSlot, inventorySlot);
        this.setHeldItemIndex(hotbarSlot, false);

        return true;
    }

    public int getHotbarSlotIndex(int index) {
        return (index >= 0 && index < this.getHotbarSize()) ? this.hotbar[index] : -1;
    }

    public void setHotbarSlotIndex(int index, int slot) { //all slots are now linked to its ID
        /*if (index >= 0 && index < this.getHotbarSize() && slot >= -1 && slot < this.getSize()) {
            this.hotbar[index] = slot;
        }*/
    }

    public int getHeldItemIndex() {
        return itemInHandIndex;
    }

    public void setHeldItemIndex(int index) {
        setHeldItemIndex(index, true);
    }

    public void setHeldItemIndex(int index, boolean send) {
        if (index >= 0 && index < this.getHotbarSize()) {
            this.itemInHandIndex = index;

            if (this.getHolder() instanceof Player && send) {
                this.sendHeldItem((Player) this.getHolder());
            }

            this.sendHeldItem(this.getHolder().getViewers().values());
        }
    }

    public Item getItemInHand() {
        Item item = this.getItem(this.getHeldItemSlot());
        if (item != null) {
            return item;
        } else {
            return new ItemBlock(new BlockAir(), 0, 0);
        }
    }

    public boolean setItemInHand(Item item) {
        return this.setItem(this.getHeldItemSlot(), item);
    }

    public int getHeldItemSlot() {
        return this.getHotbarSlotIndex(this.itemInHandIndex);
    }

    public void setHeldItemSlot(int slot) {
        if (slot >= -1 && slot < this.getSize()) {
            Item item = this.getItem(slot);

            int itemIndex = this.getHeldItemIndex();

            if (this.getHolder() instanceof Player) {
                PlayerItemHeldEvent ev = new PlayerItemHeldEvent((Player) this.getHolder(), item, slot, itemIndex);
                Server.getInstance().getPluginManager().callEvent(ev);
                if (ev.isCancelled()) {
                    this.sendHotbarContents();
                    return;
                }
            }

            this.setHotbarSlotIndex(itemIndex, slot);
        }
    }

    public void sendHeldItem(Player player) {
        Item item = this.getItemInHand();

        MobEquipmentPacket pk = new MobEquipmentPacket();
        pk.eid = this.getHolder().getId();
        pk.item = item;
        pk.inventorySlot = (byte) this.getHeldItemSlot();
        pk.hotbarSlot = (byte) this.getHeldItemIndex();

        player.dataPacket(pk);
        if (player.equals(this.getHolder())) {
            this.sendSlot(this.getHeldItemSlot(), player);
        }
    }

    public void sendHeldItem(Player[] players) {
        Item item = this.getItemInHand();

        MobEquipmentPacket pk = new MobEquipmentPacket();
        pk.item = item;
        pk.inventorySlot = (byte) this.getHeldItemSlot();
        pk.hotbarSlot = (byte) this.getHeldItemIndex();

        for (Player player : players) {
            pk.eid = this.getHolder().getId();
            if (player.equals(this.getHolder())) {
                pk.eid = player.getId();
                this.sendSlot(this.getHeldItemSlot(), player);
            }

            player.dataPacket(pk);
        }
    }

    public void sendHeldItem(Collection<Player> players) {
        this.sendHeldItem(players.stream().toArray(Player[]::new));
    }

    @Override
    public void onSlotChange(int index, Item before, boolean send) {
        EntityHuman holder = this.getHolder();
        if (holder instanceof Player && !((Player) holder).spawned) {
            return;
        }

        if (index >= this.getSize()) {
            this.sendArmorSlot(index, this.getViewers());
            this.sendArmorSlot(index, this.getHolder().getViewers().values());
        } else {
            super.onSlotChange(index, before, send);
        }
    }

    public int getHotbarSize() {
        return 9;
    }

    public Item getArmorItem(int index) {
        return this.getItem(this.getSize() + index);
    }

    public boolean setArmorItem(int index, Item item) {
        return this.setArmorItem(index, item, false);
    }

    public boolean setArmorItem(int index, Item item, boolean ignoreArmorEvents) {
        return this.setItem(this.getSize() + index, item, ignoreArmorEvents);
    }

    public Item getHelmet() {
        return this.getItem(this.getSize());
    }

    public Item getChestplate() {
        return this.getItem(this.getSize() + 1);
    }

    public Item getLeggings() {
        return this.getItem(this.getSize() + 2);
    }

    public Item getBoots() {
        return this.getItem(this.getSize() + 3);
    }

    public boolean setHelmet(Item helmet) {
        return this.setItem(this.getSize(), helmet);
    }

    public boolean setChestplate(Item chestplate) {
        return this.setItem(this.getSize() + 1, chestplate);
    }

    public boolean setLeggings(Item leggings) {
        return this.setItem(this.getSize() + 2, leggings);
    }

    public boolean setBoots(Item boots) {
        return this.setItem(this.getSize() + 3, boots);
    }

    @Override
    public boolean setItem(int index, Item item) {
        return setItem(index, item, true, false);
    }

    private boolean setItem(int index, Item item, boolean send, boolean ignoreArmorEvents) {
        if (index < 0 || index >= this.size) {
            return false;
        } else if (item.getId() == 0 || item.getCount() <= 0) {
            return this.clear(index);
        }

        //Armor change
        if (!ignoreArmorEvents && index >= this.getSize()) {
            EntityArmorChangeEvent ev = new EntityArmorChangeEvent(this.getHolder(), this.getItem(index), item, index);
            Server.getInstance().getPluginManager().callEvent(ev);
            if (ev.isCancelled() && this.getHolder() != null) {
                this.sendArmorSlot(index, this.getViewers());
                return false;
            }
            item = ev.getNewItem();
        } else {
            EntityInventoryChangeEvent ev = new EntityInventoryChangeEvent(this.getHolder(), this.getItem(index), item, index);
            Server.getInstance().getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                this.sendSlot(index, this.getViewers());
                return false;
            }
            item = ev.getNewItem();
        }
        Item old = this.getItem(index);
        this.slots.put(index, item.clone());
        this.onSlotChange(index, old, send);
        return true;
    }

    @Override
    public boolean clear(int index, boolean send) {
        if (this.slots.containsKey(index)) {
            Item item = new ItemBlock(new BlockAir(), null, 0);
            Item old = this.slots.get(index);
            if (index >= this.getSize() && index < this.size) {
                EntityArmorChangeEvent ev = new EntityArmorChangeEvent(this.getHolder(), old, item, index);
                Server.getInstance().getPluginManager().callEvent(ev);
                if (ev.isCancelled()) {
                    if (index >= this.size) {
                        this.sendArmorSlot(index, this.getViewers());
                    } else {
                        this.sendSlot(index, this.getViewers());
                    }
                    return false;
                }
                item = ev.getNewItem();
            } else {
                EntityInventoryChangeEvent ev = new EntityInventoryChangeEvent(this.getHolder(), old, item, index);
                Server.getInstance().getPluginManager().callEvent(ev);
                if (ev.isCancelled()) {
                    if (index >= this.size) {
                        this.sendArmorSlot(index, this.getViewers());
                    } else {
                        this.sendSlot(index, this.getViewers());
                    }
                    return false;
                }
                item = ev.getNewItem();
            }

            if (item.getId() != Item.AIR) {
                this.slots.put(index, item.clone());
            } else {
                this.slots.remove(index);
            }

            this.onSlotChange(index, old, send);
        }

        return true;
    }

    public Item[] getArmorContents() {
        Item[] armor = new Item[4];
        for (int i = 0; i < 4; i++) {
            armor[i] = this.getItem(this.getSize() + i);
        }

        return armor;
    }

    @Override
    public void clearAll() {
        int limit = this.getSize() + 4;
        for (int index = 0; index < limit; ++index) {
            this.clear(index);
        }
    }

    public void sendArmorContents(Player player) {
        this.sendArmorContents(new Player[]{player});
    }

    public void sendArmorContents(Player[] players) {
        Item[] armor = this.getArmorContents();

        MobArmorEquipmentPacket pk = new MobArmorEquipmentPacket();
        pk.eid = this.getHolder().getId();
        pk.slots = armor;
        pk.encode();
        pk.isEncoded = true;

        for (Player player : players) {
            if (player.equals(this.getHolder())) {
                InventoryContentPacket pk2 = new InventoryContentPacket();
                pk2.inventoryId = InventoryContentPacket.SPECIAL_ARMOR;
                pk2.slots = armor;
                player.dataPacket(pk2);
            } else {
                player.dataPacket(pk);
            }
        }
    }

    public void setArmorContents(Item[] items) {
        if (items.length < 4) {
            Item[] newItems = new Item[4];
            System.arraycopy(items, 0, newItems, 0, items.length);
            items = newItems;
        }

        for (int i = 0; i < 4; ++i) {
            if (items[i] == null) {
                items[i] = new ItemBlock(new BlockAir(), null, 0);
            }

            if (items[i].getId() == Item.AIR) {
                this.clear(this.getSize() + i);
            } else {
                this.setItem(this.getSize() + i, items[i]);
            }
        }
    }

    public void sendArmorContents(Collection<Player> players) {
        this.sendArmorContents(players.stream().toArray(Player[]::new));
    }

    public void sendArmorSlot(int index, Player player) {
        this.sendArmorSlot(index, new Player[]{player});
    }

    public void sendArmorSlot(int index, Player[] players) {
        Item[] armor = this.getArmorContents();

        MobArmorEquipmentPacket pk = new MobArmorEquipmentPacket();
        pk.eid = this.getHolder().getId();
        pk.slots = armor;
        pk.encode();
        pk.isEncoded = true;

        for (Player player : players) {
            if (player.equals(this.getHolder())) {
                InventorySlotPacket pk2 = new InventorySlotPacket();
                pk2.inventoryId = InventoryContentPacket.SPECIAL_ARMOR;
                pk2.slot = index - this.getSize();
                pk2.item = this.getItem(index);
                player.dataPacket(pk2);
            } else {
                player.dataPacket(pk);
            }
        }
    }

    public void sendArmorSlot(int index, Collection<Player> players) {
        this.sendArmorSlot(index, players.stream().toArray(Player[]::new));
    }

    @Override
    public void sendContents(Player player) {
        this.sendContents(new Player[]{player});
    }

    @Override
    public void sendContents(Collection<Player> players) {
        this.sendContents(players.stream().toArray(Player[]::new));
    }

    @Override
    public void sendContents(Player[] players) {
        InventoryContentPacket pk = new InventoryContentPacket();
        pk.slots = new Item[this.getSize()];
        for (int i = 0; i < this.getSize(); ++i) {
            pk.slots[i] = this.getItem(i);
        }

        /*//Because PE is stupid and shows 9 less slots than you send it, give it 9 dummy slots so it shows all the REAL slots.
        for(int i = this.getSize(); i < this.getSize() + this.getHotbarSize(); ++i){
            pk.slots[i] = new ItemBlock(new BlockAir());
        }
            pk.slots[i] = new ItemBlock(new BlockAir());
        }*/

        for (Player player : players) {
            int id = player.getWindowId(this);
            if (id == -1 || !player.spawned) {
                this.close(player);
                continue;
            }
            pk.inventoryId = id;
            player.dataPacket(pk.clone());

            if (player.getId() == this.getHolder().getId()) {
                this.sendHotbarContents();
            }
        }
    }

    public void sendHotbarContents() {
        if (this.getHolder() instanceof Player) {
            PlayerHotbarPacket pk = new PlayerHotbarPacket();
            pk.windowId = ContainerIds.INVENTORY;
            pk.selectedHotbarSlot = this.getHeldItemIndex();
            pk.slots = new int[this.getHotbarSize()];
            //pk.slots = this.hotbar;

            System.arraycopy(this.hotbar, 0, pk.slots, 0, pk.slots.length);

            for (int i = 0; i < pk.slots.length; i++) {
                pk.slots[i] = pk.slots[i] + 9;
            }

            ((Player) holder).dataPacket(pk);

        }
    }

    @Override
    public void sendSlot(int index, Player player) {
        this.sendSlot(index, new Player[]{player});
    }

    @Override
    public void sendSlot(int index, Collection<Player> players) {
        this.sendSlot(index, players.stream().toArray(Player[]::new));
    }

    @Override
    public void sendSlot(int index, Player... players) {
        InventorySlotPacket pk = new InventorySlotPacket();
        pk.slot = index;
        pk.item = this.getItem(index).clone();

        for (Player player : players) {
            if (player.equals(this.getHolder())) {
                pk.inventoryId = ContainerIds.INVENTORY;
                player.dataPacket(pk);

                if (index >= 0 && index <= 9) { //send hotbar always
                    this.sendHotbarContents();
                }
            } else {
                int id = player.getWindowId(this);
                if (id == -1) {
                    this.close(player);
                    continue;
                }
                pk.inventoryId = id;
                player.dataPacket(pk.clone());
            }
        }
    }

    public void sendCreativeContents() {
        if (!(this.getHolder() instanceof Player)) {
            return;
        }
        Player p = (Player) this.getHolder();

        InventoryContentPacket pk = new InventoryContentPacket();
        pk.inventoryId = ContainerIds.CREATIVE;

        if (!p.isSpectator()) { //fill it for all gamemodes except spectator
            pk.slots = Item.getCreativeItems().stream().toArray(Item[]::new);
        }

        p.dataPacket(pk);
    }

    @Override
    public EntityHuman getHolder() {
        return (EntityHuman) super.getHolder();
    }
}
