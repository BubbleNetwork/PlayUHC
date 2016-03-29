package com.microcraftmc.playuhc.utils;

import java.util.Arrays;

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

public class CompareUtils {
	
	public static boolean equalsToAny(Object comparable, Object... compareList){
		return Arrays.asList(compareList).contains(comparable);
	}
}
