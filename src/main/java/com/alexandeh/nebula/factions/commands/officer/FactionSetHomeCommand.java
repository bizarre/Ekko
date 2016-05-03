package com.alexandeh.nebula.factions.commands.officer;

import com.alexandeh.nebula.factions.commands.FactionCommand;
import com.alexandeh.nebula.factions.type.PlayerFaction;
import com.alexandeh.nebula.profiles.Profile;
import com.alexandeh.nebula.utils.LocationSerialization;
import com.alexandeh.nebula.utils.command.Command;
import com.alexandeh.nebula.utils.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionSetHomeCommand extends FactionCommand {
    @Command(name = "f.sethome", aliases = {"faction.sethome", "factions.sethome", "factions.sethq", "f.sethq", "faction.sethq"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

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

        //TODO: checks to check if in own claim...

        playerFaction.setHome(LocationSerialization.serializeLocation(player.getLocation()));

        Bukkit.broadcastMessage(langConfig.getString("ANNOUNCEMENTS.FACTION_HOME_SET").replace("%PLAYER%", player.getName()));
    }
}
