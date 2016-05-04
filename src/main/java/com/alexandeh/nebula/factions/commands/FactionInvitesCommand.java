package com.alexandeh.nebula.factions.commands;

import com.alexandeh.nebula.factions.type.PlayerFaction;
import com.alexandeh.nebula.profiles.Profile;
import com.alexandeh.nebula.utils.command.Command;
import com.alexandeh.nebula.utils.command.CommandArgs;
import com.alexandeh.nebula.utils.player.SimpleOfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionInvitesCommand extends FactionCommand {
    @Command(name = "f.invites", aliases = {"faction.invites", "factions.invites"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String invites = "";
        HashSet<PlayerFaction> factionsInvitedTo = new HashSet<>();
        Profile profile = Profile.getByUuid(player.getUniqueId());

        for (PlayerFaction playerFaction : PlayerFaction.getPlayerFactions()) {
            if (playerFaction.getInvitedPlayers().containsKey(player.getUniqueId())) {
                factionsInvitedTo.add(playerFaction);
            }
        }

        String splitter = langConfig.getString("FACTION_OTHER.INVITES.SPLITTER");

        if (factionsInvitedTo.isEmpty()) {
            invites = langConfig.getString("FACTION_OTHER.INVITES.PLAYER_INVITES_PLACEHOLDER");
        } else {
            for (PlayerFaction playerFaction : factionsInvitedTo) {
                invites = playerFaction.getName() + splitter;
            }
            invites = invites.substring(0, invites.lastIndexOf(splitter));
        }

        player.sendMessage(langConfig.getString("FACTION_OTHER.INVITES.PLAYER_INVITES").replace("%INVITES%", invites));

        if (profile.getFaction() != null) {
            PlayerFaction playerFaction = profile.getFaction();

            if (!playerFaction.getInvitedPlayers().isEmpty()) {
                String invitedPlayers = "";

                for (UUID invitedPlayer : playerFaction.getInvitedPlayers().keySet()) {
                    SimpleOfflinePlayer offlinePlayer = SimpleOfflinePlayer.getByUuid(invitedPlayer);
                    if (offlinePlayer != null) {
                        invitedPlayers = invitedPlayers + offlinePlayer.getName() + splitter;
                    }
                }

                invitedPlayers = invitedPlayers.substring(0, invitedPlayers.lastIndexOf(splitter));
                player.sendMessage(langConfig.getString("FACTION_OTHER.INVITES.FACTION_INVITES").replace("%INVITES%", invitedPlayers));
            }
        }
    }
}
