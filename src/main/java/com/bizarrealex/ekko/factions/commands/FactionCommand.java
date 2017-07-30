package com.bizarrealex.ekko.factions.commands;

import com.bizarrealex.ekko.Ekko;
import com.bizarrealex.ekko.files.ConfigFile;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */

public class FactionCommand {

    public Ekko main = Ekko.getInstance();
    public ConfigFile langConfig = main.getLangConfig();
    public ConfigFile mainConfig = main.getMainConfig();

    public FactionCommand() {
        main.getFramework().registerCommands(this);
    }
}
