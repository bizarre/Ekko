package com.bizarrealex.ekko.factions.commands.officer;

import com.bizarrealex.ekko.Ekko;
import com.bizarrealex.ekko.factions.commands.FactionCommand;
import com.bizarrealex.ekko.factions.type.PlayerFaction;
import com.bizarrealex.ekko.profiles.Profile;
import com.bizarrealex.ekko.utils.command.Command;
import com.bizarrealex.ekko.utils.command.CommandArgs;
import com.bizarrealex.ekko.utils.player.SimpleOfflinePlayer;
import mkremins.fanciful.FancyMessage;
import org.bukkit.entity.Player;
import java.util.UUID;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionInviteCommand extends FactionCommand {
    @Command(name = "f.invite", aliases = {"faction.invite", "factions.invite", "f.inv", "factions.inv", "faction.inv"}, inFactionOnly = true, isOfficerOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (command.getArgs().length == 0) {
            player.sendMessage(langConfig.getString("TOO_FEW_ARGS.INVITE"));
            return;
        }

        Profile profile = Profile.getByUuid(player.getUniqueId());
        PlayerFaction playerFaction = profile.getFaction();

        if (command.getArgs(0).equalsIgnoreCase(player.getName())) {
            player.sendMessage(langConfig.getString("ERROR.INVITE_YOURSELF"));
            return;
        }


        UUID uuid;
        String name;
        Player toInvite = Ekko.getInstance().getServer().getPlayer(command.getArgs(0));

        if (toInvite == null) {
            SimpleOfflinePlayer offlinePlayer = SimpleOfflinePlayer.getByName(command.getArgs(0));
            if (offlinePlayer != null) {
                uuid = offlinePlayer.getUuid();
                name = offlinePlayer.getName();
            } else {
                player.sendMessage(langConfig.getString("ERROR.NOT_ONLINE").replace("%PLAYER%", command.getArgs(0)));
                return;
            }
        } else {
            uuid = toInvite.getUniqueId();
            name = toInvite.getName();
        }

        if (playerFaction.getAllPlayerUuids().contains(uuid)) {
            player.sendMessage(langConfig.getString("ERROR.INVITE_MEMBER").replace("%PLAYER%", name));
            return;
        }


        if (playerFaction.getInvitedPlayers().containsKey(uuid)) {
            player.sendMessage(langConfig.getString("ERROR.ALREADY_INVITED").replace("%PLAYER%", name));
            return;
        }

        if (toInvite != null) {
            FancyMessage message = new FancyMessage()
                    .text(langConfig.getString("FACTION_OTHER.INVITED_TO_JOIN").replace("%FACTION%", playerFaction.getName()))
                    .command("/f join " + playerFaction.getName().toLowerCase());
            message.send(toInvite);
        }

        playerFaction.getInvitedPlayers().put(uuid, player.getUniqueId());
        playerFaction.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION.PLAYER_INVITED").replace("%PLAYER%", player.getName()).replace("%INVITED_PLAYER%", name));
    }
}
