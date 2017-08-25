package cn.nukkit.network.protocol;

/**
 * @author Nukkit Project Team
 */
public class AdventureSettingsPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.ADVENTURE_SETTINGS_PACKET;

    public static final int ACTION_FLAG_PROHIBIT_ALL = 0;
    public static final int ACTION_FLAG_BUILD_AND_MINE = 1;
    public static final int ACTION_FLAG_DOORS_AND_SWITCHES = 2;
    public static final int ACTION_FLAG_OPEN_CONTAINERS = 4;
    public static final int ACTION_FLAG_ATTACK_PLAYERS = 8;
    public static final int ACTION_FLAG_ATTACK_MOBS = 16;
    public static final int ACTION_FLAG_OP = 32;
    public static final int ACTION_FLAG_TELEPORT = 64;
    public static final int ACTION_FLAG_DEFAULT_LEVEL_PERMISSIONS = 128;
    public static final int ACTION_FLAG_ALLOW_ALL = 511;

    public static final int PERMISSION_LEVEL_VISITOR = 0;
    public static final int PERMISSION_LEVEL_MEMBER = 1;
    public static final int PERMISSION_LEVEL_OPERATOR = 2;
    public static final int PERMISSION_LEVEL_CUSTOM = 3;

    public boolean worldImmutable;
    public boolean noPvp;
    public boolean noPvm;
    public boolean noMvp;

    public boolean autoJump;
    public boolean allowFlight;
    public boolean noClip;
    public boolean worldBuilder;
    public boolean isFlying;
    public boolean muted;

    /*
     bit mask | flag name
	0x00000001 world_immutable
	0x00000002 no_pvp
	0x00000004 no_pvm
	0x00000008 no_mvp
	0x00000010 ?
	0x00000020 auto_jump
	0x00000040 allow_fly
	0x00000080 noclip
	0x00000100 ?
	0x00000200 is_flying
	*/

    public int flags = 0;
    public int userPermission;
    public int actionPermissions = ACTION_FLAG_DEFAULT_LEVEL_PERMISSIONS;
	public int permissionLevel = PERMISSION_LEVEL_MEMBER;
	public long userId = 0;

    @Override
    public void decode() {
        this.flags = (int) this.getUnsignedVarInt();
        this.userPermission = (int) this.getUnsignedVarInt();
        this.worldImmutable = (this.flags & 1) != 0;
        this.noPvp = (this.flags & (1 << 1)) != 0;
        this.noPvm = (this.flags & (1 << 2)) != 0;
        this.noMvp = (this.flags & (1 << 3)) != 0;

        this.autoJump = (this.flags & (1 << 5)) != 0;
        this.allowFlight = (this.flags & (1 << 6)) != 0;
        this.noClip = (this.flags & (1 << 7)) != 0;
        this.worldBuilder = (this.flags & (1 << 8)) != 0;
        this.isFlying = (this.flags & (1 << 9)) != 0;
        this.muted = (this.flags & (1 << 10)) != 0;
    }

    @Override
    public void encode() {
        this.reset();
        if (this.worldImmutable) this.flags |= 1;
        if (this.noPvp) this.flags |= 1 << 1;
        if (this.noPvm) this.flags |= 1 << 2;
        if (this.noMvp) this.flags |= 1 << 3;

        if (this.autoJump) this.flags |= 1 << 5;
        if (this.allowFlight) this.flags |= 1 << 6;
        if (this.noClip) this.flags |= 1 << 7;
        if (this.worldBuilder) this.flags |= 1 << 8;
        if (this.isFlying) this.flags |= 1 << 9;
        if (this.muted) this.flags |= 1 << 10;
        this.putUnsignedVarInt(this.flags);
        this.putUnsignedVarInt(this.userPermission);
        this.putUnsignedVarInt(this.actionPermissions);
        this.putUnsignedVarInt(this.permissionLevel);
        if ((this.userId & 0x01) != 0) {
            this.putLLong(-1 * ((this.userId + 1) >> 1));
        } else {
            this.putLLong(this.userId >> 1);
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
