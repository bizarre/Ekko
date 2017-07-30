package com.bizarrealex.ekko.factions.commands.leader;

import com.bizarrealex.ekko.Ekko;
import com.bizarrealex.ekko.factions.commands.FactionCommand;
import com.bizarrealex.ekko.factions.type.PlayerFaction;
import com.bizarrealex.ekko.profiles.Profile;
import com.bizarrealex.ekko.utils.command.Command;
import com.bizarrealex.ekko.utils.command.CommandArgs;
import com.bizarrealex.ekko.utils.player.SimpleOfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionLeaderCommand extends FactionCommand {
    @Command(name = "f.leader", aliases = {"faction.leader", "factions.leader", "f.owner", "factions.owner", "faction.owner", "f.ownership", "factions.ownership", "faction.ownership"}, inFactionOnly = true, isLeaderOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (command.getArgs().length == 0) {
            player.sendMessage(langConfig.getString("TOO_FEW_ARGS.LEADER"));
            return;
        }

        Profile profile = Profile.getByUuid(player.getUniqueId());
        PlayerFaction playerFaction = profile.getFaction();

        UUID uuid;
        String name;
        Player toLeader = Ekko.getInstance().getServer().getPlayer(command.getArgs(0));

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
