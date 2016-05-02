package com.alexandeh.nebula.factions.commands;

import com.alexandeh.nebula.factions.Faction;
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
public class FactionRenameCommand extends FactionCommand {
    @Command(name = "f.tag", aliases = {"faction.rag", "factions.tag", "factions.rename", "f.rename", "faction.rename"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (command.getArgs().length < 1) {
            player.sendMessage(langConfig.getString("TOO_FEW_ARGS.RENAME"));
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

        Faction faction = Faction.getByName(name);

        if (faction != null) {
            if (faction.equals(playerFaction)) {
                if (faction.getName().equals(name)) { //allow case changing but not exact duplicates. e.g "Faction" -> "factioN"
                    player.sendMessage(langConfig.getString("ERROR.NAME_TAKEN"));
                    return;
                }
            } else {
                player.sendMessage(langConfig.getString("ERROR.NAME_TAKEN"));
                return;
            }
        }

        Bukkit.broadcastMessage(langConfig.getString("ANNOUNCEMENTS.FACTION_RENAMED").replace("%OLD_NAME%", playerFaction.getName()).replace("%NEW_NAME%", name).replace("%PLAYER%", player.getName()));
        playerFaction.setName(name);
    }
}
