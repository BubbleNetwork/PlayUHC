package com.microcraftmc.playuhc.threads;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

import com.microcraftmc.playuhc.PlayUhc;
import com.microcraftmc.playuhc.game.GameManager;
import com.microcraftmc.playuhc.game.GameState;
import com.microcraftmc.spigotutils.Sounds;

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

public class TimeBeforeEndThread implements Runnable{

	long remainingTime;
	GameManager gm;
	TimeBeforeEndThread task;
	
	
	public TimeBeforeEndThread() {
		this.gm = GameManager.getGameManager();
		this.remainingTime = gm.getRemainingTime();
		task = this;
	}
	
	@Override
	public void run() {
		
		remainingTime--;
		gm.setRemainingTime(remainingTime);
		
		if(remainingTime >= 0 && remainingTime <= 60 && (remainingTime%10 == 0 || remainingTime <= 10)){
			Sounds.playAll(Sound.CLICK);
		}
		
		if(remainingTime > 0 && (gm.getGameState().equals(GameState.PLAYING) || gm.getGameState().equals(GameState.DEATHMATCH)))
			Bukkit.getScheduler().runTaskLaterAsynchronously(PlayUhc.getInstance().getPlugin(), task, 20);
	}
	
}
