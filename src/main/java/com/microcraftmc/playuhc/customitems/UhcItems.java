package com.microcraftmc.playuhc.customitems;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.microcraftmc.playuhc.configuration.MainConfiguration;
import com.microcraftmc.playuhc.exceptions.UhcPlayerDoesntExistException;
import com.microcraftmc.playuhc.game.GameManager;
import com.microcraftmc.playuhc.languages.Lang;
import com.microcraftmc.playuhc.players.UhcPlayer;
import com.microcraftmc.playuhc.players.UhcTeam;

/**
 * Copyright Statement
 * ----------------------
 * Copyright (C) Microcraft MC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Class information
 * ---------------------
 * Package: com.microcraftmc.playuhc
 * Project: PlayUHC
 *
 */

public class UhcItems {
	
	public static void giveLobbyItemTo(Player player){
		ItemStack item =new ItemStack(Material.IRON_SWORD , 1 , (short) 0);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Lang.ITEMS_SWORD);
		meta.setLore(Arrays.asList("Lobby"));
		item.setItemMeta(meta);
		player.getInventory().addItem(item);
	}
	
	
	public static boolean isLobbyItem(ItemStack item){
		return (
				item != null 
				&& item.getType().equals(Material.IRON_SWORD)
				&& item.getItemMeta().getLore().contains("Lobby")
				);
	}
	
	public static void openTeamInventory(Player player){
		int maxSlots = 6*9;
		Inventory inv = Bukkit.createInventory(null, maxSlots, ChatColor.GREEN+Lang.DISPLAY_MESSAGE_PREFIX+" "+ChatColor.DARK_GREEN+Lang.TEAM_INVENTORY);
		int slot = 0;
		GameManager gm = GameManager.getGameManager();
		List<UhcTeam> teams = gm.getPlayersManager().listUhcTeams();
		for(UhcTeam team : teams){
			if(slot < maxSlots){
				ItemStack item = createTeamSkullItem(team);
				inv.setItem(slot, item);
				slot++;
			}
		}
		
		// Leave team item
		if(!gm.getConfiguration().getPreventPlayerFromLeavingTeam()){
			ItemStack leaveTeamItem = new ItemStack(Material.BARRIER,1,(short) 0);
			ItemMeta imLeave = leaveTeamItem.getItemMeta();
			imLeave.setDisplayName(ChatColor.RED+Lang.ITEMS_BARRIER);
			leaveTeamItem.setItemMeta(imLeave);
			inv.setItem(maxSlots-1, leaveTeamItem);
		}
		
		UhcPlayer uhcPlayer;
		try {
			uhcPlayer = gm.getPlayersManager().getUhcPlayer(player);
			

			// Team ready/not ready item
			if(uhcPlayer.isTeamLeader() && !gm.getConfiguration().getTeamAlwaysReady()){
				
				// Laine rouge
				short woolColor = 14;
				String readyState = ChatColor.RED+Lang.TEAM_NOT_READY;
				
				if(uhcPlayer.getTeam().isReadyToStart()){
					// Laine verte
					woolColor = 5;
					readyState = ChatColor.GREEN+Lang.TEAM_READY;
				}
				
				ItemStack readyTeamItem = new ItemStack(Material.WOOL,1,woolColor);
				ItemMeta imReady = readyTeamItem.getItemMeta();
				imReady.setDisplayName(readyState);
				List<String> readyLore = new ArrayList<String>();
				readyLore.add(ChatColor.GRAY+Lang.TEAM_READY_TOGGLE);
				imReady.setLore(readyLore);
				readyTeamItem.setItemMeta(imReady);
				inv.setItem(maxSlots-2, readyTeamItem);
			}
			
			player.openInventory(inv);
		} catch (UhcPlayerDoesntExistException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static ItemStack createTeamSkullItem(UhcTeam team){
		String leaderName = team.getLeader().getName();
		ItemStack item = ItemFactory.createPlayerSkull(leaderName);
		List<String> membersNames = team.getMembersNames();
		item.setAmount(membersNames.size());
		SkullMeta im = (SkullMeta) item.getItemMeta();
		
		// Setting up lore with team members
		List<String> teamLore = new ArrayList<String>();
		teamLore.add(ChatColor.GREEN+"Members");
		for(String teamMember : membersNames){
			teamLore.add(ChatColor.WHITE+teamMember);
		}
		
		// Ready State
		if(team.isReadyToStart())
			teamLore.add(ChatColor.GREEN+"--- "+Lang.TEAM_READY+" ---");
		else
			teamLore.add(ChatColor.RED+"--- "+Lang.TEAM_NOT_READY+" ---");
		
		im.setLore(teamLore);
		
		im.setOwner(leaderName);
		im.setDisplayName(leaderName);
		item.setItemMeta(im);
		return item;
	}
	
	public static boolean isLobbyTeamItem(ItemStack item){
		if(item != null && item.getType() == Material.SKULL_ITEM){
			List<String> lore = item.getItemMeta().getLore();
			return lore.contains(ChatColor.GREEN+"Members") || lore.contains(ChatColor.DARK_PURPLE+Lang.TEAM_REQUEST_HEAD);
		}
		return false;
	}
	
	public static boolean isLobbyLeaveTeamItem(ItemStack item){
			return (
					item != null 
					&& item.getType() == Material.BARRIER 
					&& item.getItemMeta().getDisplayName().equals(ChatColor.RED+Lang.ITEMS_BARRIER)
					);
	}
	


	public static boolean isLobbyReadyTeamItem(ItemStack item) {
		return (
				item != null 
				&& item.getType() == Material.WOOL 
				&& (item.getItemMeta().getDisplayName().equals(ChatColor.RED+Lang.TEAM_NOT_READY)
						|| item.getItemMeta().getDisplayName().equals(ChatColor.GREEN+Lang.TEAM_READY))
				);
	}
	

	public static boolean isRegenHeadItem(ItemStack item) {
		return (
				item != null 
				&& item.getType() == Material.SKULL_ITEM 
				&& item.getItemMeta().getLore().contains(ChatColor.GREEN+Lang.ITEMS_REGEN_HEAD)
				);
	}
	
	public static boolean doesInventoryContainsLobbyTeamItem(Inventory inv, String name){
		for(ItemStack item : inv.getContents()){
			if(item!=null && item.getItemMeta().getDisplayName().equals(name) && isLobbyTeamItem(item))
				return true;
		}
		return false;
	}

	public static void spawnRegenHead(Player player) {
		MainConfiguration cfg = GameManager.getGameManager().getConfiguration();		
		if(cfg.getRegenHeadDropOnPlayerDeath()){
			
			String name = player.getName();
			ItemStack item = ItemFactory.createPlayerSkull(name);
			item.setAmount(1);
			SkullMeta im = (SkullMeta) item.getItemMeta();
			
			// Setting up lore with team members
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GREEN+Lang.ITEMS_REGEN_HEAD);
			im.setLore(lore);
			im.setDisplayName(name);		
			item.setItemMeta(im);
			
			player.getWorld().dropItem(player.getLocation(), item);
		}
	}


	public static void giveCompassPlayingTo(Player player) {
		ItemStack compass = new ItemStack(Material.COMPASS, 1);
		ItemMeta im = compass.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+Lang.ITEMS_COMPASS_PLAYING);
		compass.setItemMeta(im);
		player.getInventory().addItem(compass);
	}


	
	public static boolean isCompassPlayingItem(ItemStack item) {
		return (
				item != null 
				&& item.getType() == Material.COMPASS 
				&& item.getItemMeta().getDisplayName().equals(ChatColor.GREEN+Lang.ITEMS_COMPASS_PLAYING)
				);
	}
	
	public static boolean isKitSelectionItem(ItemStack item){
		return (
				item != null 
				&& item.getType() == Material.IRON_PICKAXE 
				&& item.getItemMeta().getDisplayName().equals(ChatColor.GREEN+Lang.ITEMS_KIT_SELECTION)
				);
	}
	
	public static void giveKitSelectionTo(Player player) {
		if(KitsManager.isAtLeastOneKit()){
			ItemStack pickaxe = new ItemStack(Material.IRON_PICKAXE, 1);
			ItemMeta im = pickaxe.getItemMeta();
			im.setDisplayName(ChatColor.GREEN+Lang.ITEMS_KIT_SELECTION);
			pickaxe.setItemMeta(im);
			player.getInventory().addItem(pickaxe);
		}
	}
	
	public static void giveCraftBookTo(Player player) {
		if(CraftsManager.isAtLeastOneCraft()){
			ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
			ItemMeta im = book.getItemMeta();
			im.setDisplayName(ChatColor.LIGHT_PURPLE+Lang.ITEMS_CRAFT_BOOK);
			book.setItemMeta(im);
			player.getInventory().addItem(book);
		}
	}
	
	public static boolean isCraftBookItem(ItemStack item){
		return (
				item != null 
				&& item.getType().equals(Material.ENCHANTED_BOOK) 
				&& item.getItemMeta().getDisplayName().equals(ChatColor.LIGHT_PURPLE+Lang.ITEMS_CRAFT_BOOK)
				);
	}


	public static void spawnExtraXp(Location location, int quantity) {
		ExperienceOrb orb = (ExperienceOrb) location.getWorld().spawnEntity(location, EntityType.EXPERIENCE_ORB);
		orb.setExperience(quantity);	
	}

	
	
	
}
