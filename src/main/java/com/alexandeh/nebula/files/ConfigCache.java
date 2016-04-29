package com.alexandeh.nebula.files;

import lombok.Getter;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
@Getter
public class ConfigCache {

    private ConfigFile configFile;

    public ConfigCache(ConfigFile configFile) {
        this.configFile = configFile;

    }

    @Getter
    public static class LangConfigCache extends ConfigCache {

        public LangConfigCache(ConfigFile configFile) {
            super(configFile);
        }
    }

    @Getter
    public static class MainConfigCache extends ConfigCache {

        public MainConfigCache(ConfigFile configFile) {
            super(configFile);
        }
    }

}
