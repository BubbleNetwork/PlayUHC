package com.microcraftmc.playuhc.threads;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.microcraftmc.playuhc.PlayUhc;
import com.microcraftmc.playuhc.configuration.VaultManager;
import com.microcraftmc.playuhc.events.UhcTimeEvent;
import com.microcraftmc.playuhc.exceptions.UhcPlayerNotOnlineException;
import com.microcraftmc.playuhc.game.GameManager;
import com.microcraftmc.playuhc.game.GameState;
import com.microcraftmc.playuhc.languages.Lang;
import com.microcraftmc.playuhc.players.UhcPlayer;
import com.microcraftmc.playuhc.utils.TimeUtils;

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

public class ElapsedTimeThread implements Runnable{

	private GameManager gm;
	private ElapsedTimeThread task;
	private boolean enableTimeEvent;
	private long intervalTimeEvent;
	private double reward;
	
	public ElapsedTimeThread() {
		this.gm = GameManager.getGameManager();
		this.task = this;
		this.enableTimeEvent = gm.getConfiguration().getEnableTimeEvent();
		this.intervalTimeEvent = gm.getConfiguration().getIntervalTimeEvent();
		this.reward = gm.getConfiguration().getRewardTimeEvent();
	}
	
	@Override
	public void run() {
		
		long time = gm.getElapsedTime() + 1;
		gm.setElapsedTime(time);
		
		if(time%intervalTimeEvent == 0){

			Set<UhcPlayer> playingPlayers = gm.getPlayersManager().getPlayingPlayer();
			
			// Call time event
			UhcTimeEvent event = new UhcTimeEvent(playingPlayers,intervalTimeEvent,time);
			Bukkit.getServer().getPluginManager().callEvent(event);
			
			if(enableTimeEvent){
				
				String message = Lang.EVENT_TIME_REWARD
						.replace("%time%", TimeUtils.getFormattedTime(intervalTimeEvent))
						.replace("%totaltime%", TimeUtils.getFormattedTime(time))
						.replace("%money%", ""+reward);
				
				for(UhcPlayer uhcP : playingPlayers){
					
					try {
						Player p = uhcP.getPlayer();
						VaultManager.addMoney(p, reward);
						if(!message.isEmpty()){
							p.sendMessage(message);
						}
					} catch (UhcPlayerNotOnlineException e) {
						// Tignore offline players
					}
				}
			}
		}
		
		if(!gm.getGameState().equals(GameState.ENDED)){
			Bukkit.getScheduler().runTaskLaterAsynchronously(PlayUhc.getInstance().getPlugin(), task, 20);
		}	
	}
	
}
