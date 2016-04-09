package com.microcraftmc.playuhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.microcraftmc.playuhc.exceptions.UhcPlayerDoesntExistException;
import com.microcraftmc.playuhc.game.GameManager;
import com.microcraftmc.playuhc.game.GameState;
import com.microcraftmc.playuhc.players.PlayerState;
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
 * Project: playuhc
 *
 */

public class CommandManager {
	public static void setGameState(GameState gameState){
		GameManager.getGameManager().setGameState(gameState);
	}

	public static String getGameState() {
		String gameState;

		switch (GameManager.getGameManager().getGameState()) {
			case LOADING:
				gameState = "LOADING";
				break;
			case WAITING:
				gameState = "WAITING";
				break;
			case STARTING:
				gameState = "STARTING";
				break;
			case PLAYING:
				gameState = "PLAYING";
				break;
			case DEATHMATCH:
				gameState = "DEATHMATCH";
				break;
			case ENDED:
				gameState = "ENDED";
				break;
			default:
				gameState = "";
		}

		return gameState;
	}

	public static void setPlayerState(Player player, PlayerState state) {
		try {
			GameManager.getGameManager().getPlayersManager().getUhcPlayer(player).setState(state);
		} catch (UhcPlayerDoesntExistException e) {
		}
	}

	public static void setPvp(boolean state) {
		GameManager.getGameManager().setPvp(state);
	}

	public static void listUhcPlayers(CommandSender sender) {
		StringBuilder str = new StringBuilder();
		str.append("Current UhcPlayers : ");
		for(UhcPlayer player : GameManager.getGameManager().getPlayersManager().getPlayersList()){
			str.append(player.getName()+" ");
		}
		sender.sendMessage(str.toString());
		//Bukkit.getLogger().info(str.toString());
	}

	public static void listUhcTeams(CommandSender sender) {
		StringBuilder str;
		sender.sendMessage("Current UhcTeams : ");
		//Bukkit.getLogger().info("Current UhcTeams : ");
		
		for(UhcTeam team : GameManager.getGameManager().getPlayersManager().listUhcTeams()){
			str = new StringBuilder();
			str.append("Team "+team.getLeader().getName()+" : ");
			for(UhcPlayer player : team.getMembers()){
				str.append(player.getName()+" ");
			}
			sender.sendMessage(str.toString());
			//Bukkit.getLogger().info(str.toString());
		}
	}
}
