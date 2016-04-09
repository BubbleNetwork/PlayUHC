package com.microcraftmc.playuhc;

import com.microcraftmc.playuhc.customitems.Kit;
import com.microcraftmc.playuhc.map.UhcMap;
import com.microcraftmc.playuhc.scoreboard.UhcBoard;
import com.thebubblenetwork.api.framework.BubbleNetwork;
import com.thebubblenetwork.api.framework.plugin.util.BubbleRunnable;
import com.thebubblenetwork.api.game.kit.KitManager;
import com.thebubblenetwork.api.game.maps.GameMap;
import com.thebubblenetwork.api.game.maps.MapData;
import com.thebubblenetwork.framework.api.global.bubblepackets.messaging.messages.handshake.JoinableUpdate;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import com.thebubblenetwork.api.game.BubbleGameAPI;

import com.microcraftmc.playuhc.game.GameManager;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

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
 * Project: playuhc
 *
 */

public class BubbleUHC extends BubbleGameAPI {

	private static final int VERSION = 1;
	
	private static BubbleUHC instance;

	public static BubbleUHC getInstance() {
		return instance;
	}

	private  GameManager gameManager;

	private UhcBoard scoreboard;

	public BubbleUHC() {
		super("BubbleUHC", GameMode.SURVIVAL, "NONE", 4);
		instance = this;
		scoreboard = new UhcBoard();
	}

	public void onEnable(){

		getPlugin().saveDefaultConfig();

		// Blocks players joins while loading the plugin
		Bukkit.getServer().setWhitelist(true);

		
	}

	public void onDisable(){
		Bukkit.getLogger().info("Plugin BubbleUHC disabled");
	}

	public void onStateChange(State oldstate, State newstate) {
		try {
			BubbleNetwork.getInstance().getPacketHub().sendMessage(BubbleNetwork.getInstance().getProxy(), new JoinableUpdate(newstate == State.LOBBY));
		} catch (IOException e) {
			BubbleNetwork.getInstance().getPlugin().getLogger().log(Level.WARNING, "Could not send joinable update for BubbleUHC", e);
		}

		if(newstate == State.HIDDEN){
			BubbleNetwork.getInstance().getLogger().log(Level.INFO, "BubbleUHC is currently Hidden");
		}
		else if(newstate == State.INGAME){
			BubbleNetwork.getInstance().getLogger().log(Level.INFO, "BubbleUHC is currently InGame");
		}
		else if(newstate == State.ENDGAME){
			Bukkit.broadcastMessage("Thank you for playing BubbleUHC");
		}
		else if (newstate == State.LOBBY) {
			new BubbleRunnable() {
				public void run() {

					gameManager = new GameManager();
					gameManager.loadNewGame();

				}
			}.runTask(this);
		}
		else if (newstate == State.RESTARTING) {
			BubbleNetwork.getInstance().getLogger().log(Level.INFO, "BubbleUHC is currently restarting");
		}
	}

	@Override
	public void teleportPlayers(GameMap map, World world) {

	}

	@Override
	public GameMap loadMap(String s, MapData mapData, File file, File file1) {
		return new UhcMap(gameManager.getConfiguration().getOverworldUuid());
	}

	public UhcBoard getScorePreset() {
		return scoreboard;
	}

	public long finishUp() {
		//TODO
		return Long.MAX_VALUE;
	}

	public int getVersion() {
		return VERSION;
	}

	@Override
	public void cleanup() {

	}

	public GameManager getGameManager() {
		return gameManager;
	}
}
