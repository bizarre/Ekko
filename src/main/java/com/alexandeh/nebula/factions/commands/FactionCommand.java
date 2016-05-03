package com.alexandeh.nebula.factions.commands;

import com.alexandeh.nebula.Nebula;
import com.alexandeh.nebula.files.ConfigFile;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */

public class FactionCommand {

    public Nebula main = Nebula.getInstance();
    public ConfigFile langConfig = main.getLangConfig();
    public ConfigFile mainConfig = main.getMainConfig();

    public FactionCommand() {
        main.getFramework().registerCommands(this);
    }

}