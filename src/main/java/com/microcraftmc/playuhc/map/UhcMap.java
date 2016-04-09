package com.microcraftmc.playuhc.map;

import com.microcraftmc.playuhc.BubbleUHC;
import com.thebubblenetwork.api.game.maps.GameMap;
import com.thebubblenetwork.api.game.maps.GameMap;
import com.thebubblenetwork.api.game.maps.MapData;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.microcraftmc.spigotutils.items.util.NMSClass.World;

public class UhcMap extends GameMap {

    private boolean loaded = false;

    private String netherWorld;

    public UhcMap (String name)  {
            super(name, null, null, null);
    }

    @SuppressWarnings("unchecked")
    public Map loadSetting(ConfigurationSection configurationSection) {
        Map map = new HashMap<>();

        List<String> overworld = new ArrayList<>();

        overworld.add(getName());

        map.put("overworld", overworld);

        return map;
    }

}
