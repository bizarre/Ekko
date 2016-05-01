package com.alexandeh.nebula.factions;

import com.alexandeh.nebula.Nebula;
import com.alexandeh.nebula.files.ConfigCache;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */

public class FactionCommand {

    public Nebula main = Nebula.getInstance();
    public ConfigCache.LangConfigCache langConfigCache = main.getLangConfigCache();
    public ConfigCache.MainConfigCache mainConfigCache = main.getMainConfigCache();

    public FactionCommand() {
        main.getFramework().registerCommands(this);
    }

}
