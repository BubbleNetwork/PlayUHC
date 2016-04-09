package com.microcraftmc.playuhc.game;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.file.FileConfiguration;

import com.microcraftmc.playuhc.BubbleUHC;
import com.microcraftmc.playuhc.threads.WorldBorderThread;

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

public class UhcWorldBorder {
	private boolean moving;
	private int startSize;
	private int endSize;
	private long timeToShrink;
	private long timeBeforeShrink;
	
	public UhcWorldBorder(){
		FileConfiguration cfg = BubbleUHC.getInstance().getPlugin().getConfig();
		moving = cfg.getBoolean("border.moving",true);
		startSize = cfg.getInt("border.start-size",1000);
		endSize = cfg.getInt("border.end-size",0);
		timeToShrink = cfg.getLong("border.time-to-shrink",3600);
		timeBeforeShrink = cfg.getLong("border.time-before-shrink",0);
		
		Bukkit.getLogger().info("[PlayUHC] Border start size is "+startSize);
		Bukkit.getLogger().info("[PlayUHC] Border end size is "+startSize);
		Bukkit.getLogger().info("[PlayUHC] Border moves : "+moving);
		Bukkit.getLogger().info("[PlayUHC] Border timeBeforeENd : "+timeToShrink);
	}
	
	public boolean getMoving() {
		return moving;
	}
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	
	public void setUpBukkitBorder(){
		Bukkit.getScheduler().runTaskLater(BubbleUHC.getInstance().getPlugin(), new Runnable(){

			@Override
			public void run() {
				World overworld = Bukkit.getWorld(GameManager.getGameManager().getConfiguration().getOverworldUuid());
				setBukkitWorldBorderSize(overworld,0,0,2*startSize);
				
				World nether = Bukkit.getWorld(GameManager.getGameManager().getConfiguration().getNetherUuid());
				setBukkitWorldBorderSize(nether,0,0,startSize);
			}
			
		}, 200);
	}
	
	public void setBukkitWorldBorderSize(World world, int centerX, int centerZ, double edgeSize){
		WorldBorder worldborder = world.getWorldBorder();
		worldborder.setCenter(centerX,centerZ);
		worldborder.setSize(edgeSize);
	}
	
	public int getStartSize() {
		return startSize;
	}

	public void setStartSize(int startSize) {
		this.startSize = startSize;
	}

	public int getEndSize() {
		return endSize;
	}

	public void setEndSize(int endSize) {
		this.endSize = endSize;
	}

	public long getTimeToShrink() {
		return timeToShrink;
	}

	public void setTimeBeforeEnd(long timeBeforeEnd) {
		this.timeToShrink = timeBeforeEnd;
	}

	public void startBorderThread() {
		if(getMoving()){
			Bukkit.getScheduler().runTaskAsynchronously(BubbleUHC.getInstance().getPlugin(), new WorldBorderThread(timeBeforeShrink, endSize, timeToShrink));
		}
		
	}
}
