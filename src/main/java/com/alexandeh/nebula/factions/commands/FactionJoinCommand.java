package com.alexandeh.nebula.factions.commands;

import com.alexandeh.nebula.factions.Faction;
import com.alexandeh.nebula.factions.FactionCommand;
import com.alexandeh.nebula.factions.type.PlayerFaction;
import com.alexandeh.nebula.profiles.Profile;
import com.alexandeh.nebula.utils.command.Command;
import com.alexandeh.nebula.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionJoinCommand extends FactionCommand {
    @Command(name = "f.join", aliases = {"faction.join", "factions.join", "f.accept", "factions.accept", "faction.accept"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (command.getArgs().length < 1) {
            player.sendMessage(langConfig.getString("TOO_FEW_ARGS.JOIN"));
            return;
        }

        Profile profile = Profile.getByUuid(player.getUniqueId());

        if (profile.getFaction() != null) {
            player.sendMessage(langConfig.getString("ERROR.ALREADY_IN_FACTION"));
            return;
        }

        String factionName = command.getArgs(0);
        Faction faction = Faction.getByName(factionName);
        PlayerFaction playerFaction;

        if (faction == null || !(faction instanceof PlayerFaction)) {
            return;
        }

        playerFaction = (PlayerFaction) faction;

        if (!(playerFaction.getInvitedPlayers().containsKey(player.getUniqueId()))) {
            playerFaction = PlayerFaction.getByPlayerName(factionName);

            if (playerFaction == null || !(playerFaction.getInvitedPlayers().containsKey(player.getUniqueId()))) {
                player.sendMessage(langConfig.getString("ERROR.NOT_INVITED"));
                return;
            }
        }

        player.sendMessage(langConfig.getString("FACTION_OTHER.JOINED").replace("%FACTION%", playerFaction.getName()));
        playerFaction.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION.PLAYER_JOINED").replace("%PLAYER%", player.getName()));

        playerFaction.getInvitedPlayers().remove(player.getUniqueId());
        playerFaction.getMembers().add(player.getUniqueId());
        profile.setFaction(playerFaction);
    }


}
