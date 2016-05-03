package com.alexandeh.nebula.utils.player;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
@Getter
public class SimpleOfflinePlayer {

    private static Set<SimpleOfflinePlayer> offlinePlayers = new HashSet<>();

    private String name;
    private UUID uuid;

    public SimpleOfflinePlayer(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;

        offlinePlayers.add(this);
    }

    public SimpleOfflinePlayer(Player player) {
        this(player.getName(), player.getUniqueId());
    }

    public static void save(JavaPlugin main) throws IOException {
        if (!(offlinePlayers.isEmpty())) {
            File file = new File(main.getDataFolder(), "offlineplayers.yml");

            if (!(file.exists())) {
                file.createNewFile();
            }

            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            for (SimpleOfflinePlayer offlinePlayer : offlinePlayers) {
                configuration.set(offlinePlayer.getUuid().toString() + ".NAME", offlinePlayer.getName());
            }
            configuration.save(file);
        }
    }
    public static void load(JavaPlugin main) {
        File file = new File(main.getDataFolder(), "offlineplayers.yml");
        if (file.exists()) {
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            for (String key : configuration.getKeys(false)) {
                UUID uuid = UUID.fromString(key);
                String name = configuration.getString(key + ".NAME");
                getOfflinePlayers().add(new SimpleOfflinePlayer(name, uuid));
            }
        }
    }

    public static SimpleOfflinePlayer getByUuid(UUID uuid) {
        for (SimpleOfflinePlayer offlinePlayer : getOfflinePlayers()) {
            if (offlinePlayer.getUuid().equals(uuid)) {
                return offlinePlayer;
            }
        }
        return null;
    }

    public static SimpleOfflinePlayer getByName(String name) {
        for (SimpleOfflinePlayer offlinePlayer : getOfflinePlayers()) {
            if (offlinePlayer.getName().equals(name)) {
                return offlinePlayer;
            }
        }
        return null;
    }

    public static Set<SimpleOfflinePlayer> getOfflinePlayers() {
        return offlinePlayers;
    }
}
