package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;

/**
 * @author Nukkit Project Team
 */
public class ContainerSetContentPacket extends DataPacket {

    public static final byte NETWORK_ID = Info.CONTAINER_SET_CONTENT_PACKET;

    public static final byte SPECIAL_INVENTORY = 0;

    public static final byte SPECIAL_ARMOR = 0x78;
    public static final byte SPECIAL_CREATIVE = 0x79;
    public static final byte SPECIAL_CRAFTING = 0x7a;

    public Item[] slots  = new Item[0];
    public int[]  hotBar = new int[0];
    public byte   windowId;

    @Override
    public void clean() {
        slots  = new Item[0];
        hotBar = new int [0];
        super.clean();
    }

    @Override
    public void decode() {
        windowId  = getByte();
        int count = getShort();
        slots = new Item[count];

        for (int s = 0; s < count && hasRemain(); ++s) {
            slots[s] = getItem();
        }

        if (windowId == SPECIAL_INVENTORY) {
            count = getShort();
            hotBar = new int[count];
            for (int s = 0; s < count && hasRemain(); ++s) {
                hotBar[s] = getInt();
            }
        }
    }

    @Override
    public void encode() {
        putByte(windowId);
        putShort((short) slots.length);
        for (Item slot : slots) {
            putItem(slot);
        }

        if (windowId == SPECIAL_INVENTORY && hotBar.length > 0) {
            putShort((short) hotBar.length);
            for (int slot : hotBar) {
                putInt(slot);
            }
        } else {
            putShort((short) 0);
        }
    }

    @Override
    public ContainerSetContentPacket clone() {
        ContainerSetContentPacket pk = (ContainerSetContentPacket) super.clone();
        pk.slots = slots.clone();
        return pk;
    }

    @Override
    public byte getNetworkId() {
        return NETWORK_ID;
    }

}
