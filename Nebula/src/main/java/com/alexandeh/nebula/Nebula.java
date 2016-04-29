package com.alexandeh.nebula;

import com.alexandeh.nebula.files.ConfigFile;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
@Getter
public class Nebula extends JavaPlugin {

    private static Nebula instance;

    private ConfigFile configFile;
    private ConfigFile langFile;

    public void onEnable() {
        instance = this;

        configFile = new ConfigFile(this, "config");
        langFile = new ConfigFile(this, "lang");
    }

    public static Nebula getInstance() {
        return instance;
    }
}
