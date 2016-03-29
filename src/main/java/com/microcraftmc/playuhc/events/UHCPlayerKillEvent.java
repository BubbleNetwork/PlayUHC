package com.microcraftmc.playuhc.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

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

public final class UHCPlayerKillEvent extends Event{
	
	private static final HandlerList handlers = new HandlerList();
	private UhcPlayer killer;
	private UhcPlayer killed;
	
	public UHCPlayerKillEvent(UhcPlayer killer, UhcPlayer killed){
		this.killed = killer;
		this.killed = killed;
	}
	
	public UhcPlayer getKiller(){
		return killer;
	}
	
	public UhcPlayer getKilled(){
		return killed;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
