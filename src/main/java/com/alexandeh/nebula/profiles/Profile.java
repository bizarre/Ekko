package com.alexandeh.nebula.profiles;

import com.alexandeh.nebula.Nebula;
import com.alexandeh.nebula.factions.type.PlayerFaction;
import com.alexandeh.nebula.files.ConfigFile;
import com.alexandeh.nebula.utils.player.PlayerUtility;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;
import java.util.Set;
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
@Setter
public class Profile {


    private static Set<Profile> profiles = new HashSet<>();

    private PlayerFaction faction;
    private ProfileChatType chatType;
    private int kills;
    private int deaths;
    private UUID uuid;

    public Profile(UUID uuid) {
        this.uuid = uuid;

        chatType = ProfileChatType.PUBLIC;

        profiles.add(this);
    }

    public Profile(Player player) {
        this(player.getUniqueId());
    }

    public void updateTab() {
        Player player = Bukkit.getPlayer(uuid);

        if (player != null) {

            Scoreboard scoreboard;
            boolean newScoreboard = false;

            if (player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard()) {
                scoreboard = player.getScoreboard();
            } else {
                scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                newScoreboard = true;
            }

            Nebula nebula = Nebula.getInstance();
            ConfigFile mainConfig = nebula.getMainConfig();

            if (mainConfig.getBoolean("TAB_LIST.ENABLED")) {
                Team friendly = getExistingOrCreateNewTeam("friendly", scoreboard, ChatColor.valueOf(mainConfig.getString("TAB_LIST.FRIENDLY_COLOR")));
                Team ally = getExistingOrCreateNewTeam("ally", scoreboard, ChatColor.valueOf(mainConfig.getString("TAB_LIST.ALLY_COLOR")));
                Team enemy = getExistingOrCreateNewTeam("enemy", scoreboard, ChatColor.valueOf(mainConfig.getString("TAB_LIST.ENEMY_COLOR")));


                if (faction != null) {

                    for (Player friendlyPlayer : faction.getOnlinePlayers()) {
                        if (!(friendly.getEntries().contains(friendlyPlayer.getName()))) {
                            friendly.addEntry(friendlyPlayer.getName());
                        }
                    }

                    for (PlayerFaction allyFaction : faction.getAllies()) {
                        for (Player allyPlayer : allyFaction.getOnlinePlayers()) {
                            if (!(ally.getEntries().contains(allyPlayer.getName()))) {
                                ally.addEntry(allyPlayer.getName());
                            }
                        }
                    }

                }

                for (Player enemyPlayer : PlayerUtility.getOnlinePlayers()) {
                    if (!(enemyPlayer.getName().equals(player.getName()))) {

                        if (friendly.getEntries().contains(enemyPlayer.getName()) && (faction == null || !faction.getOnlinePlayers().contains(enemyPlayer))) {
                            friendly.removeEntry(enemyPlayer.getName());
                        }

                        if (ally.getEntries().contains(enemyPlayer.getName())) {
                            Profile enemyProfile = Profile.getByUuid(enemyPlayer.getUniqueId());
                            PlayerFaction enemyFaction = enemyProfile.getFaction();
                            if (enemyFaction == null || faction == null || !faction.getAllies().contains(enemyFaction)) {
                                ally.removeEntry(enemyPlayer.getName());
                            }
                        }

                        if (!(friendly.getEntries().contains(enemyPlayer.getName())) && (!(ally.getEntries().contains(enemyPlayer.getName())))) {
                            enemy.addEntry(enemyPlayer.getName());
                        }
                    }
                }

                if (!(friendly.getEntries().contains(player.getName()))) {
                    friendly.addEntry(player.getName());
                }

                if (newScoreboard) {
                    player.setScoreboard(scoreboard);
                }
            }
        }
    }

    private Team getExistingOrCreateNewTeam(String string, Scoreboard scoreboard, ChatColor prefix) {
        Team toReturn = scoreboard.getTeam(string);

        if (toReturn == null) {
            toReturn = scoreboard.registerNewTeam(string);
            toReturn.setPrefix(prefix + "");
        }

        return toReturn;
    }

    public static void sendTabUpdate() {
        for (Player player : PlayerUtility.getOnlinePlayers()) {
            getByUuid(player.getUniqueId()).updateTab();
        }
    }

    public static Profile getByUuid(UUID uuid) {
        for (Profile profile : getProfiles()) {
            if (profile.getUuid().equals(uuid)) {
                return profile;
            }
        }
        return new Profile(uuid);
    }

    public static Set<Profile> getProfiles() {
        return profiles;
    }
}
