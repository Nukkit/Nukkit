package cn.nukkit;

import cn.nukkit.network.protocol.AdventureSettingsPacket;

/**
 * Nukkit Project
 * Author: MagicDroidX
 */
public class AdventureSettings implements Cloneable {

    public static final int PERMISSION_NORMAL = 0;
    public static final int PERMISSION_OPERATOR = 1;
    public static final int PERMISSION_HOST = 2;
    public static final int PERMISSION_AUTOMATION = 3;
    public static final int PERMISSION_ADMIN = 4;

    private boolean canDestroyBlock = true;

    private boolean autoJump = true;

    private boolean canFly = false;

    private boolean flying = false;

    private boolean noclip = false;

    private boolean noPvp = false;

    private boolean noPvm = false;

    private boolean noMvp = false;

    private boolean muted = false;

    private Player player;

    private AdventureSettings() {
    }

    public AdventureSettings clone(Player newPlayer) {
        try {
            AdventureSettings settings = (AdventureSettings) super.clone();
            settings.player = newPlayer;
            return settings;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public void setCanDestroyBlock(boolean canDestroyBlock) {
        this.canDestroyBlock = canDestroyBlock;
    }

    public void setAutoJump(boolean autoJump) {
        this.autoJump = autoJump;
    }

    public void setCanFly(boolean canFly) {
        this.canFly = canFly;
    }

    public void setFlying(boolean flying) {
        this.flying = flying;
    }

    public void setNoclip(boolean noclip) {
        this.noclip = noclip;
    }

    public void setNoPvp(boolean noPvp) {
        this.noPvp = noPvp;
    }

    public void setNoPvm(boolean noPvm) {
        this.noPvm = noPvm;
    }

    public void setNoMvp(boolean noMvp) {
        this.noMvp = noMvp;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean canDestroyBlock() {
        return canDestroyBlock;
    }

    public boolean isAutoJumpEnabled() {
        return autoJump;
    }

    public boolean canFly() {
        return canFly;
    }

    public boolean isFlying() {
        return flying;
    }

    public boolean isNoclipEnabled() {
        return noclip;
    }

    public boolean isNoPvp() {
        return noPvp;
    }

    public boolean isNoPvm() {
        return noPvm;
    }

    public boolean isNoMvp() {
        return noMvp;
    }

    public boolean isMuted() {
        return muted;
    }

    public void update() {
        AdventureSettingsPacket pk = new AdventureSettingsPacket();
        pk.setFlag(AdventureSettingsPacket.WORLD_IMMUTABLE, !canDestroyBlock);
        pk.setFlag(AdventureSettingsPacket.AUTO_JUMP, autoJump);
        pk.setFlag(AdventureSettingsPacket.ALLOW_FLIGHT, canFly);
        pk.setFlag(AdventureSettingsPacket.NO_CLIP, noclip);
        pk.setFlag(AdventureSettingsPacket.FLYING, flying);
        pk.setFlag(AdventureSettingsPacket.NO_PVP, noPvp);
        pk.setFlag(AdventureSettingsPacket.MUTED, muted);

        pk.commandPermission = (player.isOp() ? AdventureSettingsPacket.PERMISSION_OPERATOR : AdventureSettingsPacket.PERMISSION_NORMAL);
        pk.playerPermission = (player.isOp() ? Player.PERMISSION_OPERATOR : Player.PERMISSION_MEMBER);
        pk.entityUniqueId = player.getId();

        player.dataPacket(pk);

        player.resetInAirTicks();
    }

    public static class Builder {
        private final AdventureSettings settings = new AdventureSettings();

        public Builder(Player player) {
            if (player == null) {
                throw new IllegalArgumentException("Player can not be null.");
            }

            settings.player = player;
        }

        public Builder canFly(boolean can) {
            settings.canFly = can;
            return this;
        }

        public Builder noclip(boolean noclip) {
            settings.noclip = noclip;
            return this;
        }

        public Builder canDestroyBlock(boolean can) {
            settings.canDestroyBlock = can;
            return this;
        }

        public Builder autoJump(boolean autoJump) {
            settings.autoJump = autoJump;
            return this;
        }

        public AdventureSettings build() {
            return this.settings;
        }
    }
}
