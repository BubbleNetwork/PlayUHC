package com.microcraftmc.playuhc.threads;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;

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

public class WorldBorderThread implements Runnable{

	private long timeBeforeShrink;
	private long timeToShrink;
	private int endSize;
	
	public WorldBorderThread(long timeBeforeShrink,int endSize, long timeToShrink){
		this.timeBeforeShrink = timeBeforeShrink;
		this.endSize = endSize;
		this.timeToShrink = timeToShrink;
	}
	
	@Override
	public void run() {
		if(timeBeforeShrink <= 0){
			startMoving();
		}else{
			timeBeforeShrink--;
			Bukkit.getScheduler().runTaskLaterAsynchronously(PlayUhc.getInstance().getPlugin(), this, 20);
		}
	}
	
	private void startMoving(){
		GameManager.getGameManager().broadcastInfoMessage(Lang.GAME_BORDER_START_SHRINKING);
		
		World overworld = Bukkit.getWorld(GameManager.getGameManager().getConfiguration().getOverworldUuid());
		WorldBorder overworldBorder = overworld.getWorldBorder();
		overworldBorder.setSize(2*endSize, timeToShrink);
		
		World nether = Bukkit.getWorld(GameManager.getGameManager().getConfiguration().getNetherUuid());
		WorldBorder netherBorder = nether.getWorldBorder();
		netherBorder.setSize(endSize, timeToShrink);
	}

}
