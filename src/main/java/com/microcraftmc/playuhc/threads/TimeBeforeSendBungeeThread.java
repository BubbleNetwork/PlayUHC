package com.microcraftmc.playuhc.threads;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.microcraftmc.playuhc.PlayUhc;
import com.microcraftmc.playuhc.exceptions.UhcPlayerNotOnlineException;
import com.microcraftmc.playuhc.game.GameManager;
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

public class TimeBeforeSendBungeeThread implements Runnable{
	
	private UhcPlayer uhcPlayer;
	private int remainingTime;
	private TimeBeforeSendBungeeThread task;
	
	public TimeBeforeSendBungeeThread(UhcPlayer uhcPlayer,int remainingTime){
		this.uhcPlayer = uhcPlayer;
		this.remainingTime = remainingTime;
		this.task = this;
	}


	@Override
	public void run() {
		
		Bukkit.getScheduler().runTask(PlayUhc.getPlugin(), new Runnable(){

			@Override
			public void run() {
				remainingTime--;
				
				Player player;
				try {
					player = uhcPlayer.getPlayer();

					if(remainingTime <=10 || (remainingTime > 10 && remainingTime%10 == 0)){
						player.sendMessage(Lang.PLAYERS_SEND_BUNGEE.replace("%time%",TimeUtils.getFormattedTime(remainingTime)));
						GameManager.getGameManager().getPlayersManager().playsoundTo(uhcPlayer, Sound.CLICK);
					}
					
					if(remainingTime <= 0){
						GameManager.getGameManager().getPlayersManager().sendPlayerToBungeeServer(player, "");
					}
					
				} catch (UhcPlayerNotOnlineException e) {
					// nothing to do for offline players
				}
				
				if(remainingTime > 0){
					Bukkit.getScheduler().runTaskLaterAsynchronously(PlayUhc.getPlugin(), task, 20);
				}
			}
			
		});
		
		
	}

}
