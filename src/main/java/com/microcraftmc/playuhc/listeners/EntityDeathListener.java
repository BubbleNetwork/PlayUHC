package com.microcraftmc.playuhc.listeners;

import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.microcraftmc.playuhc.configuration.MainConfiguration;
import com.microcraftmc.playuhc.game.GameManager;

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

public class EntityDeathListener implements Listener {

	// Gold drops
	private final int min;
	private final int max;
	private final int chance;
	private final List<EntityType> affectedMobs;
	private final boolean allowGhastTearDrop;
	private final boolean enableGoldDrops;
	
	// Fast mode cooked food
	private final boolean enableCookedFood;
	
	public EntityDeathListener() {
		MainConfiguration cfg = GameManager.getGameManager().getConfiguration();
		min = cfg.getMinGoldDrops();
		max = cfg.getMaxGoldDrops();
		chance = cfg.getGoldDropPercentage();
		affectedMobs = cfg.getAffectedGoldDropsMobs();
		allowGhastTearDrop = cfg.getAllowGhastTearsDrops();
		enableGoldDrops = cfg.getEnableGoldDrops();
		enableCookedFood = cfg.getCookedDroppedFood();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDeath(EntityDeathEvent event) {

		handleCookedFoodDrop(event);
		handleGoldDrop(event);
		handleGhastTearDrop(event);
		
	}
	
	private void handleGoldDrop(EntityDeathEvent event){
		if(enableGoldDrops && affectedMobs.contains(event.getEntityType())){
			Random r = new Random();
			if(r.nextInt(100) < chance){
				int drop;
				try{
					drop = min+r.nextInt(1+max-min);
				}catch(IllegalArgumentException e){
					drop=0;
				}
				if(drop > 0){
					ItemStack gold = new ItemStack(Material.GOLD_INGOT,drop);
					event.getDrops().add(gold);
				}
			}			
		}
	}
	
	private void handleGhastTearDrop(EntityDeathEvent event){
		if(event.getEntityType().equals(EntityType.GHAST) && !allowGhastTearDrop){
			for(int i = event.getDrops().size()-1 ; i>=0 ; i--){
				if(event.getDrops().get(i).getType().equals(Material.GHAST_TEAR)){
					event.getDrops().remove(i);
				}
			}
		}
	}

	
	private void handleCookedFoodDrop(EntityDeathEvent event){
		if(enableCookedFood){
			for(int i=0 ; i<event.getDrops().size() ; i++){
				Material replaceBy = null;
				switch(event.getDrops().get(i).getType()){
					case RAW_BEEF:
						replaceBy = Material.COOKED_BEEF;
						break;
					case RAW_CHICKEN:
						replaceBy = Material.COOKED_CHICKEN;
						break;
					case MUTTON:
						replaceBy = Material.COOKED_MUTTON;
						break;
					case RABBIT:
						replaceBy = Material.COOKED_RABBIT;
						break;
					case PORK:
						replaceBy = Material.GRILLED_PORK;
						break;
					default:
						break;
				}
				if(replaceBy != null){
					ItemStack cookedFood = event.getDrops().get(i).clone();
					cookedFood.setType(replaceBy);
					event.getDrops().set(i, cookedFood);
				}
			}
		}
	}
}
