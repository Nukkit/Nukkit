package cn.nukkit.block;

public abstract class BlockSolidMeta extends BlockSolid {
    public int meta;

    protected BlockSolidMeta(int meta) {
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
