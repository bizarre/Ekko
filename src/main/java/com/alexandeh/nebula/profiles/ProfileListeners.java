package com.alexandeh.nebula.profiles;

import com.alexandeh.nebula.utils.player.SimpleOfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class ProfileListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (SimpleOfflinePlayer.getByUuid(player.getUniqueId()) == null) {
            new SimpleOfflinePlayer(player);
        }
    }
}