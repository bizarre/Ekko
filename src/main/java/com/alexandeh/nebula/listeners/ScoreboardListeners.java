package com.alexandeh.nebula.listeners;

import com.alexandeh.nebula.factions.events.FactionAllyEvent;
import com.alexandeh.nebula.factions.events.FactionEnemyEvent;
import com.alexandeh.nebula.factions.events.player.PlayerDisbandFactionEvent;
import com.alexandeh.nebula.factions.events.player.PlayerJoinFactionEvent;
import com.alexandeh.nebula.factions.events.player.PlayerLeaveFactionEvent;
import com.alexandeh.nebula.factions.type.PlayerFaction;
import com.alexandeh.nebula.profiles.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class ScoreboardListeners implements Listener {

    @EventHandler
    public void onJoinFaction(PlayerJoinFactionEvent event) {
        for (Player player : event.getFaction().getOnlinePlayers()) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            profile.updateTab();
        }
        for (PlayerFaction ally : event.getFaction().getAllies()) {
            for (Player allyPlayer : ally.getOnlinePlayers()) {
                Profile profile = Profile.getByUuid(allyPlayer.getUniqueId());
                profile.updateTab();
            }
        }
    }

    @EventHandler
    public void onLeaveFaction(PlayerLeaveFactionEvent event) {
        Set<Player> toLoop = new HashSet<>(event.getFaction().getOnlinePlayers());
        toLoop.add(event.getPlayer());
        for (Player player : toLoop) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            profile.updateTab();
        }
        for (PlayerFaction ally : event.getFaction().getAllies()) {
            for (Player allyPlayer : ally.getOnlinePlayers()) {
                Profile profile = Profile.getByUuid(allyPlayer.getUniqueId());
                profile.updateTab();
            }
        }
    }

    @EventHandler
    public void onDisbandFaction(PlayerDisbandFactionEvent event) {
        for (Player player : event.getFaction().getOnlinePlayers()) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            profile.updateTab();
        }
        for (PlayerFaction ally : event.getFaction().getAllies()) {
            for (Player allyPlayer : ally.getOnlinePlayers()) {
                Profile profile = Profile.getByUuid(allyPlayer.getUniqueId());
                profile.updateTab();
            }
        }
    }


    @EventHandler
    public void onAllyFaction(FactionAllyEvent event) {
        for (PlayerFaction faction : event.getFactions()) {
            for (Player player : faction.getOnlinePlayers()) {
                Profile profile = Profile.getByUuid(player.getUniqueId());
                profile.updateTab();
            }
        }
    }

    @EventHandler
    public void onEnemyFaction(FactionEnemyEvent event) {
        for (PlayerFaction faction : event.getFactions()) {
            for (Player player : faction.getOnlinePlayers()) {
                Profile profile = Profile.getByUuid(player.getUniqueId());
                profile.updateTab();
            }
        }
    }

}
