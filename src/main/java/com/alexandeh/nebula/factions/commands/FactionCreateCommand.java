package com.alexandeh.nebula.factions.commands;

import com.alexandeh.nebula.factions.FactionCommand;
import com.alexandeh.nebula.factions.type.PlayerFaction;
import com.alexandeh.nebula.profiles.Profile;
import com.alexandeh.nebula.utils.command.Command;
import com.alexandeh.nebula.utils.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionCreateCommand extends FactionCommand {
    @Command(name = "f.create", aliases = {"faction.create", "factions.create"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (command.getArgs().length < 1) {
            player.sendMessage(langConfigCache.getTooFewArguments().get("CREATE"));
            return;
        }

        Profile profile = Profile.getByPlayer(player);

        if (profile.getFaction() != null) {
            player.sendMessage(langConfigCache.getErrors().get("ALREADY_IN_FACTION"));
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < command.getArgs().length; i++) {
            sb.append(command.getArgs()[i]).append(" ");
        }

        String name = sb.toString().trim().replace(" ", "");

        if (name.length() < mainConfigCache.getFactionNameLimit()[0]) {
            player.sendMessage(langConfigCache.getErrors().get("TAG_TOO_SHORT"));
            return;
        }

        if (name.length() > mainConfigCache.getFactionNameLimit()[1]) {
            player.sendMessage(langConfigCache.getErrors().get("TAG_TOO_LONG"));
            return;
        }

        if (!(StringUtils.isAlphanumeric(name))) {
            player.sendMessage(langConfigCache.getErrors().get("NOT_ALPHANUMERIC"));
            return;
        }

        for (String string : mainConfigCache.getBlockedFactionNames()) {
            if (name.contains(string)) {
                player.sendMessage(langConfigCache.getErrors().get("BLOCKED_NAME"));
                return;
            }
        }

        PlayerFaction playerFaction = new PlayerFaction(name);
        profile.setFaction(playerFaction);
        Bukkit.broadcastMessage(langConfigCache.getAnnouncements().get("FACTION_CREATED").replace("%PLAYER%", player.getName()).replace("%NAME%", name));
    }
}
