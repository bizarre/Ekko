package com.alexandeh.nebula.factions.events.player;

import com.alexandeh.nebula.factions.events.FactionEvent;
import com.alexandeh.nebula.factions.type.PlayerFaction;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
@Getter
public class PlayerLeaveFactionEvent extends FactionEvent {

    private PlayerFaction faction;
    private Player player;

    public PlayerLeaveFactionEvent(Player player, PlayerFaction faction) {
        this.player = player;
        this.faction = faction;
    }

}
