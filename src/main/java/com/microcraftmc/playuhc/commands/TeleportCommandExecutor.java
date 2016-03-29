package com.microcraftmc.playuhc.commands;

import org.bukkit.Bukkit;
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
import com.microcraftmc.playuhc.players.PlayersManager;
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

public class TeleportCommandExecutor implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if(sender instanceof Player){
			Player player = (Player) sender;
			GameManager gm = GameManager.getGameManager();
			PlayersManager pm = gm.getPlayersManager();
			UhcPlayer uhcPlayer;
			try {
				uhcPlayer = pm.getUhcPlayer(player);
				if(uhcPlayer != null 
						&& uhcPlayer.getState().equals(PlayerState.DEAD) 
						&& gm.getGameState().equals(GameState.PLAYING)
						&& gm.getConfiguration().getSpectatingTeleport()){
					if(args.length == 1){
						Player target = Bukkit.getPlayer(args[0]);
						if(target == null){
							uhcPlayer.sendMessage(ChatColor.RED+Lang.COMMAND_SPECTATING_TELEPORT_ERROR);
						}else{
							UhcPlayer uhcTarget = pm.getUhcPlayer(target);
							if(uhcTarget.getState().equals(PlayerState.PLAYING)){
								uhcPlayer.sendMessage(ChatColor.GREEN+Lang.COMMAND_SPECTATING_TELEPORT.replace("%player%", uhcTarget.getName()));
								player.teleport(target);
							}else{
								uhcPlayer.sendMessage(ChatColor.RED+Lang.COMMAND_SPECTATING_TELEPORT_ERROR);
							}
						}
							
					}else{
						uhcPlayer.sendMessage(ChatColor.RED+Lang.COMMAND_SPECTATING_TELEPORT_ERROR);
					}
				}else{
					uhcPlayer.sendMessage(ChatColor.RED+Lang.COMMAND_SPECTATING_TELEPORT_ERROR);
				}
			} catch (UhcPlayerDoesntExistException e) {
			}

			return true;
		}
		

		return false;
	}

}
