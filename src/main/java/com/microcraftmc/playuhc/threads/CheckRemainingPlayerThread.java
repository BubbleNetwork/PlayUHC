package com.microcraftmc.playuhc.threads;

import com.microcraftmc.playuhc.BubbleUHC;
import org.bukkit.Bukkit;

import com.microcraftmc.playuhc.BubbleUHC;
import com.microcraftmc.playuhc.game.GameManager;
import com.microcraftmc.playuhc.game.GameState;

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

public class CheckRemainingPlayerThread implements Runnable{

	private CheckRemainingPlayerThread task;
	
	public CheckRemainingPlayerThread(){
		task = this;
	}
	
	@Override
	public void run() {
		
		Bukkit.getScheduler().runTask(BubbleUHC.getInstance().getPlugin(), new Runnable(){

			@Override
			public void run() {
				GameManager.getGameManager().getPlayersManager().checkIfRemainingPlayers();
				GameState state = GameManager.getGameManager().getGameState();
				if(state.equals(GameState.PLAYING) || state.equals(GameState.DEATHMATCH))
					Bukkit.getScheduler().runTaskLaterAsynchronously(BubbleUHC.getInstance().getPlugin(),task,40);
				}
				
		});
	}

}
