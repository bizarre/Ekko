package com.alexandeh.nebula;

import com.alexandeh.nebula.files.ConfigFile;
import com.alexandeh.nebula.files.ConfigCache.MainConfigCache;
import com.alexandeh.nebula.files.ConfigCache.LangConfigCache;
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

    private ConfigFile mainFile;
    private ConfigFile langFile;

    private MainConfigCache mainConfigCache;
    private LangConfigCache langConfigCache;

    public void onEnable() {
        instance = this;

        mainFile = new ConfigFile(this, "config");
        langFile = new ConfigFile(this, "lang");

        mainConfigCache = new MainConfigCache(mainFile);
        langConfigCache = new LangConfigCache(langFile);
    }

    public static Nebula getInstance() {
        return instance;
    }
}
