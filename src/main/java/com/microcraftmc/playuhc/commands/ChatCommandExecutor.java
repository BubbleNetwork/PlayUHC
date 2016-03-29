package com.microcraftmc.playuhc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.microcraftmc.playuhc.exceptions.UhcPlayerDoesntExistException;
import com.microcraftmc.playuhc.game.GameManager;
import com.microcraftmc.playuhc.game.GameState;
import com.microcraftmc.playuhc.languages.Lang;
import com.microcraftmc.playuhc.players.PlayerState;
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

public class ChatCommandExecutor implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if(sender instanceof Player){
			Player player = (Player) sender;
			GameManager gm = GameManager.getGameManager();
			UhcPlayer uhcPlayer;
			try {
				uhcPlayer = gm.getPlayersManager().getUhcPlayer(player);
				if(uhcPlayer != null && uhcPlayer.getState().equals(PlayerState.PLAYING) && gm.getGameState().equals(GameState.PLAYING)){
					if(args.length == 0){
						if(uhcPlayer.isGlobalChat()){
							uhcPlayer.setGlobalChat(false);
							uhcPlayer.sendMessage(ChatColor.GREEN+Lang.COMMAND_CHAT_TEAM);
						}
						else{
							uhcPlayer.setGlobalChat(true);
							uhcPlayer.sendMessage(ChatColor.GREEN+Lang.COMMAND_CHAT_GLOBAL);
						}
					}else{
						player.sendMessage(ChatColor.GRAY+Lang.COMMAND_CHAT_HELP);
					}
				}else{
					player.sendMessage(ChatColor.RED+Lang.COMMAND_CHAT_ERROR);
				}
				

				return true;
			} catch (UhcPlayerDoesntExistException e) {
			}
			
		}
		

		return false;
	}

}
