package cn.nukkit.item.enchantment;

import cn.nukkit.event.entity.EntityDamageByEntityEvent;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class EnchantmentKnockback extends Enchantment {
    protected EnchantmentKnockback() {
        super(ID_KNOCKBACK, "knockback", 5, EnchantmentType.WEAPON);
    }

    @Override
    public int getMinEnchantAbility(int level) {
        return 5 + (level - 1) * 20;
    }

    @Override
    public int getMaxEnchantAbility(int level) {
        return this.getMinEnchantAbility(level) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public void apply(EntityDamageByEntityEvent event){
        event.setKnockBack((float) (event.getKnockBack() + (level * 0.5))); //some random calculation
    }
}
