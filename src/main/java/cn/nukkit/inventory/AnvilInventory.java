package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class AnvilInventory extends ContainerInventory {

    public static final int TARGET = 0;
    public static final int SACRIFICE = 1;
    public static final int RESULT = 2;

    public AnvilInventory(Position position) {
        super(null, InventoryType.ANVIL);
        this.holder = new FakeBlockMenu(this, position);
    }

    @Override
    public FakeBlockMenu getHolder() {
        return (FakeBlockMenu) this.holder;
    }

    public boolean onRename(Player player, Item resultItem) {
        Item local = getItem(TARGET);
        Item second = getItem(SACRIFICE);

        if(!resultItem.deepEquals(local, true, false) || resultItem.getCount() != local.getCount()){
            //Item does not match target item. Everything must match except the tags.
            return false;
        }

        if(local.getId() != 0 && second.getId() == 0){ //only rename
            local.setCustomName(resultItem.getCustomName());
            setItem(RESULT, local);
            player.getInventory().addItem(local);
            clearAll();
            player.getInventory().sendContents(player);
            sendContents(player);
            return true;
        } else if(local.getId() != 0 && second.getId() != 0){
            //TODO: unit enchants
        }

        return false;
    }

    @Override
    public void onClose(Player who) {
        super.onClose(who);
        who.craftingType = 0;

        for (int i = 0; i < 2; ++i) {
            this.getHolder().getLevel().dropItem(this.getHolder().add(0.5, 0.5, 0.5), this.getItem(i));
            this.clear(i);
        }
    }

    @Override
    public void onOpen(Player who) {
        super.onOpen(who);
        who.craftingType = 3;
    }
}
