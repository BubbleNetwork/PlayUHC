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

public class StopRestartThread implements Runnable{

	long timeBeforeStop;
	
	
	public StopRestartThread(){
		this.timeBeforeStop = GameManager.getGameManager().getConfiguration().getTimeBeforeRestartAfterEnd();
	}
	
	@Override
	public void run() {
		GameManager gm = GameManager.getGameManager();
			
			if(timeBeforeStop == 0){
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
			}else{
				if(timeBeforeStop<5 || timeBeforeStop%10 == 0){
					Bukkit.getLogger().info("[PlayUhc] Server will shutdown in "+timeBeforeStop+"s");
					gm.broadcastInfoMessage(Lang.GAME_SHUTDOWN.replace("%time%", ""+timeBeforeStop));
				}
				
				timeBeforeStop--;
				Bukkit.getScheduler().scheduleSyncDelayedTask(PlayUhc.getPlugin(), this,20);
				}
	}

}
