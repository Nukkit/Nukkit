package cn.nukkit.item.enchantment;

public class EnchantmentEntry {
	
	private Enchantment[] enchantments;
	private long cost;
	private String randomName;
	
	public EnchantmentEntry(Enchantment[] enchantments, long cost, String randomName){
		this.enchantments = enchantments;
		this.cost = cost;
		this.randomName = randomName;
	}
	
	public Enchantment[] getEnchantments(){
		return this.enchantments;
	}
	
	public long getCost(){
		return this.cost;
	}
	
	public String getRandomName(){
		return this.randomName;
	}
}
