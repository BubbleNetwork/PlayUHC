package com.microcraftmc.playuhc.utils;

import java.util.Random;

import org.bukkit.block.BlockFace;

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

public class RandomUtils {
	private static Random r = new Random();
	
	public static int randomInteger(int min, int max){
		int realMin = Math.min(min, max);
		int realMax = Math.max(min, max);
		int exclusiveSize = realMax-realMin;
		return r.nextInt(exclusiveSize+1)+min;
	}
	
	public static BlockFace randomAdjacentFace(){
		BlockFace[] faces = new BlockFace[]{
			BlockFace.DOWN,
			BlockFace.UP,
			BlockFace.EAST,
			BlockFace.WEST,
			BlockFace.NORTH,
			BlockFace.SOUTH
		};
		return faces[randomInteger(0,faces.length-1)];
	}
	
}
