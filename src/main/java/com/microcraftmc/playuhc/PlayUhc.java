package com.microcraftmc.playuhc;

import com.microcraftmc.playuhc.game.GameState;
import com.thebubblenetwork.api.framework.plugin.util.BubbleRunnable;
import com.thebubblenetwork.api.game.maps.GameMap;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
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

	public PlayUhc() {
		super("PlayUhc", GameMode.SURVIVAL, "NONE", 4);

		instance = this;

	}

	public void onEnable(){

		getPlugin().saveDefaultConfig();

		// Blocks players joins while loading the plugin
		Bukkit.getServer().setWhitelist(true);


		new BubbleRunnable() {
			public void run() {

				GameManager gameManager = new GameManager();
				gameManager.loadNewGame();

				//unlock so players can join and rely on UhcPlayerJoinListener
				getServer().setWhitelist(false);

			}
		}.runTask(getInstance());
		
		
	}

	public void onDisable(){
		Bukkit.getLogger().info("Plugin PlayUHC disabled");
	}

	public void onStateChange(State oldstate, State newstate) {

	}

	public void teleportPlayers(GameMap map, World world) {

	}

	public

	public long finishUp() {
		//TODO
		return Long.MAX_VALUE;
	}

	public int getVersion() {
		return VERSION;
	}
}
