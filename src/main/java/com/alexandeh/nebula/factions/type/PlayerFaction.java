package com.alexandeh.nebula.factions.type;

import com.alexandeh.nebula.factions.Faction;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/*
 * Copyright (c) 2016, Alexander Maxwell. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - The name of Alexander Maxwell may not be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
@Getter
public class PlayerFaction extends Faction {

    private UUID leader;
    private List<UUID> officers;
    private List<UUID> members;
    private BigDecimal deathsTillRaidable;
    private String announcement;
    private int[] freezeInformation;

    public PlayerFaction(String name) {
        super(name);
    }

    public PlayerFaction(String name, UUID uuid) {
        super(name, uuid);
    }

    public static PlayerFaction getByPlayer(Player player) {
        for (Faction faction : Faction.getFactions()) {
            if (faction instanceof PlayerFaction) { //TODO: Remove if no other faction type added...
                PlayerFaction playerFaction = (PlayerFaction) faction;
                if (playerFaction.getLeader().equals(player.getUniqueId()) || playerFaction.getOfficers().contains(player.getUniqueId()) || playerFaction.getMembers().contains(player.getUniqueId())) {
                    return playerFaction;
                }
            }
        }
        return null;
    }
}
