package com.alexandeh.ekko.factions.commands.leader;

import com.alexandeh.ekko.factions.Faction;
import com.alexandeh.ekko.factions.commands.FactionCommand;
import com.alexandeh.ekko.factions.events.player.PlayerDisbandFactionEvent;
import com.alexandeh.ekko.factions.type.PlayerFaction;
import com.alexandeh.ekko.profiles.Profile;
import com.alexandeh.ekko.utils.command.Command;
import com.alexandeh.ekko.utils.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionDisbandCommand extends FactionCommand {
    @Command(name = "f.disband", aliases = {"faction.disband", "factions.disband"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = Profile.getByUuid(player.getUniqueId());

        if (profile.getFaction() == null) {
            player.sendMessage(langConfig.getString("ERROR.NOT_IN_FACTION"));
            return;
        }

        PlayerFaction playerFaction = profile.getFaction();

        if (!(playerFaction.getLeader().equals(player.getUniqueId())) && !player.hasPermission("ekko.admin")) {
            player.sendMessage(langConfig.getString("ERROR.NOT_LEADER"));
            return;
        }

        for (UUID member : playerFaction.getAllPlayerUuids()) {
            Profile memberProfile = Profile.getByUuid(member);
            if (memberProfile != null && memberProfile.getFaction().equals(playerFaction)) {
                memberProfile.setFaction(null);
            }
        }

        Bukkit.getPluginManager().callEvent(new PlayerDisbandFactionEvent(player, playerFaction));

        Bukkit.broadcastMessage(langConfig.getString("ANNOUNCEMENTS.FACTION_DISBANDED").replace("%PLAYER%", player.getName()).replace("%NAME%", playerFaction.getName()));
        Faction.getFactions().remove(playerFaction);

        for (PlayerFaction ally : playerFaction.getAllies()) {
            ally.getAllies().remove(playerFaction);
        }
    }
}
