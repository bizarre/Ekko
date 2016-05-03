package com.alexandeh.nebula.factions.commands;

import com.alexandeh.nebula.factions.type.PlayerFaction;
import com.alexandeh.nebula.profiles.Profile;
import com.alexandeh.nebula.profiles.ProfileChatType;
import com.alexandeh.nebula.utils.command.Command;
import com.alexandeh.nebula.utils.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionChatCommand extends FactionCommand implements Listener {

    public FactionChatCommand() {
        Bukkit.getPluginManager().registerEvents(this, main);
    }

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
        if (profile.getFaction() == null && profile.getChatType() != ProfileChatType.PUBLIC) {
            return ProfileChatType.PUBLIC;
        }

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

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByUuid(player.getUniqueId());
        PlayerFaction playerFaction = profile.getFaction();

        boolean inFactionChat = profile.getChatType() == ProfileChatType.FACTION;

        if (inFactionChat || profile.getChatType() == ProfileChatType.ALLY) {
            event.setCancelled(true);

            if (playerFaction == null) {
                player.sendMessage(langConfig.getString("ERROR.MUST_BE_IN_FACTION_FOR_CHAT_TYPE"));
                return;
            }

            if (inFactionChat) {
                playerFaction.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION.PLAYER_FACTION_CHAT").replace("%PLAYER%", player.getName()).replace("%MESSAGE%", event.getMessage()).replace("%FACTION%", playerFaction.getName()));
            } else {
                //TODO Send message to allies
                playerFaction.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION.PLAYER_ALLY_CHAT").replace("%PLAYER%", player.getName()).replace("%MESSAGE%", event.getMessage()).replace("%FACTION%", playerFaction.getName()));
            }
        }
    }
}
