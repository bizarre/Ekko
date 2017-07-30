package com.bizarrealex.ekko.factions.commands;

import com.bizarrealex.ekko.Ekko;
import com.bizarrealex.ekko.factions.Faction;
import com.bizarrealex.ekko.factions.events.player.PlayerCreateFactionEvent;
import com.bizarrealex.ekko.factions.type.PlayerFaction;
import com.bizarrealex.ekko.profiles.Profile;
import com.bizarrealex.ekko.utils.command.Command;
import com.bizarrealex.ekko.utils.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
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

        if (command.getArgs().length == 0) {
            player.sendMessage(langConfig.getString("TOO_FEW_ARGS.CREATE"));
            return;
        }

        Profile profile = Profile.getByUuid(player.getUniqueId());

        if (profile.getFaction() != null) {
            player.sendMessage(langConfig.getString("ERROR.ALREADY_IN_FACTION"));
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < command.getArgs().length; i++) {
            sb.append(command.getArgs()[i]).append(" ");
        }

        String name = sb.toString().trim().replace(" ", "");

        if (name.length() < mainConfig.getInt("FACTION_NAME.MIN_CHARACTERS")) {
            player.sendMessage(langConfig.getString("ERROR.TAG_TOO_SHORT"));
            return;
        }

        if (name.length() > mainConfig.getInt("FACTION_NAME.MAX_CHARACTERS")) {
            player.sendMessage(langConfig.getString("ERROR.TAG_TOO_LONG"));
            return;
        }

        if (!(StringUtils.isAlphanumeric(name))) {
            player.sendMessage(langConfig.getString("ERROR.NOT_ALPHANUMERIC"));
            return;
        }

        for (String string : mainConfig.getStringList("FACTION_NAME.BLOCKED_NAMES")) {
            if (name.contains(string)) {
                player.sendMessage(langConfig.getString("ERROR.BLOCKED_NAME"));
                return;
            }
        }

        if (Faction.getByName(name) != null) {
            player.sendMessage(langConfig.getString("ERROR.NAME_TAKEN"));
            return;
        }

        PlayerFaction playerFaction = new PlayerFaction(name, player.getUniqueId(), null);
        profile.setFaction(playerFaction);

        Ekko.getInstance().getServer().broadcastMessage(langConfig.getString("ANNOUNCEMENTS.FACTION_CREATED").replace("%PLAYER%", player.getName()).replace("%NAME%", name));

        Ekko.getInstance().getServer().getPluginManager().callEvent(new PlayerCreateFactionEvent(player, playerFaction));
    }
}
