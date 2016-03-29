package com.microcraftmc.playuhc.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

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

public class PingListener implements Listener{
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPing(ServerListPingEvent event){
		GameManager gm = GameManager.getGameManager();
		if(gm != null){
			switch(gm.getGameState()){
			case ENDED:
				event.setMotd(Lang.DISPLAY_MOTD_ENDED);
				break;
			case LOADING:
				event.setMotd(Lang.DISPLAY_MOTD_LOADING);
				break;
			case DEATHMATCH:
			case PLAYING:
				event.setMotd(Lang.DISPLAY_MOTD_PLAYING);
				break;
			case STARTING:
				event.setMotd(Lang.DISPLAY_MOTD_STARTING);
				break;
			case WAITING:
				if (gm.getConfiguration().isFFa() && !gm.getConfiguration().isCutclean()) {
					event.setMotd(Lang.GAMETYPE_FFA_VANILLA);
				}
				if (gm.getConfiguration().isFFa() && gm.getConfiguration().isCutclean()) {
					event.setMotd(Lang.GAMETYPE_FFA_CUTCLEAN);
				}
				if (!gm.getConfiguration().isFFa() && !gm.getConfiguration().isCutclean()) {
					String teamsize = Integer.toString(gm.getConfiguration().getMaxPlayersPerTeam());
					event.setMotd(Lang.GAMETYPE_TEAM_VANILLA.replace("%teamsize%", teamsize));
				}
				if (!gm.getConfiguration().isFFa() && gm.getConfiguration().isCutclean()) {
					String teamsize = Integer.toString(gm.getConfiguration().getMaxPlayersPerTeam());
					event.setMotd(Lang.GAMETYPE_TEAM_CUTCLEAN.replace("%teamsize%", teamsize));
				}
				break;
			default:
				event.setMotd(Lang.DISPLAY_MESSAGE_PREFIX);
				break;
			
			}
		}else{
			event.setMotd(Lang.DISPLAY_MESSAGE_PREFIX);
		}
	}
}
