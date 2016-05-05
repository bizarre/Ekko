package com.alexandeh.ekko.factions.commands.leader;

import com.alexandeh.ekko.factions.commands.FactionCommand;
import com.alexandeh.ekko.factions.type.PlayerFaction;
import com.alexandeh.ekko.profiles.Profile;
import com.alexandeh.ekko.utils.command.Command;
import com.alexandeh.ekko.utils.command.CommandArgs;
import com.alexandeh.ekko.utils.player.SimpleOfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionDemoteCommand extends FactionCommand {
    @Command(name = "f.demote", aliases = {"faction.demote", "factions.demote", "f.unmod", "factions.unmod", "faction.unmod", "f.unofficer", "factions.unofficer", "faction.unofficer", "faction.uncaptain", "f.uncaptain", "faction.uncaptain"}, inFactionOnly = true, isLeaderOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (command.getArgs().length == 0) {
            player.sendMessage(langConfig.getString("TOO_FEW_ARGS.DEMOTE"));
            return;
        }

        Profile profile = Profile.getByUuid(player.getUniqueId());
        PlayerFaction playerFaction = profile.getFaction();

        if (command.getArgs(0).equalsIgnoreCase(player.getName()) && player.getUniqueId().equals(playerFaction.getLeader())) {
            player.sendMessage(langConfig.getString("ERROR.DEMOTE_YOURSELF"));
            return;
        }


        UUID uuid;
        String name;
        Player toDemote = Bukkit.getPlayer(command.getArgs(0));

        if (toDemote == null) {
            SimpleOfflinePlayer offlinePlayer = SimpleOfflinePlayer.getByName(command.getArgs(0));
            if (offlinePlayer != null) {
                uuid = offlinePlayer.getUuid();
                name = offlinePlayer.getName();
            } else {
                player.sendMessage(langConfig.getString("ERROR.NOT_ONLINE").replace("%PLAYER%", command.getArgs(0)));
                return;
            }
        } else {
            uuid = toDemote.getUniqueId();
            name = toDemote.getName();
        }

        if (!playerFaction.getAllPlayerUuids().contains(uuid)) {
            player.sendMessage(langConfig.getString("ERROR.NOT_IN_YOUR_FACTION").replace("%PLAYER%", name));
            return;
        }
        
        if (!playerFaction.getOfficers().contains(uuid)) {
            player.sendMessage(langConfig.getString("ERROR.NOT_OFFICER").replace("%PLAYER%", name));
            return;
        }

        playerFaction.getOfficers().remove(uuid);
        playerFaction.getMembers().add(uuid);

        playerFaction.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION.PLAYER_DEMOTED").replace("%PLAYER%", name).replace("%LEADER%", player.getName()));
    }
}
