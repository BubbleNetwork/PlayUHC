package com.microcraftmc.playuhc.threads;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.microcraftmc.playuhc.game.GameManager;
import com.microcraftmc.playuhc.players.UhcPlayer;
import com.microcraftmc.playuhc.players.UhcTeam;

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

public class TeleportPlayersThread implements Runnable {
	
	UhcTeam team;
	
	public TeleportPlayersThread(UhcTeam team) {
		this.team = team;
	}

	@Override
	public void run() {
		
		for(UhcPlayer uhcPlayer : team.getMembers()){
			Player player = Bukkit.getPlayer(uhcPlayer.getName());
			if(player != null){
				Bukkit.getLogger().info("[PlayUHC] Teleporting "+player.getName());
				for(PotionEffect effect : GameManager.getGameManager().getConfiguration().getPotionEffectOnStart()){
					player.addPotionEffect(effect);
				}
				player.teleport(team.getStartingLocation());
				player.removePotionEffect(PotionEffectType.BLINDNESS);
				player.setFireTicks(0);
				uhcPlayer.setHasBeenTeleportedToLocation(true);
			}
		}
		
		
	}

}
