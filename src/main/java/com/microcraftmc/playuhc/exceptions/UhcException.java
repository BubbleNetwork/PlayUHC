package com.microcraftmc.playuhc.exceptions;

import com.microcraftmc.playuhc.languages.Lang;

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

public class UhcException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5868977668183366492L;

	
	public UhcException(String message){
		super(Lang.DISPLAY_MESSAGE_PREFIX+" "+message);
	}
}
