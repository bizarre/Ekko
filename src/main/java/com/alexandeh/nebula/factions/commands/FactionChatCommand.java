package com.alexandeh.nebula.factions.commands;

import com.alexandeh.nebula.profiles.Profile;
import com.alexandeh.nebula.profiles.ProfileChatType;
import com.alexandeh.nebula.utils.command.Command;
import com.alexandeh.nebula.utils.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionChatCommand extends FactionCommand {
    @Command(name = "f.c", aliases = {"faction.c", "factions.c", "factions.chat", "f.chat", "faction.chat"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        String[] args = command.getArgs();

        List<String> chatTypes = new ArrayList<>(Arrays.asList(
                "a", "ally", "f", "faction", "p", "public"
        ));

        Profile profile = Profile.getByUuid(player.getUniqueId());

        if (args.length == 0 || (!chatTypes.contains(args[0]))) {
            ProfileChatType toToggle = getToToggle(profile);

            if (toToggle != ProfileChatType.PUBLIC && profile.getFaction() == null) {
                player.sendMessage(langConfig.getString("ERROR.MUST_BE_IN_FACTION_FOR_CHAT_TYPE"));
                return;
            }

            setChatType(player, toToggle);
            return;
        }

        String arg = args[0];
        if (arg.equalsIgnoreCase("public") || arg.equalsIgnoreCase("p")) {
            setChatType(player, ProfileChatType.PUBLIC);
            return;
        }

        if (profile.getFaction() == null) {
            player.sendMessage(langConfig.getString("ERROR.MUST_BE_IN_FACTION_FOR_CHAT_TYPE"));
            return;
        }

        if (arg.equalsIgnoreCase("a") || arg.equalsIgnoreCase("ally") || arg.equalsIgnoreCase("alliance")) {
            setChatType(player, ProfileChatType.ALLY);
            return;
        }

        if (arg.equalsIgnoreCase("f") || arg.equalsIgnoreCase("faction")) {
            setChatType(player, ProfileChatType.FACTION);
        }
    }

    private ProfileChatType getToToggle(Profile profile) {
        switch (profile.getChatType()) {
            case FACTION: return ProfileChatType.ALLY;
            case ALLY: return ProfileChatType.PUBLIC;
            case PUBLIC: return ProfileChatType.FACTION;
        }
        return null;
    }

    private void setChatType(Player player, ProfileChatType type) {
        final String ROOT = "FACTION_OTHER.CHAT_CHANGED.";

        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.setChatType(type);

        switch (type) {
            case PUBLIC: {
                player.sendMessage(langConfig.getString(ROOT + "PUBLIC"));
                break;
            }
            case FACTION: {
                player.sendMessage(langConfig.getString(ROOT + "FACTION"));
                break;
            }
            case ALLY: {
                player.sendMessage(langConfig.getString(ROOT + "ALLY"));
                break;
            }
        }
    }
}