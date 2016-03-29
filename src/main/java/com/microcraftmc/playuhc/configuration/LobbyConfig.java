package com.microcraftmc.playuhc.configuration;

import java.io.File;
import java.io.IOException;

import com.microcraftmc.playuhc.PlayUhc;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

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

public class LobbyConfig {

    private static LobbyConfig instance = new LobbyConfig();

    private File lobbyFile;
    private FileConfiguration lobbyConfig;

    public String LOBBY_WORLD;
    public Double LOBBY_LOCATION_X;
    public Double LOBBY_LOCATION_Z;
    public Double LOBBY_LOCATION_Y;
    public boolean LOBBY_ENABLED;

    public LobbyConfig() {

    }

    public static LobbyConfig getInstance() {
        return instance;
    }

    public void setup() {
        lobbyFile = new File(PlayUhc.getPlugin().getDataFolder(), "lobby.yml");
        if (lobbyFile.exists()) {
            lobbyConfig = YamlConfiguration.loadConfiguration(lobbyFile);

            //lobby enabled?
            LOBBY_ENABLED = lobbyConfig.getBoolean("enabled");

            //lobby world
            LOBBY_WORLD = lobbyConfig.getString("world");

            //lobby cords
            LOBBY_LOCATION_X = lobbyConfig.getDouble("spawn.x");
            LOBBY_LOCATION_Z = lobbyConfig.getDouble("spawn.z");
            LOBBY_LOCATION_Y = lobbyConfig.getDouble("spawn.y");

        }
        else {
           try {
               saveDefaultLobbyConfig();
           } catch (IOException e) {
               e.printStackTrace();
           }
        }

    }

    public void saveDefaultLobbyConfig() throws IOException {

        lobbyFile.createNewFile();
        lobbyConfig = YamlConfiguration.loadConfiguration(lobbyFile);

        //by default the lobby should be disabled
        lobbyConfig.set("enabled", false);
        lobbyConfig.set("world", "lobby");
        lobbyConfig.set("spawn.x", 0D);
        lobbyConfig.set("spawn.z", 0D);
        lobbyConfig.set("spawn.y", 0D);

        //save our lobby file
        lobbyConfig.save(lobbyFile);

    }

    public FileConfiguration getLobbyConfig() {
        return lobbyConfig;
    }

    public void saveLobbyConfig() {
        try {
            //attempt to save our lobby config file
            lobbyConfig.save(lobbyFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadLobbyConfig() {
        setup();
    }

    public boolean isEnabled() {
        return LOBBY_ENABLED;
    }

    public boolean setLobbySpawn(Location playerLocation) {
        try {

            //set our location for the lobby spawn
            lobbyConfig.set("world", playerLocation.getWorld().getName());
            lobbyConfig.set("spawn.x", playerLocation.getX());
            lobbyConfig.set("spawn.z", playerLocation.getZ());
            lobbyConfig.set("spawn.y", playerLocation.getY());

            //save our lobby config file
            saveLobbyConfig();

            //reload our LobbyConfig
            reloadLobbyConfig();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Location getLobbySpawn() {
        try {
            return new Location(Bukkit.getServer().getWorld(LOBBY_WORLD), LOBBY_LOCATION_X, LOBBY_LOCATION_Y, LOBBY_LOCATION_Z);
        } catch (Exception e) {
            return null;
        }
    }

    public void setEnable(boolean enabled) {
        //set our enabled value in our config
        lobbyConfig.set("enabled", enabled);

        //save our config file
        saveLobbyConfig();

        //reload our config file
        reloadLobbyConfig();
    }

}
