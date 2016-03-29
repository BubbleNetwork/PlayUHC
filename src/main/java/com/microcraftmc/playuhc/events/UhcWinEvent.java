package com.microcraftmc.playuhc.events;

import java.util.Set;

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

public class UhcWinEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	private Set<UhcPlayer> winners;
	
	public UhcWinEvent(Set<UhcPlayer> winners){
		this.winners = winners;
	}

	public Set<UhcPlayer> getWinners(){
		return winners;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
