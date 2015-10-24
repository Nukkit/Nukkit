package cn.nukkit.item.enchantment;

public class EnchantmentList {
	
	private EnchantmentEntry[] enchantments;
	
	public  EnchantmentList(int size){
		this.enchantments = new EnchantmentEntry[size];
	}
	
	public void setSlot(int slot, EnchantmentEntry entry){
		this.enchantments[slot] = entry;
	}
	
	public EnchantmentEntry getSlot(int slot){
		return this.enchantments[slot];
	}
	
	public int getSize(){
		return this.enchantments.length;
	}
}
