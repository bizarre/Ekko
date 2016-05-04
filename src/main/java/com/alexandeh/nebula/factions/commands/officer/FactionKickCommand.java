package com.alexandeh.nebula.factions.commands.officer;

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
public class FactionKickCommand extends FactionCommand {
    @Command(name = "f.kick", aliases = {"faction.kick", "factions.kick"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (command.getArgs().length == 0) {
            player.sendMessage(langConfig.getString("TOO_FEW_ARGS.KICK"));
            return;
        }

        Profile profile = Profile.getByUuid(player.getUniqueId());

        if (profile.getFaction() == null) {
            player.sendMessage(langConfig.getString("ERROR.NOT_IN_FACTION"));
            return;
        }

        PlayerFaction playerFaction = profile.getFaction();

        if (!(playerFaction.getLeader().equals(player.getUniqueId())) && !playerFaction.getOfficers().contains(player.getUniqueId()) && !player.hasPermission("nebula.admin")) {
            player.sendMessage(langConfig.getString("ERROR.NOT_OFFICER_OR_LEADER"));
            return;
        }

        if (command.getArgs(0).equalsIgnoreCase(player.getName())) {
            player.sendMessage(langConfig.getString("ERROR.KICK_YOURSELF"));
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

        if (playerFaction.getLeader().equals(uuid)) {
            player.sendMessage(langConfig.getString("ERROR.CANT_KICK_LEADER"));
            return;
        }
        
        if (playerFaction.getOfficers().contains(uuid) && playerFaction.getOfficers().contains(player.getUniqueId())) {
            player.sendMessage(langConfig.getString("ERROR.CANT_KICK_OTHER_OFFICER"));
            return;
        }

        playerFaction.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION.PLAYER_KICKED").replace("%KICKED_PLAYER%", name).replace("%PLAYER%", player.getName()));

        Profile kickProfile = Profile.getByUuid(uuid);
        kickProfile.setFaction(null);
        playerFaction.getOfficers().remove(uuid);
        playerFaction.getMembers().remove(uuid);
    }
}
