package com.microcraftmc.playuhc.schematics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;

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

@SuppressWarnings("deprecation")
public class SchematicHandler {
	
	public static ArrayList<Integer> pasteSchematic(Location loc, String path) throws MaxChangedBlocksException, com.sk89q.worldedit.data.DataException, IOException{
		WorldEditPlugin we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		Bukkit.getLogger().info("[PlayUHC] Pasting "+path);
		File schematic = new File(path);
		EditSession session = we.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(loc.getWorld()), 1000000);
		
		CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(schematic).load(schematic);
		clipboard.paste(session, new Vector(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ()), false);

		ArrayList<Integer> dimensions = new ArrayList<Integer>();
		dimensions.add(clipboard.getHeight());
		dimensions.add(clipboard.getLength());
		dimensions.add(clipboard.getWidth());
		
		Bukkit.getLogger().info("[PlayUHC] Successfully pasted '"+path+"' at "+loc.getBlockX()+" "+loc.getBlockY()+" "+loc.getBlockZ());
		return dimensions;
		
	}
}
