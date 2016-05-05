package com.alexandeh.ekko.factions.events.player;

import com.alexandeh.ekko.factions.events.FactionEvent;
import com.alexandeh.ekko.factions.type.PlayerFaction;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
@Getter
public class PlayerDisbandFactionEvent extends FactionEvent {

    private PlayerFaction faction;
    private Player player;

    public PlayerDisbandFactionEvent(Player player, PlayerFaction faction) {
        this.player = player;
        this.faction = faction;
    }

}
