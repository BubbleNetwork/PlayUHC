package com.microcraftmc.playuhc;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.thebubblenetwork.api.game.BubbleGameAPI;

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

public class PlayUhc extends BubbleGameAPI {

	public static final int VERSION = 1;
	
	private static PlayUhc instance;

	public static PlayUhc getInstance() {
		return instance;
	}

	public void onEnable(){
	
		// Blocks players joins while loading the plugin
		Bukkit.getServer().setWhitelist(true);


		Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable(){
			
			@Override
			public void run() {
				GameManager gameManager = new GameManager();
				gameManager.loadNewGame();
				
				// Unlock players joins and rely on UhcPlayerJoinListener
				Bukkit.getServer().setWhitelist(false);
			}
			
		}, 1);
		
		
	}

	public void onDisable(){
		Bukkit.getLogger().info("Plugin PlayUHC disabled");
	}

	public void onStateChange(State oldstate, State newstate) {
		if (newstate == State.HIDDEN){

		}
	}

	public long finishUp() {
		//TODO
		return Long.MAX_VALUE;
	}

	public int getVersion() {
		return VERSION;
	}
}
