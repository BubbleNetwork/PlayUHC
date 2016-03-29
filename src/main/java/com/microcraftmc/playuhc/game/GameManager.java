package com.microcraftmc.playuhc.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.microcraftmc.playuhc.configuration.LobbyConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

import com.microcraftmc.playuhc.PlayUhc;
import com.microcraftmc.playuhc.commands.ChatCommandExecutor;
import com.microcraftmc.playuhc.commands.TeleportCommandExecutor;
import com.microcraftmc.playuhc.commands.UhcCommandExecutor;
import com.microcraftmc.playuhc.configuration.MainConfiguration;
import com.microcraftmc.playuhc.customitems.CraftsManager;
import com.microcraftmc.playuhc.customitems.KitsManager;
import com.microcraftmc.playuhc.languages.Lang;
import com.microcraftmc.playuhc.listeners.BlockListener;
import com.microcraftmc.playuhc.listeners.CraftListener;
import com.microcraftmc.playuhc.listeners.EntityDeathListener;
import com.microcraftmc.playuhc.listeners.ItemsListener;
import com.microcraftmc.playuhc.listeners.PingListener;
import com.microcraftmc.playuhc.listeners.PlayerChatListener;
import com.microcraftmc.playuhc.listeners.PlayerConnectionListener;
import com.microcraftmc.playuhc.listeners.PlayerDamageListener;
import com.microcraftmc.playuhc.listeners.PlayerDeathListener;
import com.microcraftmc.playuhc.listeners.PortalListener;
import com.microcraftmc.playuhc.maploader.MapLoader;
import com.microcraftmc.playuhc.players.PlayersManager;
import com.microcraftmc.playuhc.players.UhcPlayer;
import com.microcraftmc.playuhc.schematics.DeathmatchArena;
import com.microcraftmc.playuhc.schematics.Lobby;
import com.microcraftmc.playuhc.schematics.UndergroundNether;
import com.microcraftmc.playuhc.threads.Auto20MinBroadcastThread;
import com.microcraftmc.playuhc.threads.ElapsedTimeThread;
import com.microcraftmc.playuhc.threads.EnablePVPThread;
import com.microcraftmc.playuhc.threads.EndThread;
import com.microcraftmc.playuhc.threads.PreStartThread;
import com.microcraftmc.playuhc.threads.StartDeathmatchThread;
import com.microcraftmc.playuhc.threads.StopRestartThread;
import com.microcraftmc.playuhc.threads.TimeBeforeEndThread;
import com.microcraftmc.playuhc.utils.TimeUtils;
import com.microcraftmc.spigotutils.Sounds;

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

public class GameManager {
	private GameState gameState;
	private Lobby lobby;
	private DeathmatchArena arena;
	private PlayersManager playerManager;
	private MapLoader mapLoader;
	private UhcWorldBorder worldBorder;
	private boolean pvp;
	private boolean gameIsEnding;
	
	private long remainingTime;
	private long elapsedTime = 0;
	
	private MainConfiguration configuration;
	
	private static GameManager uhcGM = null;
	
	public GameManager() {
		uhcGM = this;
	}



	public MainConfiguration getConfiguration() {
		return configuration;
	}
	
	public UhcWorldBorder getWorldBorder() {
		return worldBorder;
	}

	public static GameManager getGameManager(){
		return uhcGM;
	}
	
	public synchronized GameState getGameState(){
		return gameState;
	}

	
	
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	
	public PlayersManager getPlayersManager(){
		return playerManager;
	}
	
	public MapLoader getMapLoader(){
		return mapLoader;
	}

	public Lobby getLobby() {
		return lobby;
	}
	
	public DeathmatchArena getArena() {
		return arena;
	}

	public boolean getGameIsEnding() {
		return gameIsEnding;
	}
	
	public synchronized long getRemainingTime(){
		return remainingTime;
	}
	
	public synchronized long getElapsedTime(){
		return elapsedTime;
	}


	public String getFormatedRemainingTime() {
		return TimeUtils.getFormattedTime(getRemainingTime());
	}

	public synchronized void setRemainingTime(long time){
		remainingTime = time;
	}
	
	public synchronized void setElapsedTime(long time){
		elapsedTime = time;
	}

	public boolean getPvp() {
		return pvp;
	}
	public void setPvp(boolean state) {
		pvp = state;
	}
	
	public void loadNewGame() {
		deleteOldPlayersFiles();
		gameState = GameState.LOADING;
		loadConfig();
		
		worldBorder = new UhcWorldBorder();
		playerManager = new PlayersManager();
		
		registerListeners();
		
		mapLoader = new MapLoader();
		if(getConfiguration().getDebug()){
			mapLoader.loadOldWorld(configuration.getOverworldUuid(),Environment.NORMAL);
			mapLoader.loadOldWorld(configuration.getNetherUuid(),Environment.NETHER);
		}else{
			mapLoader.deleteLastWorld(configuration.getOverworldUuid());
			mapLoader.deleteLastWorld(configuration.getNetherUuid());
			mapLoader.createNewWorld(Environment.NORMAL);
			mapLoader.createNewWorld(Environment.NETHER);
		}
		
		if(getConfiguration().getEnableBungeeSupport())
			PlayUhc.getPlugin().getServer().getMessenger().registerOutgoingPluginChannel(PlayUhc.getPlugin(), "BungeeCord");
		
		if(getConfiguration().getEnablePregenerateWorld() && !getConfiguration().getDebug())
			mapLoader.generateChunks(Environment.NORMAL);
		else
			GameManager.getGameManager().startWaitingPlayers();
	}
	
	private void deleteOldPlayersFiles() {
		
		if(Bukkit.getServer().getWorlds().size()>0){
			// Deleting old players files
			File playerdata = new File(Bukkit.getServer().getWorlds().get(0).getName()+"/playerdata");
			if(playerdata.exists() && playerdata.isDirectory()){
				for(File playerFile : playerdata.listFiles()){
					playerFile.delete();
				}
			}
			
			// Deleting old players stats
			File stats = new File(Bukkit.getServer().getWorlds().get(0).getName()+"/stats");
			if(stats.exists() && stats.isDirectory()){
				for(File statFile : stats.listFiles()){
					statFile.delete();
				}
			}
		}
		
	}



	public void startWaitingPlayers(){
		loadWorlds();
		registerCommands();
		gameState = GameState.WAITING;
		Bukkit.getLogger().info(Lang.DISPLAY_MESSAGE_PREFIX+" Players are now allowed to join");
		Bukkit.getScheduler().scheduleSyncDelayedTask(PlayUhc.getPlugin(), new PreStartThread(),0);
	}
	
	public void startGame(){
		setGameState(GameState.STARTING);
		if(!getConfiguration().getAlwaysDay())
			Bukkit.getWorld(configuration.getOverworldUuid()).setGameRuleValue("doDaylightCycle", "true");
		broadcastInfoMessage(Lang.GAME_STARTING);
		broadcastInfoMessage(Lang.GAME_PLEASE_WAIT_TELEPORTING);
		getPlayersManager().randomTeleportTeams();	
		gameIsEnding = false;
	}
	
	public void startWatchingEndOfGame(){
		gameState = GameState.PLAYING;

		World overworld = Bukkit.getWorld(configuration.getOverworldUuid());
		overworld.setGameRuleValue("doMobSpawning", "true");

		//TODO: are we using a schematic lobby?
		getLobby().destroyBoundingBox();
		getPlayersManager().startWatchPlayerPlayingThread();
		Bukkit.getScheduler().runTaskAsynchronously(PlayUhc.getPlugin(), new ElapsedTimeThread());
		Bukkit.getScheduler().runTaskAsynchronously(PlayUhc.getPlugin(), new EnablePVPThread());
		Bukkit.getScheduler().runTaskAsynchronously(PlayUhc.getPlugin(), new Auto20MinBroadcastThread());
		if(getConfiguration().getEnableTimeLimit())
			Bukkit.getScheduler().runTaskAsynchronously(PlayUhc.getPlugin(), new TimeBeforeEndThread());
		worldBorder.startBorderThread();
	}

	public void broadcastMessage(String message){
		for(UhcPlayer player : getPlayersManager().getPlayersList()){
			player.sendMessage(message);
		}
	}
	
	public void broadcastInfoMessage(String message){
		broadcastMessage(ChatColor.GREEN+Lang.DISPLAY_MESSAGE_PREFIX+" "+ChatColor.WHITE+message);
	}
	
	private void loadConfig(){
		new Lang();

		FileConfiguration cfg = PlayUhc.getPlugin().getConfig();
		configuration = new MainConfiguration();
		configuration.load(cfg);
		
		// Load kits
		KitsManager.loadKits();
		CraftsManager.loadCrafts();
		CraftsManager.loadBannedCrafts();

		//load our lobby config
		LobbyConfig.getInstance().setup();
	}

	private void registerListeners(){
		// Registers Listeners
			List<Listener> listeners = new ArrayList<Listener>();		
			listeners.add(new PlayerConnectionListener());	
			listeners.add(new PlayerChatListener());
			listeners.add(new PlayerDamageListener());
			listeners.add(new ItemsListener());
			listeners.add(new PortalListener());
			listeners.add(new PlayerDeathListener());
			listeners.add(new EntityDeathListener());
			listeners.add(new CraftListener());
			listeners.add(new PingListener());
			listeners.add(new BlockListener());
			for(Listener listener : listeners){
				Bukkit.getServer().getPluginManager().registerEvents(listener, PlayUhc.getPlugin());
			}
	}
	
	private void loadWorlds(){
		World overworld = Bukkit.getWorld(configuration.getOverworldUuid());
		overworld.save();
		overworld.setGameRuleValue("naturalRegeneration", "false");
		overworld.setGameRuleValue("doDaylightCycle", "false");
		overworld.setGameRuleValue("commandBlockOutput", "false");
		overworld.setGameRuleValue("logAdminCommands", "false");
		overworld.setGameRuleValue("sendCommandFeedback", "false");
		overworld.setGameRuleValue("doMobSpawning", "false");
		overworld.setTime(6000);
		overworld.setDifficulty(Difficulty.HARD);
		overworld.setWeatherDuration(999999999);
		
		World nether = Bukkit.getWorld(configuration.getNetherUuid());
		nether.save();
		nether.setGameRuleValue("naturalRegeneration", "false");
		nether.setGameRuleValue("commandBlockOutput", "false");
		nether.setGameRuleValue("logAdminCommands", "false");
		nether.setGameRuleValue("sendCommandFeedback", "false");
		nether.setDifficulty(Difficulty.HARD);

		//TODO: should we build a schematic or should we use another world for the lobby?
		lobby = new Lobby(new Location(overworld, 0, 200, 0), Material.GLASS);
		lobby.build();
		lobby.loadLobbyChunks();

		
		arena = new DeathmatchArena(new Location(overworld, 10000, configuration.getArenaPasteAtY(), 10000));
		arena.build();
		arena.loadChunks();
		
		UndergroundNether undergoundNether = new UndergroundNether();
		undergoundNether.build();
		
		worldBorder.setUpBukkitBorder();
		
		pvp = false;
	}
	
	private void registerCommands(){
			// Registers CommandExecutor
			PlayUhc.getPlugin().getCommand("uhc").setExecutor(new UhcCommandExecutor());
			PlayUhc.getPlugin().getCommand("chat").setExecutor(new ChatCommandExecutor());
			PlayUhc.getPlugin().getCommand("teleport").setExecutor(new TeleportCommandExecutor());
	}

	public void endGame() {
		if(gameState.equals(GameState.PLAYING) || gameState.equals(GameState.DEATHMATCH)){
			setGameState(GameState.ENDED);
			pvp = false;
			gameIsEnding = true;
			broadcastInfoMessage(Lang.GAME_FINISHED);
			Sounds.playAll(Sound.ENDERDRAGON_GROWL, 1, 2);
			getPlayersManager().setAllPlayersEndGame();
			Bukkit.getScheduler().scheduleSyncDelayedTask(PlayUhc.getPlugin(), new StopRestartThread(),20);
		}
		
	}
	
	public void startDeathmatch() {
		if(gameState.equals(GameState.PLAYING)){
			setGameState(GameState.DEATHMATCH);
			pvp = false;
			broadcastInfoMessage(Lang.GAME_START_DEATHMATCH);
			getPlayersManager().playsoundToAll(Sound.ENDERDRAGON_GROWL);
			Location arenaLocation = getArena().getLoc();
			
			//Set big border size to avoid hurting players
			getWorldBorder().setBukkitWorldBorderSize(arenaLocation.getWorld(), arenaLocation.getBlockX(), arenaLocation.getBlockZ(), 50000);
			
			// Teleport players
			getPlayersManager().setAllPlayersStartDeathmatch();
			
			// Shrink border to arena size
			getWorldBorder().setBukkitWorldBorderSize(arenaLocation.getWorld(), arenaLocation.getBlockX(), arenaLocation.getBlockZ(), getArena().getMaxSize());
			
			// Start Enable pvp thread
			Bukkit.getScheduler().scheduleSyncDelayedTask(PlayUhc.getPlugin(), new StartDeathmatchThread(),20);
		}
		
	}

	public void startEndGameThread() {
		if(gameIsEnding == false && (gameState.equals(GameState.DEATHMATCH) || gameState.equals(GameState.PLAYING))){
			gameIsEnding = true;
			EndThread.start();
		}		
	}
	
	public void stopEndGameThread(){
		if(gameIsEnding == true && (gameState.equals(GameState.DEATHMATCH) || gameState.equals(GameState.PLAYING))){
			gameIsEnding = false;
			EndThread.stop();
		}
	}


	
	


	
	
	

}
