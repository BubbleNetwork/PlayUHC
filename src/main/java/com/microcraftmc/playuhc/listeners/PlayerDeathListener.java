package com.microcraftmc.playuhc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.microcraftmc.playuhc.PlayUhc;
import com.microcraftmc.playuhc.configuration.MainConfiguration;
import com.microcraftmc.playuhc.configuration.VaultManager;
import com.microcraftmc.playuhc.customitems.UhcItems;
import com.microcraftmc.playuhc.events.UHCPlayerKillEvent;
import com.microcraftmc.playuhc.exceptions.UhcPlayerDoesntExistException;
import com.microcraftmc.playuhc.game.GameManager;
import com.microcraftmc.playuhc.languages.Lang;
import com.microcraftmc.playuhc.players.PlayerState;
import com.microcraftmc.playuhc.players.PlayersManager;
import com.microcraftmc.playuhc.players.UhcPlayer;
import com.microcraftmc.playuhc.threads.TimeBeforeSendBungeeThread;

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

public class PlayerDeathListener implements Listener {

	private boolean enableKillEvent;
	private double reward;
	
	public PlayerDeathListener(){
		GameManager gm = GameManager.getGameManager();
		this.enableKillEvent = gm.getConfiguration().getEnableKillEvent();
		this.reward = gm.getConfiguration().getRewardKillEvent();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		GameManager gm = GameManager.getGameManager();
		PlayersManager pm = gm.getPlayersManager();
		MainConfiguration cfg = gm.getConfiguration();
		UhcPlayer uhcPlayer;
		try {
			uhcPlayer = pm.getUhcPlayer(player);
			if(uhcPlayer.getState().equals(PlayerState.PLAYING)){
				
				// kill event
				Player killer = player.getKiller();
				UHCPlayerKillEvent killEvent;
				if(killer != null){
					killEvent = new UHCPlayerKillEvent(uhcPlayer, pm.getUhcPlayer(player.getKiller()));
					Bukkit.getServer().getPluginManager().callEvent(killEvent);
					if(enableKillEvent){
						VaultManager.addMoney(killer, reward);
						if(!Lang.EVENT_KILL_REWARD.isEmpty()){
							killer.sendMessage(Lang.EVENT_KILL_REWARD.replace("%money%", ""+reward));
						}
					}
				}
				
				
				// eliminations
				gm.broadcastInfoMessage(Lang.PLAYERS_ELIMINATED.replace("%player%", player.getName()));
				if(cfg.getRegenHeadDropOnPlayerDeath()){
					UhcItems.spawnRegenHead(player);
				}
				if(cfg.getEnableExpDropOnDeath()){
					UhcItems.spawnExtraXp(player.getLocation(), cfg.getExpDropOnDeath());
				}
				uhcPlayer.setState(PlayerState.DEAD);
				pm.strikeLightning(uhcPlayer);
				pm.playSoundPlayerDeath();
				if(!cfg.getCanSpectateAfterDeath()){
					player.kickPlayer(Lang.DISPLAY_MESSAGE_PREFIX+" "+Lang.KICK_DEAD);
				}
				if(cfg.getEnableBungeeSupport() && cfg.getTimeBeforeSendBungeeAfterDeath() >= 0){
					Bukkit.getScheduler().runTaskAsynchronously(PlayUhc.getPlugin(), new TimeBeforeSendBungeeThread(uhcPlayer, cfg.getTimeBeforeSendBungeeAfterDeath()));
				}
				pm.checkIfRemainingPlayers();
			}else{
				player.kickPlayer("Don't cheat !");
			}
		} catch (UhcPlayerDoesntExistException e) {
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		GameManager gm = GameManager.getGameManager();
		final PlayersManager pm = gm.getPlayersManager();
		final UhcPlayer uhcPlayer;
		try {
			uhcPlayer = pm.getUhcPlayer(player);

			if(uhcPlayer.getState().equals(PlayerState.DEAD)){
				
				Bukkit.getScheduler().runTaskLater(PlayUhc.getPlugin(), new Runnable(){

					@Override
					public void run() {
						pm.setPlayerSpectateAtLobby(uhcPlayer);
					}}, 1);
			}
		} catch (UhcPlayerDoesntExistException e) {
		}
	}
	
}
