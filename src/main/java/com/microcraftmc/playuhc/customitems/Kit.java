package com.microcraftmc.playuhc.customitems;

import java.util.List;

import org.bukkit.inventory.ItemStack;

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

public class Kit {
	protected String key;
	protected String name;
	protected ItemStack symbol;
	protected List<ItemStack> items;
	public String getKey() {
		return key;
	}
	public String getName() {
		return name;
	}
}
