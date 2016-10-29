package cn.nukkit.block;

public abstract class BlockTransparentMeta extends BlockTransparent {
    public int meta;

    protected BlockTransparentMeta(int meta) {
        this.meta = meta;
    }

    @Override
    public final int getDamage() {
        return this.meta;
    }

    @Override
    public final void setDamage(Integer meta) {
        this.meta = (meta == null ? 0 : meta & 0x0f);
    }
}
