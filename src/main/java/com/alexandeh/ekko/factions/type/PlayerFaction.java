package com.alexandeh.ekko.factions.type;

import com.alexandeh.ekko.Ekko;
import com.alexandeh.ekko.factions.Faction;
import com.alexandeh.ekko.utils.player.SimpleOfflinePlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.util.*;

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
@Setter
public class PlayerFaction extends Faction {

    private UUID leader;
    private Set<UUID> officers, members;
    private Set<PlayerFaction> allies;
    private Set<UUID> requestedAllies;
    private BigDecimal deathsTillRaidable;
    private String announcement;
    private int[] freezeInformation;
    private Map<UUID, UUID> invitedPlayers;
    private int balance;

    public PlayerFaction(String name, UUID leader, UUID uuid) {
        super(name, uuid);

        this.leader = leader;

        officers = new HashSet<>();
        members = new HashSet<>();
        invitedPlayers = new HashMap<>();
        deathsTillRaidable = BigDecimal.valueOf(mainConfig.getDouble("FACTION_GENERAL.STARTING_DTR"));
        requestedAllies = new HashSet<>();
        allies = new HashSet<>();
    }

    public boolean isRaidable() {
        return getDeathsTillRaidable().doubleValue() <= 0;
    }

    public boolean isFrozen() {
        return freezeInformation != null;
    }

    public void freeze(int duration) {
        freezeInformation = new int[]{duration, (int) (System.currentTimeMillis() / 1000)};
    }

    public BigDecimal getMaxDeathsTillRaidable() {
        return BigDecimal.valueOf(mainConfig.getDouble("FACTION_GENERAL.STARTING_DTR") + mainConfig.getDouble("FACTION_GENERAL.DTR_PER_PLAYER") * getAllPlayerUuids().size());
    }

    public List<UUID> getAllPlayerUuids() {
        List<UUID> toReturn = new ArrayList<>();

        toReturn.add(leader);
        toReturn.addAll(officers);
        toReturn.addAll(members);

        return toReturn;
    }

    public void sendMessage(String message) {
        for (Player player : getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    public Set<Player> getOnlinePlayers() {
        Set<Player> toReturn = new HashSet<>();
        for (UUID uuid : getAllPlayerUuids()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                toReturn.add(player);
            }
        }
        return toReturn;
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

    public static PlayerFaction getByPlayerName(String name) {
        for (Faction faction : Faction.getFactions()) {
            if (faction instanceof PlayerFaction) { //TODO: Remove if no other faction type added...
                PlayerFaction playerFaction = (PlayerFaction) faction;
                for (UUID uuid : playerFaction.getAllPlayerUuids()) {
                    SimpleOfflinePlayer offlinePlayer = SimpleOfflinePlayer.getByUuid(uuid);
                    if (offlinePlayer != null) {
                        if (offlinePlayer.getName().equalsIgnoreCase(name)) {
                            return playerFaction;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Faction getAnyByString(String factionName) {
        Faction faction = Faction.getByName(factionName);

        if (faction == null) {
            faction = PlayerFaction.getByPlayerName(factionName);

            if (faction == null) {
                return null;
            }
        }
        return faction;
    }

    public static Set<PlayerFaction> getPlayerFactions() {
        Set<PlayerFaction> toReturn = new HashSet<>();
        for (Faction faction : getFactions()) {
            if (faction instanceof PlayerFaction) {
                toReturn.add((PlayerFaction) faction);
            }
        }
        return toReturn;
    }

    public static void runTasks(Ekko ekko) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (PlayerFaction playerFaction : PlayerFaction.getPlayerFactions()) {
                    if (playerFaction.isFrozen()) {
                        if (System.currentTimeMillis() / 1000 - playerFaction.getFreezeInformation()[1] >= playerFaction.getFreezeInformation()[0]) {
                            playerFaction.setFreezeInformation(null);
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(ekko, 20L, 20L);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (PlayerFaction playerFaction : PlayerFaction.getPlayerFactions()) {
                    if (!(playerFaction.isFrozen()) && playerFaction.getDeathsTillRaidable().doubleValue() < playerFaction.getMaxDeathsTillRaidable().doubleValue()) {
                        playerFaction.setDeathsTillRaidable(playerFaction.getDeathsTillRaidable().add(BigDecimal.valueOf(0.1)));
                    }
                }
            }
        }.runTaskTimerAsynchronously(ekko, ekko.getMainConfig().getInt("FACTION_GENERAL.REGEN_DELAY") * 20, ekko.getMainConfig().getInt("FACTION_GENERAL.REGEN_DELAY") * 20);
    }
}
