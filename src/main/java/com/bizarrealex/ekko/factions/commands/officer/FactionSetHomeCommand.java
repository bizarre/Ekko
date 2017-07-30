package com.bizarrealex.ekko.factions.commands.officer;

import com.bizarrealex.ekko.factions.commands.FactionCommand;
import com.bizarrealex.ekko.factions.type.PlayerFaction;
import com.bizarrealex.ekko.profiles.Profile;
import com.bizarrealex.ekko.utils.LocationSerialization;
import com.bizarrealex.ekko.utils.command.Command;
import com.bizarrealex.ekko.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionSetHomeCommand extends FactionCommand {
    @Command(name = "f.sethome", aliases = {"faction.sethome", "factions.sethome", "factions.sethq", "f.sethq", "faction.sethq"}, inFactionOnly = true, isOfficerOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Profile.getByUuid(player.getUniqueId());
        PlayerFaction playerFaction = profile.getFaction();

        //TODO: check if in own claim...

        playerFaction.setHome(LocationSerialization.serializeLocation(player.getLocation()));

        playerFaction.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION.PLAYER_SET_HOME").replace("%PLAYER%", player.getName()));
    }
}
