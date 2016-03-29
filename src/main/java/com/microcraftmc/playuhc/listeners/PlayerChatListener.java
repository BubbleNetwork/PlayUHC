package com.microcraftmc.playuhc.listeners;

import com.microcraftmc.playuhc.PlayUhc;
import com.microcraftmc.playuhc.configuration.VaultManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.microcraftmc.playuhc.exceptions.UhcPlayerDoesntExistException;
import com.microcraftmc.playuhc.game.GameManager;
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

public class PlayerChatListener implements Listener{
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event){
		
		Player player = event.getPlayer();
		GameManager gm = GameManager.getGameManager();
		UhcPlayer uhcPlayer;
		try {
			uhcPlayer = gm.getPlayersManager().getUhcPlayer(player);
			switch(uhcPlayer.getState()){
			case WAITING:
				event.setCancelled(true);
				gm.broadcastMessage(ChatColor.AQUA + player.getName() + ChatColor.GRAY + ":" + ChatColor.RESET + " " + event.getMessage());
				break;
			case PLAYING:
				event.setCancelled(true);
				if(uhcPlayer.isGlobalChat())
					gm.broadcastMessage(ChatColor.AQUA + player.getName() + ChatColor.GRAY + ":" + ChatColor.RESET + " " + event.getMessage());
				else
					uhcPlayer.getTeam().sendChatMessageToTeamMembers(ChatColor.WHITE + uhcPlayer.getName() + " : "+  event.getMessage());
				break;
			case DEAD:
				event.setCancelled(true);
				if(gm.getConfiguration().getCanSendMessagesAfterDeath())
					gm.broadcastMessage(ChatColor.AQUA + "[Spec] " + uhcPlayer.getName() + ChatColor.GRAY + ":" + ChatColor.RESET + " " + event.getMessage());
				break;
			case HOST:
				event.setCancelled(true);
				gm.broadcastMessage(ChatColor.DARK_RED + "[Host] " + ChatColor.WHITE + player.getName() + ChatColor.GRAY + ":" + ChatColor.RESET + " " + event.getMessage());
				break;
			case MOD:
				event.setCancelled(true);
				gm.broadcastMessage(ChatColor.DARK_AQUA + "[UHC-Mod] " + ChatColor.WHITE + player.getName() + ChatColor.GRAY + ":" + ChatColor.RESET + " " + event.getMessage());
				break;

			}
		} catch (UhcPlayerDoesntExistException e) {
		}
				
		
	}
}
