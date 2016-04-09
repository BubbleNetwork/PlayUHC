package com.microcraftmc.playuhc.scoreboard;

import com.microcraftmc.playuhc.BubbleUHC;
import com.thebubblenetwork.api.framework.BubbleNetwork;
import com.thebubblenetwork.api.framework.util.mc.scoreboard.api.BoardPreset;
import com.thebubblenetwork.api.framework.util.mc.scoreboard.api.BubbleBoardAPI;
import com.thebubblenetwork.api.framework.util.mc.scoreboard.util.BoardModuleBuilder;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

public class UhcBoard extends BoardPreset {

    public UhcBoard() {
        super("BubbleUHC", new BoardModuleBuilder("Server", 11)
                .withDisplay(ChatColor.BLUE + ChatColor.BOLD.toString() + "Server").build(),
                new BoardModuleBuilder("ServerValue", 10).withRandomDisplay().build(),
                new BoardModuleBuilder("Spacer1", 9).withRandomDisplay().build(),
                new BoardModuleBuilder("Players", 8).withDisplay(ChatColor.BLUE + ChatColor.BOLD.toString() + "Players").build(),
                new BoardModuleBuilder("AliveValue", 7).withDisplay(ChatColor.AQUA + "Alive: " + ChatColor.RESET).build(),
                new BoardModuleBuilder("WatchingValue", 6).withDisplay(ChatColor.AQUA + "Watching: " + ChatColor.RESET).build(),
                new BoardModuleBuilder("Spacer2", 5).withRandomDisplay().build(),
                new BoardModuleBuilder("address", 4).withDisplay("thebubblenetwork").build());
                //new BoardModuleBuilder("Spacer3", 1).withRandomDisplay().build(),
                //new BoardModuleBuilder("address", 0).withDisplay("thebubblenetwork").build());
    }

    public void onEnable(BubbleBoardAPI api) {
        int spectators = BubbleUHC.getInstance().getGame().getSpectatorList().size();
        int players = Bukkit.getOnlinePlayers().size() - spectators;
        Team address = api.getScore(this, getModule("address")).getTeam();
        address.setPrefix(ChatColor.GRAY + "play.");
        address.setSuffix(".com");
        api.getScore(this, getModule("ServerValue")).getTeam().setSuffix(BubbleNetwork.getInstance().getType().getName() + "-" + String.valueOf(BubbleNetwork.getInstance().getId()));
        api.getScore(this, getModule("AliveValue")).getTeam().setSuffix(String.valueOf(players));
        api.getScore(this, getModule("WatchingValue")).getTeam().setSuffix(String.valueOf(spectators));
    }


}
