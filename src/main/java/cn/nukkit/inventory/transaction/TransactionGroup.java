package cn.nukkit.inventory.transaction;

import cn.nukkit.Player;

import java.util.Set;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public interface TransactionGroup {

    int TYPE_NORMAL = 0;
    int TYPE_ITEM_USE = 1; //This is surely 1
    int TYPE_INVENTORY_MISMATCH = 2; //I do not know this
    int TYPE_ITEM_USE_ON_ENTITY = 3;
    int TYPE_ITEM_RELEASE = 4;

    int ITEM_RELEASE_ACTION_RELEASE = 0; //Drop item, bow shoot
    int ITEM_RELEASE_ACTION_CONSUME = 1; //eat arrows

    //Checked, this values should be like this
    int ITEM_USE_CLICK_BLOCK = 0;
    int ITEM_USE_CLICK_AIR = 1;
    int ITEM_USE_BREAK_BLOCK = 2; //-1

    int SOURCE_CONTAINER = 0;
    int SOURCE_WORLD = 2; //drop/pickup item entity
    int SOURCE_CREATIVE = 3;
    int SOURCE_TODO = 99999;

    int SOURCE_TYPE_CRAFTING_ADD_INGREDIENT = -2;
    int SOURCE_TYPE_CRAFTING_REMOVE_INGREDIENT = -3;
    int SOURCE_TYPE_CRAFTING_RESULT = -4;
    int SOURCE_TYPE_CRAFTING_USE_INGREDIENT = -5;

    int SOURCE_TYPE_ANVIL_INPUT = -10;
    int SOURCE_TYPE_ANVIL_MATERIAL = -11;
    int SOURCE_TYPE_ANVIL_RESULT = -12;
    int SOURCE_TYPE_ANVIL_OUTPUT = -13;

    int SOURCE_TYPE_ENCHANT_INPUT = -15;
    int SOURCE_TYPE_ENCHANT_MATERIAL = -16;
    int SOURCE_TYPE_ENCHANT_OUTPUT = -17;

    int SOURCE_TYPE_TRADING_INPUT_1 = -20;
    int SOURCE_TYPE_TRADING_INPUT_2 = -21;
    int SOURCE_TYPE_TRADING_USE_INPUTS = -22;
    int SOURCE_TYPE_TRADING_OUTPUT = -23;

    int SOURCE_TYPE_BEACON = -24;

    int SOURCE_TYPE_CONTAINER_DROP_CONTENTS = -100;

    int ACTION_MAGIC_SLOT_DROP_ITEM = 0;
    int ACTION_MAGIC_SLOT_PICKUP_ITEM = 1;

    int ACTION_MAGIC_SLOT_CREATIVE_DELETE_ITEM = 0;
    int ACTION_MAGIC_SLOT_CREATIVE_CREATE_ITEM = 1;

    long getCreationTime();

    Set<Transaction> getTransactions();

    void addTransaction(Transaction transaction);

    boolean canExecute(Player player);

    boolean executeOn(Player player);

    boolean executeOn(Player player, boolean force);

    boolean hasExecuted();

    int getType();
}
