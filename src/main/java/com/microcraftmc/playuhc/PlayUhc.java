package com.microcraftmc.playuhc;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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

public class PlayUhc extends JavaPlugin{
	
	private static PlayUhc pl;
	
	
	public void onEnable(){
		pl = this;
	
		// Blocks players joins while loading the plugin
		Bukkit.getServer().setWhitelist(true);
		saveDefaultConfig();
		
		
		Bukkit.getScheduler().runTaskLater(this, new Runnable(){
			
			@Override
			public void run() {
				GameManager gameManager = new GameManager();
				gameManager.loadNewGame();
				
				// Unlock players joins and rely on UhcPlayerJoinListener
				Bukkit.getServer().setWhitelist(false);
			}
			
		}, 1);
		
		
	}
	
	public static PlayUhc getPlugin(){
		return pl;
	}
	
	public void onDisable(){
		Bukkit.getLogger().info("Plugin PlayUHC disabled");
	}
}
