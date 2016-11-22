package cn.nukkit.network.protocol;

/**
 * author: MagicDroidX & iNevet
 * Nukkit Project
 */
public interface ProtocolInfo {

    /**
     * Actual Minecraft: PE protocol version
     */
    byte CURRENT_PROTOCOL = 91;

    byte LOGIN_PACKET = 0x01;
    byte PLAY_STATUS_PACKET = 0x02;
    byte SERVER_TO_CLIENT_HANDSHAKE_PACKET = 0x03;
    byte CLIENT_TO_SERVER_HANDSHAKE_PACKET = 0x04;
    byte DISCONNECT_PACKET = 0x05;
    byte BATCH_PACKET = 0x06;
    byte RESOURCE_PACKS_INFO_PACKET = 0x07;
    byte RESOURCE_PACK_STACK_PACKET = 0x08;
    byte RESOURCE_PACK_CLIENT_RESPONSE_PACKET = 0x09;
    byte TEXT_PACKET = 0x0a;
    byte SET_TIME_PACKET = 0x0b;
    byte START_GAME_PACKET = 0x0c;
    byte ADD_PLAYER_PACKET = 0x0d;
    byte ADD_ENTITY_PACKET = 0x0e;
    byte REMOVE_ENTITY_PACKET = 0x0f;
    byte ADD_ITEM_ENTITY_PACKET = 0x10;
    byte ADD_HANGING_ENTITY_PACKET = 0x11;
    byte TAKE_ITEM_ENTITY_PACKET = 0x12;
    byte MOVE_ENTITY_PACKET = 0x13;
    byte MOVE_PLAYER_PACKET = 0x14;
    byte RIDER_JUMP_PACKET = 0x15;
    byte REMOVE_BLOCK_PACKET = 0x16;
    byte UPDATE_BLOCK_PACKET = 0x17;
    byte ADD_PAINTING_PACKET = 0x18;
    byte EXPLODE_PACKET = 0x19;
    byte LEVEL_SOUND_EVENT_PACKET = 0x1a;
    byte LEVEL_EVENT_PACKET = 0x1b;
    byte BLOCK_EVENT_PACKET = 0x1c;
    byte ENTITY_EVENT_PACKET = 0x1d;
    byte MOB_EFFECT_PACKET = 0x1e;
    byte UPDATE_ATTRIBUTES_PACKET = 0x1f;
    byte MOB_EQUIPMENT_PACKET = 0x20;
    byte MOB_ARMOR_EQUIPMENT_PACKET = 0x21;
    byte INTERACT_PACKET = 0x22;
    byte USE_ITEM_PACKET = 0x23;
    byte PLAYER_ACTION_PACKET = 0x24;
    byte HURT_ARMOR_PACKET = 0x25;
    byte SET_ENTITY_DATA_PACKET = 0x26;
    byte SET_ENTITY_MOTION_PACKET = 0x27;
    byte SET_ENTITY_LINK_PACKET = 0x28;
    byte SET_HEALTH_PACKET = 0x29;
    byte SET_SPAWN_POSITION_PACKET = 0x2a;
    byte ANIMATE_PACKET = 0x2b;
    byte RESPAWN_PACKET = 0x2c;
    byte DROP_ITEM_PACKET = 0x2d;
    byte INVENTORY_ACTION_PACKET = 0x2e;
    byte CONTAINER_OPEN_PACKET = 0x2f;
    byte CONTAINER_CLOSE_PACKET = 0x30;
    byte CONTAINER_SET_SLOT_PACKET = 0x31;
    byte CONTAINER_SET_DATA_PACKET = 0x32;
    byte CONTAINER_SET_CONTENT_PACKET = 0x33;
    byte CRAFTING_DATA_PACKET = 0x34;
    byte CRAFTING_EVENT_PACKET = 0x35;
    byte ADVENTURE_SETTINGS_PACKET = 0x36;
    byte BLOCK_ENTITY_DATA_PACKET = 0x37;
    byte PLAYER_INPUT_PACKET = 0x38;
    byte FULL_CHUNK_DATA_PACKET = 0x39;
    byte SET_COMMANDS_ENABLED_PACKET = 0x3a;
    byte SET_DIFFICULTY_PACKET = 0x3b;
    byte CHANGE_DIMENSION_PACKET = 0x3c;
    byte SET_PLAYER_GAME_TYPE_PACKET = 0x3d;
    byte PLAYER_LIST_PACKET = 0x3e;
    byte EVENT_PACKET = 0x3f;
    byte SPAWN_EXPERIENCE_ORB_PACKET = 0x40;
    byte CLIENTBOUND_MAP_ITEM_DATA_PACKET = 0x41;
    byte MAP_INFO_REQUEST_PACKET = 0x42;
    byte REQUEST_CHUNK_RADIUS_PACKET = 0x43;
    byte CHUNK_RADIUS_UPDATED_PACKET = 0x44;
    byte ITEM_FRAME_DROP_ITEM_PACKET = 0x45;
    byte REPLACE_SELECTED_ITEM_PACKET = 0x46;
    byte GAME_RULES_CHANGED_PACKET = 0x47;
    byte CAMERA_PACKET = 0x48;
    byte ADD_ITEM_PACKET = 0x49;
    byte BOSS_EVENT_PACKET = 0x4a;
    byte AVAILABLE_COMMANDS_PACKET = 0x4c;
    byte COMMAND_STEP_PACKET = 0x4d;
    byte RESOURCE_PACK_DATA_INFO_PACKET = 0x4e;
    byte RESOURCE_PACK_CHUNK_DATA_PACKET = 0x4f;
    byte RESOURCE_PACK_CHUNK_REQUEST_PACKET = 0x50;
}
