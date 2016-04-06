package com.microcraftmc.playuhc.threads;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

import com.microcraftmc.playuhc.PlayUhc;
import com.microcraftmc.playuhc.game.GameManager;
import com.microcraftmc.playuhc.game.GameState;
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

public class EnablePVPThread implements Runnable{

	int timeBeforePVP;
	EnablePVPThread task;
	GameManager gm;
	
	public EnablePVPThread(){
		timeBeforePVP = GameManager.getGameManager().getConfiguration().getTimeBeforePvp();;
		task = this;
		gm = GameManager.getGameManager();
	}
	
	@Override
	public void run() {
		Bukkit.getScheduler().runTask(PlayUhc.getInstance().getPlugin(), new Runnable(){

			@Override
			public void run() {
				
				if(gm.getGameState().equals(GameState.PLAYING)){

					if(timeBeforePVP == 0){
						GameManager.getGameManager().setPvp(true);
						GameManager.getGameManager().broadcastInfoMessage(Lang.PVP_ENABLED);
						GameManager.getGameManager().getPlayersManager().playsoundToAll(Sound.WITHER_SPAWN);
					}else{
						
						if(timeBeforePVP <= 10 || timeBeforePVP%60 == 0){
							if(timeBeforePVP%60 == 0)
								GameManager.getGameManager().broadcastInfoMessage(Lang.PVP_START_IN+" "+(timeBeforePVP/60)+"m");
							else
								GameManager.getGameManager().broadcastInfoMessage(Lang.PVP_START_IN+" "+timeBeforePVP+"s");
							
							GameManager.getGameManager().getPlayersManager().playsoundToAll(Sound.CLICK);
						}
						
						if(timeBeforePVP >= 20){
							timeBeforePVP -= 10;
							Bukkit.getScheduler().runTaskLaterAsynchronously(PlayUhc.getInstance().getPlugin(), task,200);
						}else{
							timeBeforePVP --;
							Bukkit.getScheduler().runTaskLaterAsynchronously(PlayUhc.getInstance().getPlugin(), task,20);
						}
					}
				}

				
			}});
	}
}
