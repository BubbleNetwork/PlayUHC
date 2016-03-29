package com.microcraftmc.playuhc.threads;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.microcraftmc.playuhc.PlayUhc;
import com.microcraftmc.playuhc.exceptions.UhcPlayerDoesntExistException;
import com.microcraftmc.playuhc.game.GameManager;
import com.microcraftmc.playuhc.game.GameState;
import com.microcraftmc.playuhc.languages.Lang;
import com.microcraftmc.playuhc.players.PlayerState;
import com.microcraftmc.playuhc.players.PlayersManager;
import com.microcraftmc.playuhc.players.UhcPlayer;

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

public class KillDisconnectedPlayerThread implements Runnable {
	
	String name;
	int timeLeft;
	KillDisconnectedPlayerThread task;
	
	public KillDisconnectedPlayerThread(String playerName) {
		name = playerName;
		timeLeft = GameManager.getGameManager().getConfiguration().getMaxDisconnectPlayersTime();
		task = this;
	}

	@Override
	public void run() {
		if(GameManager.getGameManager().getGameState().equals(GameState.PLAYING)){
			Bukkit.getScheduler().runTask(PlayUhc.getPlugin(), new Runnable(){

					@Override
					public void run() {
						Player player = Bukkit.getPlayer(name);
						if(player == null){
							if(timeLeft <= 0){
								UhcPlayer uhcPlayer;
								GameManager gm = GameManager.getGameManager();
								PlayersManager pm = gm.getPlayersManager();
								try {
									uhcPlayer = pm.getUhcPlayer(name);
									gm.broadcastInfoMessage(Lang.PLAYERS_ELIMINATED.replace("%player%", name));
									uhcPlayer.setState(PlayerState.DEAD);
									pm.strikeLightning(uhcPlayer);
									pm.playSoundPlayerDeath();
									pm.checkIfRemainingPlayers();
								} catch (UhcPlayerDoesntExistException e) {
								}
							}else{
								timeLeft-=5;
								Bukkit.getScheduler().runTaskLaterAsynchronously(PlayUhc.getPlugin(), task, 100);
							}
						}
						
					}});
		}
		
	}

}
