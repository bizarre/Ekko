package com.alexandeh.ekko.factions.commands.officer;

import com.alexandeh.ekko.factions.commands.FactionCommand;
import com.alexandeh.ekko.factions.type.PlayerFaction;
import com.alexandeh.ekko.profiles.Profile;
import com.alexandeh.ekko.utils.command.Command;
import com.alexandeh.ekko.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionAnnouncementCommand extends FactionCommand {
    @Command(name = "f.announcement", aliases = {"faction.announcement", "factions.announcement", "f.anouncement", "faction.anouncement", "factions.anouncement"}, inFactionOnly = true, isOfficerOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(langConfig.getString("TOO_FEW_ARGS.ANNOUNCEMENT"));
            return;
        }

        Profile profile = Profile.getByUuid(player.getUniqueId());
        PlayerFaction playerFaction = profile.getFaction();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < command.getArgs().length; i++) {
            sb.append(command.getArgs()[i]).append(" ");
        }
        String message = sb.toString().trim();

        playerFaction.setAnnouncement(message);
        playerFaction.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION.PLAYER_SET_ANNOUNCEMENT").replace("%PLAYER%", player.getName()).replace("%MESSAGE%", message).replace("%FACTION%", playerFaction.getName()));
    }
}
