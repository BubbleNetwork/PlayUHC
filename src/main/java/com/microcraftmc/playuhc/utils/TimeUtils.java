package com.microcraftmc.playuhc.utils;

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

public class TimeUtils {
	public static String getFormattedTime(long time){
		int h,m;
		h = (int) time / (60 * 60);
		time -= h * (60 * 60);
		m = (int) time / 60;
		time -= m * 60;
		
		if(h == 0){
			if(m == 0){
				return time+"s";
			}else{
				return m+"m "+time+"s";
			}
		}else{
			return h+"h "+m+"m "+time+"s";
		}
	}
}
