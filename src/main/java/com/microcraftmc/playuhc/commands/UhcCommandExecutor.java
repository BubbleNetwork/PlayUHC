package com.microcraftmc.playuhc.commands;

import com.microcraftmc.playuhc.configuration.LobbyConfig;
import com.microcraftmc.playuhc.game.GameManager;
import com.microcraftmc.playuhc.languages.Lang;
import com.microcraftmc.playuhc.players.UhcPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.microcraftmc.playuhc.game.GameState;
import com.microcraftmc.playuhc.players.PlayerState;
import com.microcraftmc.playuhc.threads.PreStartThread;

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

public class UhcCommandExecutor implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		// For debug purpose
		if(sender instanceof ConsoleCommandSender || sender instanceof Player){
			if(sender.hasPermission("playuhc.commands")){
				if(args.length > 0){
					switch(args[0]){
						case "gamestate":
							if(args.length == 2){
								try{
									CommandManager.setGameState(GameState.valueOf(args[1].toUpperCase()));
									return true;
								}catch(IllegalArgumentException e){
									sender.sendMessage(args[1]+" is not a valid game state");
								}
							} else {
								sender.sendMessage(ChatColor.GREEN + Lang.DISPLAY_MESSAGE_PREFIX + " " + ChatColor.RESET +
										"Current GameState: " + CommandManager.getGameState());
							}
						break;
						
						case "playerstate":
							if(args.length == 3){
								try{
									Player player = Bukkit.getPlayer(args[1]);
									if(player == null)
										throw new Exception("Player "+args[1]+" is not online");
									CommandManager.setPlayerState(player,PlayerState.valueOf(args[2].toUpperCase()));
									return true;
								}catch(IllegalArgumentException e){
									sender.sendMessage(args[2]+" is not a valid player state");
								}catch(Exception e){
									sender.sendMessage(e.getMessage());
								}
							}
							break;

						case "setlobby":
							if (sender instanceof Player) {
								Player player = (Player) sender;

								if (LobbyConfig.getInstance().setLobbySpawn(player.getLocation())) {
									player.sendMessage(ChatColor.GREEN + Lang.DISPLAY_MESSAGE_PREFIX +
											ChatColor.RESET + "uhc spawn successfully set.");
								} else {
									player.sendMessage(ChatColor.GREEN + Lang.DISPLAY_MESSAGE_PREFIX +
											ChatColor.RESET + "an error occured while trying to set the uhc spawn.");
								}

							} else {
								sender.sendMessage(ChatColor.GREEN + Lang.DISPLAY_MESSAGE_PREFIX +
										ChatColor.RESET + "This is a player only command.");
							}
							break;

						case "lobby":
							if (args.length == 2) {
								if (args[1].equalsIgnoreCase("enable")) {
									LobbyConfig.getInstance().setEnable(true);
									sender.sendMessage("Lobby enabled!");
								}
								if (args[1].equalsIgnoreCase("disable")) {
									LobbyConfig.getInstance().setEnable(false);
									sender.sendMessage("Lobby disabled!");
								}
							}
							break;

						case "host":
							if (sender instanceof Player) {
								Player player = (Player) sender;

								CommandManager.setPlayerState(player, PlayerState.HOST);
								player.sendMessage(ChatColor.GREEN + Lang.DISPLAY_MESSAGE_PREFIX +
										ChatColor.RESET + " UHC Host mode enabled");

							} else {
								sender.sendMessage(ChatColor.GREEN + Lang.DISPLAY_MESSAGE_PREFIX +
										ChatColor.RESET + "This is a player only command.");
							}
							break;

						case "mod":
							if (sender instanceof Player) {
								Player player = (Player) sender;

								CommandManager.setPlayerState(player, PlayerState.MOD);
								player.sendMessage(ChatColor.GREEN + Lang.DISPLAY_MESSAGE_PREFIX +
										ChatColor.RESET + " UHC Mod mode enabled");

							} else {
								sender.sendMessage(ChatColor.GREEN + Lang.DISPLAY_MESSAGE_PREFIX +
										ChatColor.RESET + "This is a player only command.");
							}
							break;
							
						case "pvp":
							if(args.length == 2){
								CommandManager.setPvp(Boolean.parseBoolean(args[1]));
								return true;
							}
							break;
							
						case "listplayers":
							CommandManager.listUhcPlayers(sender);
							return true;
						
						case "listteams":
							CommandManager.listUhcTeams(sender);
							return true;
							
						case "pause":
							String pauseState = PreStartThread.togglePause();
							sender.sendMessage("The starting thread state is now : "+pauseState);
							return true;
							
						case "force":
							String forceState = PreStartThread.toggleForce();
							sender.sendMessage("The starting thread state is now : "+forceState);
							return true;

					}
				}
			}
			
		}
		return false;
	}

}
