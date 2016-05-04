package com.alexandeh.nebula.factions.commands.leader;

import com.alexandeh.nebula.factions.commands.FactionCommand;
import com.alexandeh.nebula.factions.type.PlayerFaction;
import com.alexandeh.nebula.profiles.Profile;
import com.alexandeh.nebula.utils.command.Command;
import com.alexandeh.nebula.utils.command.CommandArgs;
import com.alexandeh.nebula.utils.player.SimpleOfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionLeaderCommand extends FactionCommand {
    @Command(name = "f.leader", aliases = {"faction.leader", "factions.leader", "f.owner", "factions.owner", "faction.owner", "f.ownership", "factions.ownership", "faction.ownership"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (command.getArgs().length == 0) {
            player.sendMessage(langConfig.getString("TOO_FEW_ARGS.LEADER"));
            return;
        }

        Profile profile = Profile.getByUuid(player.getUniqueId());

        if (profile.getFaction() == null) {
            player.sendMessage(langConfig.getString("ERROR.NOT_IN_FACTION"));
            return;
        }

        PlayerFaction playerFaction = profile.getFaction();

        if (!(playerFaction.getLeader().equals(player.getUniqueId())) && !player.hasPermission("nebula.admin")) {
            player.sendMessage(langConfig.getString("ERROR.NOT_LEADER"));
            return;
        }

        UUID uuid;
        String name;
        Player toLeader = Bukkit.getPlayer(command.getArgs(0));

        if (toLeader == null) {
            SimpleOfflinePlayer offlinePlayer = SimpleOfflinePlayer.getByName(command.getArgs(0));
            if (offlinePlayer != null) {
                uuid = offlinePlayer.getUuid();
                name = offlinePlayer.getName();
            } else {
                player.sendMessage(langConfig.getString("ERROR.NOT_ONLINE").replace("%PLAYER%", command.getArgs(0)));
                return;
            }
        } else {
            uuid = toLeader.getUniqueId();
            name = toLeader.getName();
        }

        if (!playerFaction.getAllPlayerUuids().contains(uuid)) {
            player.sendMessage(langConfig.getString("ERROR.NOT_IN_YOUR_FACTION"));
            return;
        }

        if (player.getUniqueId().equals(playerFaction.getLeader()) && uuid.equals(playerFaction.getLeader())) {
            player.sendMessage(langConfig.getString("ERROR.ALREADY_LEADER"));
            return;
        }

        if (uuid.equals(playerFaction.getLeader()) && !uuid.equals(player.getUniqueId())) {
            player.sendMessage(langConfig.getString("ERROR.PLAYER_ALREADY_LEADER").replace("%PLAYER%", name));
            return;
        }

        playerFaction.getMembers().remove(uuid);
        playerFaction.getOfficers().remove(uuid);

        playerFaction.getOfficers().add(player.getUniqueId());
        playerFaction.setLeader(uuid);

        playerFaction.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION.PLAYER_TRANSFER_LEADERSHIP").replace("%PLAYER%", name).replace("%LEADER%", player.getName()));
    }
}
