package com.microcraftmc.playuhc.threads;

import org.bukkit.Bukkit;

import com.microcraftmc.playuhc.PlayUhc;
import com.microcraftmc.playuhc.game.GameManager;
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
 * Package: com.microcraftmc.playuhc
 * Project: PlayUHC
 *
 */

public class Auto20MinBroadcastThread implements Runnable {
	
	boolean broadcast;
	Auto20MinBroadcastThread task;
	
	public Auto20MinBroadcastThread() {
		broadcast = GameManager.getGameManager().getConfiguration().getAuto20MinBroadcast();
		task = this;
	}

	@Override
	public void run() {
		Bukkit.getScheduler().runTask(PlayUhc.getPlugin(), new Runnable(){

			@Override
			public void run() {

				if(broadcast){
					GameManager.getGameManager().broadcastInfoMessage(Lang.DISPLAY_YOUTUBER_MARK);
					Bukkit.getScheduler().runTaskLaterAsynchronously(PlayUhc.getPlugin(), task, 24000);
				}
				
			}});
	}

}
