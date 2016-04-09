package com.microcraftmc.playuhc.configuration;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.microcraftmc.playuhc.BubbleUHC;
import com.microcraftmc.playuhc.game.GameManager;

/**
 * Copyright Statement
 * ----------------------
 * Copyright (C) Microcraft MC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Class information
 * ---------------------
 * Package: com.microcraftmc.BubbleUHC
 * Project: BubbleUHC
 *
 */

public class VaultManager {
	
    private static Economy economy = null;
    
    public static void setupEconomy(){
    	if(GameManager.getGameManager().getConfiguration().getVaultLoaded()){
    		RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
	        if (economyProvider != null) {
	            economy = economyProvider.getProvider();
	        }else{
	        	Bukkit.getLogger().severe("Error trying to load economy provider. Check that you have a economy plugin installed");
	        }
    	}
    }

	
	public static void addMoney(Player player, final Double amount){
		if(GameManager.getGameManager().getConfiguration().getVaultLoaded()){
			if(economy == null){
				Bukkit.getLogger().warning("Vault is not loaded ! Couldnt pay "+amount+" to "+player.getName()+" !");
			}else{
				
				final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player.getUniqueId());
				if(offlinePlayer != null){
					Bukkit.getScheduler().runTaskAsynchronously(BubbleUHC.getInstance().getPlugin(), new Runnable(){

						@Override
						public void run() {
							economy.depositPlayer(offlinePlayer, amount);
						}
						
					});
				}
				
				
			}
		}
	}
	

}
