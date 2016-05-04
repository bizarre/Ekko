package com.alexandeh.nebula.factions.commands.officer;

import com.alexandeh.nebula.factions.Faction;
import com.alexandeh.nebula.factions.commands.FactionCommand;
import com.alexandeh.nebula.factions.events.FactionAllyEvent;
import com.alexandeh.nebula.factions.events.player.PlayerJoinFactionEvent;
import com.alexandeh.nebula.factions.type.PlayerFaction;
import com.alexandeh.nebula.profiles.Profile;
import com.alexandeh.nebula.utils.command.Command;
import com.alexandeh.nebula.utils.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionAllyCommand extends FactionCommand { //Wrote this at school, has not been tested yet.
    @Command(name = "f.ally", aliases = {"faction.ally", "factions.ally", "f.alliance", "factions.alliance", "faction.alliance"})
    public void onCommand(CommandArgs command) {

        if (!(mainConfig.getBoolean("FACTION_ALLIES.ENABLED"))) {
            main.getFramework().unregisterCommands(this);
            return;
        }

        Player player = command.getPlayer();

        if (command.getArgs().length == 0) {
            player.sendMessage(langConfig.getString("TOO_FEW_ARGS.ALLY"));
            return;
        }

        Profile profile = Profile.getByUuid(player.getUniqueId());
        PlayerFaction playerFaction = profile.getFaction();

        if (playerFaction == null) {
            player.sendMessage(langConfig.getString("ERROR.NOT_IN_FACTION"));
            return;
        }

        if (!(playerFaction.getLeader().equals(player.getUniqueId())) && !playerFaction.getOfficers().contains(player.getUniqueId()) && !player.hasPermission("nebula.admin")) {
            player.sendMessage(langConfig.getString("ERROR.NOT_OFFICER_OR_LEADER"));
            return;
        }

        String factionName = command.getArgs(0);
        Faction faction = Faction.getByName(factionName);
        PlayerFaction allyFaction = null;

        if (faction instanceof PlayerFaction) {
            allyFaction = (PlayerFaction) faction;
        }

        if (faction == null || (!(faction instanceof PlayerFaction))) {
            allyFaction = PlayerFaction.getByPlayerName(factionName);

            if (allyFaction == null) {
                player.sendMessage(langConfig.getString("ERROR.NO_FACTIONS_FOUND").replace("%NAME%", factionName));
                return;
            }
        }

        if (playerFaction.getName().equals(allyFaction.getName())) {
            player.sendMessage(langConfig.getString("ERROR.CANT_ALLY_YOURSELF"));
            return;
        }

        if (playerFaction.getRequestedAllies().contains(allyFaction.getUuid())) {
            player.sendMessage(langConfig.getString("ERROR.ALREADY_REQUESTED").replace("%NAME%", allyFaction.getName()));
            return;
        }

        if (allyFaction.getAllies().size() > mainConfig.getInt("FACTION_ALLIES.MAX_ALLIES")) {
            player.sendMessage(langConfig.getString("ERROR.MAX_ALLIES").replace("%NAME%", allyFaction.getName()));
            return;
        }

        if (allyFaction.getRequestedAllies().contains(playerFaction.getUuid())) {
            allyFaction.getRequestedAllies().remove(playerFaction.getUuid());

            allyFaction.getAllies().add(playerFaction);
            playerFaction.getAllies().add(allyFaction);

            allyFaction.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION_ALLIED").replace("%FACTION%", playerFaction.getName()));
            playerFaction.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION_ALLIED").replace("%FACTION%", playerFaction.getName()));

            Bukkit.getPluginManager().callEvent(new FactionAllyEvent(new PlayerFaction[]{playerFaction, allyFaction}));
        } else {
            playerFaction.getRequestedAllies().add(allyFaction.getUuid());
            playerFaction.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION.PLAYER_SEND_ALLY_REQUEST").replace("%PLAYER%", player.getName()).replace("%FACTION%", allyFaction.getName()));
            allyFaction.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION_RECEIVE_ALLY_REQUEST").replace("%FACTION%", playerFaction.getName()));
        }
    }
}
