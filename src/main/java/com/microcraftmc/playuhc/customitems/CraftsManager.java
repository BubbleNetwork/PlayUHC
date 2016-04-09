package com.microcraftmc.playuhc.customitems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.microcraftmc.playuhc.BubbleUHC;
import com.microcraftmc.playuhc.languages.Lang;

/**
 * Copyright Statement
 * ----------------------
 * Copyright (C) Microcraft MC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Class information
 * ---------------------
 * Package: com.microcraftmc.BubbleUHC
 * Project: BubbleUHC
 *
 */

public class CraftsManager {
	
	private static List<Craft> crafts;
	private static List<ItemStack> bannedCrafts;
	
	public static synchronized List<Craft> getCrafts(){
		return crafts;
	}

	public static boolean isAtLeastOneCraft() {
		return (getCrafts() != null && getCrafts().size() >= 1);
	}
	
	public static boolean isBannedCraft(ItemStack item) {
		for(ItemStack stack : bannedCrafts){
			if(stack.getType().equals(item.getType()) && stack.getData().equals(item.getData()))
				return true;
		}
		return false;
	}
	
	public static void loadBannedCrafts(){
		Bukkit.getLogger().info("[BubbleUHC] Loading banned crafts list");
		bannedCrafts = Collections.synchronizedList(new ArrayList<ItemStack>());
		
		FileConfiguration cfg = BubbleUHC.getInstance().getPlugin().getConfig();
		for(String itemLine : cfg.getStringList("customize-game-behavior.ban-items-crafts")){
			
			String[] itemData = itemLine.split("/");
			try{
				if(itemData.length !=2){
					throw new IllegalArgumentException("Couldn't parse "+itemLine+" : Each banned craft should be formatted like ITEM/DATA");
				}else{
					bannedCrafts.add(new ItemStack(Material.valueOf(itemData[0]), 1, Short.parseShort(itemData[1])));
					Bukkit.getLogger().info("[BubbleUHC] Banned item "+itemLine+" registered");
				}
			}catch(IllegalArgumentException e){
				Bukkit.getLogger().warning("[BubbleUHC] Failed to register "+itemLine+" banned craft");
				Bukkit.getLogger().warning(e.getMessage());
			}
		}
		
	}
	
	public static void loadCrafts(){
		Bukkit.getLogger().info("[BubbleUHC] Loading custom crafts");
		crafts = Collections.synchronizedList(new ArrayList<Craft>());
		FileConfiguration cfg = BubbleUHC.getInstance().getPlugin().getConfig();
		Set<String> craftsKeys = cfg.getConfigurationSection("customize-game-behavior.add-custom-crafts").getKeys(false);
		for(String craftKey : craftsKeys){
			ConfigurationSection section = cfg.getConfigurationSection("customize-game-behavior.add-custom-crafts."+craftKey);
			
			List<ItemStack> recipe = new ArrayList<ItemStack>();
			ItemStack craft;
			String name = craftKey;
			int limit;
			
			try{

				Bukkit.getLogger().info("[BubbleUHC] Loading custom craft "+name);
				
				// Recipe
				String[] lines = new String[3];
				lines[0] = section.getString("1", "");
				lines[1] = section.getString("2", "");
				lines[2] = section.getString("3", "");
				
				for(int i=0 ; i<3; i++){
					String[] itemsInLine = lines[i].split(" ");
					if(itemsInLine.length != 3)
						throw new IllegalArgumentException("Each line should be formatted like ITEM/QUANTITY/DATA ITEM/QUANTITY/DATA ITEM/QUANTITY/DATA");
					for(int j=0 ; j<3 ;j++){
						String[] itemData = itemsInLine[j].split("/");
						if(itemData.length !=3)
							throw new IllegalArgumentException("Each item should be formatted like ITEM/QUANTITY/DATA");
						recipe.add(new ItemStack(Material.valueOf(itemData[0]), Integer.parseInt(itemData[1]), Short.parseShort(itemData[2])));
					}
				}
				
				// Craft
				String craftString = section.getString("craft","");
				String[] craftData = craftString.split("/");
				if(craftData.length != 3)
					throw new IllegalArgumentException("The craft result must be formatted like ITEM/QUANTITY/DATA");
				craft = new ItemStack(Material.valueOf(craftData[0]), Integer.parseInt(craftData[1]), Short.parseShort(craftData[2]));
				

				List<String> enchStringList = section.getStringList("enchants");
				ItemMeta im = craft.getItemMeta();
				for(String enchString : enchStringList){
					String[] enchData = enchString.split(" ");
					Enchantment ench = Enchantment.getByName(enchData[0]);
					if(ench != null){
						int level = 1;
						if(enchData.length > 1){
							level = Integer.parseInt(enchData[1]);
						}
						if(craft.getType().equals(Material.ENCHANTED_BOOK)){
							
							((EnchantmentStorageMeta) im).addStoredEnchant(ench, level, true);
						}else{
							craft.addUnsafeEnchantment(ench, level);
						}
					}
				}
				
				// Limit
				limit = section.getInt("limit",-1);
				getCrafts().add(new Craft(name, recipe, craft, limit));
			}catch(IllegalArgumentException e){
				//ignore craft if bad formatting
				Bukkit.getLogger().warning("[BubbleUHC] Failed to register "+name+" custom craft : syntax error");
				Bukkit.getLogger().warning(e.getMessage());
			}
			
		}
	}

	public static Craft getCraft(ItemStack result) {
		String displayName = result.getItemMeta().getDisplayName();
		if(displayName != null){
			for(Craft craft : getCrafts()){
				if(displayName.equals(craft.getCraft().getItemMeta().getDisplayName())){
					return craft;
				}
			}
		}
		return null;
	}
	
	public static Craft getCraftByName(String craftName) {
		for(Craft craft : getCrafts()){
			if(craft.getName().equals(craftName)){
				return craft;
			}
		}
		return null;
	}
	
	public static Craft getCraftByDisplayName(String craftName) {
		for(Craft craft : getCrafts()){
			if(craft.getCraft().getItemMeta().getDisplayName().equals(craftName)){
				return craft;
			}
		}
		return null;
	}
	
	public static void openCraftBookInventory(Player player){
		int maxSlots = 6*9;
		Inventory inv = Bukkit.createInventory(null, maxSlots, ChatColor.GREEN+Lang.DISPLAY_MESSAGE_PREFIX+" "+ChatColor.DARK_GREEN+Lang.ITEMS_CRAFT_BOOK_INVENTORY);
		int slot = 0;
		for(Craft craft : getCrafts()){
			if(slot < maxSlots){
				inv.setItem(slot, craft.getCraft());
				slot++;
			}
		}
		
		player.openInventory(inv);
	}

	public static boolean isCraftItem(ItemStack item) {
		if(item == null || item.getType().equals(Material.AIR))
			return false;


		String name = item.getItemMeta().getDisplayName();
		if(name != null){
			for(Craft craft : getCrafts()){
				 if(name.equals(craft.getCraft().getItemMeta().getDisplayName())
				   && item.getType().equals(craft.getCraft().getType()))
					return true;
			}
		}
		return false;
	}
	
	public static boolean isCraftBookBackItem(ItemStack item) {
		if(item == null || item.getType().equals(Material.AIR))
			return false;

		if(item.getType().equals(Material.RAW_FISH) && item.getDurability() == 3 && item.getItemMeta().getDisplayName().equals(ChatColor.GRAY+Lang.ITEMS_CRAFT_BOOK_BACK))
			return true;
		return false;
	}

	public static void openCraftInventory(Player player, Craft craft) {
		int maxSlots = 6*9;
		Inventory inv = Bukkit.createInventory(null, maxSlots, ChatColor.GREEN+Lang.DISPLAY_MESSAGE_PREFIX+" "+ChatColor.DARK_GREEN+Lang.ITEMS_CRAFT_BOOK_INVENTORY);

		for(int i = 0 ; i < maxSlots-9 ; i++){
			inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
		}
		
		for(int i = maxSlots-9 ; i < maxSlots ; i++){
			inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0));
		}
		
		// Recipe
		inv.setItem(11, craft.getRecipe().get(0));
		inv.setItem(12, craft.getRecipe().get(1));
		inv.setItem(13, craft.getRecipe().get(2));
		inv.setItem(20, craft.getRecipe().get(3));
		inv.setItem(21, craft.getRecipe().get(4));
		inv.setItem(22, craft.getRecipe().get(5));
		inv.setItem(29, craft.getRecipe().get(6));
		inv.setItem(30, craft.getRecipe().get(7));
		inv.setItem(31, craft.getRecipe().get(8));
		
		// Craft
		inv.setItem(24, craft.getCraft());		

		// Back
		ItemStack back = new ItemStack(Material.RAW_FISH,1,(short) 3);
		ItemMeta im = back.getItemMeta();
		im.setDisplayName(ChatColor.GRAY+Lang.ITEMS_CRAFT_BOOK_BACK);
		back.setItemMeta(im);
		inv.setItem(49, back);
		
		player.openInventory(inv);
		
	}

}
