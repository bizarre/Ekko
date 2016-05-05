package com.alexandeh.ekko.factions.commands;

import com.alexandeh.ekko.factions.Faction;
import com.alexandeh.ekko.factions.type.PlayerFaction;
import com.alexandeh.ekko.profiles.Profile;
import com.alexandeh.ekko.utils.LocationSerialization;
import com.alexandeh.ekko.utils.command.Command;
import com.alexandeh.ekko.utils.command.CommandArgs;
import com.alexandeh.ekko.utils.player.SimpleOfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionShowCommand extends FactionCommand {
    @Command(name = "f.show", aliases = {"faction.show", "factions.show", "f.i", "faction.i", "factions.i", "f.info", "faction.info", "factions.info"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Profile.getByUuid(player.getUniqueId());

        if (command.getArgs().length == 0) {
            PlayerFaction playerFaction = profile.getFaction();

            if (playerFaction == null) {
                player.sendMessage(langConfig.getString("ERROR.NOT_IN_FACTION"));
                return;
            }

            sendFactionInformation(player, playerFaction);
            return;
        }


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < command.getArgs().length; i++) {
            sb.append(command.getArgs()[i]).append(" ");
        }
        String name = sb.toString().trim().replace(" ", "");

        Set<Faction> matchedFactions = Faction.getAllByString(name);

        if (matchedFactions.isEmpty()) {
            player.sendMessage(langConfig.getString("ERROR.NO_FACTIONS_FOUND").replace("%NAME%", name));
            return;
        }

        for (Faction faction : matchedFactions) {
            sendFactionInformation(player, faction);
        }
    }

    private void sendFactionInformation(Player player, Faction faction) { //This method is legit aids, need to fix this when I have time...
        if (faction instanceof PlayerFaction) {

            PlayerFaction playerFaction = (PlayerFaction) faction;
            List<String> toSend = new ArrayList<>();

            final String ROOT = "FACTION_SHOW.PLAYER_FACTION.";
            final String ROOT_SETTINGS = ROOT + "SETTINGS.";

            for (String string : langConfig.getStringList(ROOT + "MESSAGE")) {
                string = string.replace("%FACTION%", faction.getName());
                string = string.replace("%ONLINE_COUNT%", playerFaction.getOnlinePlayers().size() + "");
                string = string.replace("%MAX_COUNT%", playerFaction.getAllPlayerUuids().size() + "");

                if (string.contains("%HOME%")) {
                    if (playerFaction.getHome() == null) {
                        string = string.replace("%HOME%", langConfig.getString(ROOT_SETTINGS + "HOME_PLACEHOLDER"));
                    } else {
                        Location homeLocation = LocationSerialization.deserializeLocation(playerFaction.getHome());
                        string = string.replace("%HOME%", homeLocation.getBlockX() + ", " + homeLocation.getBlockZ());
                    }
                }

                ChatColor offlineColor = ChatColor.valueOf(langConfig.getString(ROOT_SETTINGS + "OFFLINE_COLOR").toUpperCase());
                ChatColor onlineColor = ChatColor.valueOf(langConfig.getString(ROOT_SETTINGS + "ONLINE_COLOR").toUpperCase());
                String killFormat = langConfig.getString(ROOT_SETTINGS + "SHOW_KILLS.FORMAT");
                String splitNamesFormat = langConfig.getString(ROOT_SETTINGS + "SPLIT_NAMES.FORMAT");
                boolean splitNamesEnabled = langConfig.getBoolean(ROOT_SETTINGS + "SPLIT_NAMES.ENABLED");
                boolean killFormatEnabled = langConfig.getBoolean(ROOT_SETTINGS + "SHOW_KILLS.ENABLED");


                if (string.contains("%LEADER%")) { //TODO: Clean this shit up lmao....

                    String leaderString;
                    UUID leader = playerFaction.getLeader();
                    Player leaderPlayer = Bukkit.getPlayer(leader);

                    if (leaderPlayer == null) {
                        SimpleOfflinePlayer offlinePlayer = SimpleOfflinePlayer.getByUuid(leader);

                        if (offlinePlayer == null) {
                            continue;
                        }

                        leaderString = offlineColor + offlinePlayer.getName();
                    } else {
                        leaderString = onlineColor + leaderPlayer.getName();
                    }

                    if (killFormatEnabled) {
                        leaderString = leaderString + killFormat.replace("%KILLS%", Profile.getByUuid(leader).getKills() + "");
                    }

                    string = string.replace("%LEADER%", leaderString);
                }

                if (string.contains("%OFFICERS%")) {
                    String officerString = "";

                    if (playerFaction.getOfficers().isEmpty()) {
                        continue;
                    }

                    for (UUID uuid : playerFaction.getOfficers()) {
                        Player officer = Bukkit.getPlayer(uuid);
                        if (officer == null) {
                            SimpleOfflinePlayer offlinePlayer = SimpleOfflinePlayer.getByUuid(uuid);

                            if (offlinePlayer == null) {
                                continue;
                            }

                            officerString = officerString + offlineColor + offlinePlayer.getName();
                        } else {
                            officerString = officerString + onlineColor + officer.getName();
                        }

                        if (killFormatEnabled) {
                            officerString = officerString + killFormat.replace("%KILLS%", Profile.getByUuid(uuid).getKills() + "");
                        }

                        if (splitNamesEnabled) {
                            officerString = officerString + splitNamesFormat;
                        }
                    }

                    string = string.replace("%OFFICERS%", officerString);
                }

                if (string.contains("%MEMBERS%")) {
                    String memberString = "";

                    if (playerFaction.getMembers().isEmpty()) {
                        continue;
                    }

                    for (UUID uuid : playerFaction.getMembers()) {
                        Player member = Bukkit.getPlayer(uuid);
                        if (member == null) {
                            SimpleOfflinePlayer offlinePlayer = SimpleOfflinePlayer.getByUuid(uuid);

                            if (offlinePlayer == null) {
                                continue;
                            }

                            memberString = memberString + offlineColor + offlinePlayer.getName();
                        } else {
                            memberString = memberString + onlineColor + member.getName();
                        }

                        if (killFormatEnabled) {
                            memberString = memberString + killFormat.replace("%KILLS%", Profile.getByUuid(uuid).getKills() + "");
                        }

                        if (splitNamesEnabled) {
                            memberString = memberString + splitNamesFormat;
                        }
                    }

                    string = string.replace("%MEMBERS%", memberString);
                }

                if (string.contains("%ALLIES%")) {

                    if (playerFaction.getAllies().isEmpty()) {
                        continue;
                    }

                    ChatColor allyColor = ChatColor.valueOf(mainConfig.getString("TAB_LIST.ALLY_COLOR"));
                    String allies = "";
                    for (PlayerFaction ally : playerFaction.getAllies()) {
                        allies = allies + allyColor + ally.getName();

                        if (splitNamesEnabled) {
                            allies = allies + splitNamesFormat;
                        }
                    }

                    string = string.replace("%ALLIES%", allies);
                }

                string = string.replace("%DTR%", playerFaction.getDeathsTillRaidable() + "");

                if (string.contains("%DTR_SYMBOL%")) {
                    if (playerFaction.getDeathsTillRaidable().equals(playerFaction.getMaxDeathsTillRaidable())) {
                        string = string.replace("%DTR_SYMBOL%", langConfig.getString(ROOT_SETTINGS + "DTR_SYMBOL.FULL"));
                    } else {
                        if (playerFaction.isFrozen()) {
                            string = string.replace("%DTR_SYMBOL%", langConfig.getString(ROOT_SETTINGS + "DTR_SYMBOL.FROZEN"));
                        } else {
                            string = string.replace("%DTR_SYMBOL%", langConfig.getString(ROOT_SETTINGS + "DTR_SYMBOL.REGENERATING"));
                        }
                    }
                }

                string = string.replace("%BALANCE%", playerFaction.getBalance() + "");

                if (string.contains("%ANNOUNCEMENT%")) {
                    if (playerFaction.getAnnouncement() == null || !playerFaction.getOnlinePlayers().contains(player)) {
                        continue;
                    }
                    string = string.replace("%ANNOUNCEMENT%", playerFaction.getAnnouncement());
                }

                if (string.contains("%REGEN_TIME%")) {
                    if (!(playerFaction.isFrozen())) {
                        continue;
                    }
                    //TODO: make this do something..
                }

                if (splitNamesEnabled && string.contains(splitNamesFormat)) {
                    string = string.substring(0, string.lastIndexOf(splitNamesFormat));
                }

                toSend.add(string);
            }

            for (String message : toSend) {
                player.sendMessage(message);
            }

        } else {
            //TODO: if make another type i.e system faction...
        }
    }
}
