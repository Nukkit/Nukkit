package cn.nukkit.block;

public abstract class BlockMeta extends Block {
    public int meta;

    protected BlockMeta(int meta) {
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
