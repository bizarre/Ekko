package com.alexandeh.ekko.factions.commands.officer;

import com.alexandeh.ekko.factions.Faction;
import com.alexandeh.ekko.factions.commands.FactionCommand;
import com.alexandeh.ekko.factions.events.FactionAllyEvent;
import com.alexandeh.ekko.factions.type.PlayerFaction;
import com.alexandeh.ekko.profiles.Profile;
import com.alexandeh.ekko.utils.command.Command;
import com.alexandeh.ekko.utils.command.CommandArgs;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionAllyCommand extends FactionCommand {

    public FactionAllyCommand(){
        if (!(mainConfig.getBoolean("FACTION_GENERAL.ALLIES.ENABLED"))) {
            main.getFramework().unregisterCommands(this);
        }
    }

    @Command(name = "f.ally", aliases = {"faction.ally", "factions.ally", "f.alliance", "factions.alliance", "faction.alliance"})
    public void onCommand(CommandArgs command) {
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

        if (!(playerFaction.getLeader().equals(player.getUniqueId())) && !playerFaction.getOfficers().contains(player.getUniqueId()) && !player.hasPermission("ekko.admin")) {
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

        if (allyFaction.getAllies().contains(playerFaction)) {
            player.sendMessage(langConfig.getString("ERROR.ALREADY_HAVE_RELATION").replace("%FACTION%", allyFaction.getName()));
            return;
        }

        if (playerFaction.getRequestedAllies().contains(allyFaction.getUuid())) {
            player.sendMessage(langConfig.getString("ERROR.ALREADY_REQUESTED").replace("%FACTION%", allyFaction.getName()));
            return;
        }

        if (allyFaction.getAllies().size() >= mainConfig.getInt("FACTION_GENERAL.ALLIES.MAX_ALLIES")) {
            player.sendMessage(langConfig.getString("ERROR.MAX_ALLIES").replace("%FACTION%", allyFaction.getName()));
            return;
        }

        if (allyFaction.getRequestedAllies().contains(playerFaction.getUuid())) {
            allyFaction.getRequestedAllies().remove(playerFaction.getUuid());

            allyFaction.getAllies().add(playerFaction);
            playerFaction.getAllies().add(allyFaction);

            allyFaction.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION_ALLIED").replace("%FACTION%", playerFaction.getName()));
            playerFaction.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION_ALLIED").replace("%FACTION%", allyFaction.getName()));

            Bukkit.getPluginManager().callEvent(new FactionAllyEvent(new PlayerFaction[]{playerFaction, allyFaction}));
        } else {
            playerFaction.getRequestedAllies().add(allyFaction.getUuid());
            playerFaction.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION.PLAYER_SEND_ALLY_REQUEST").replace("%PLAYER%", player.getName()).replace("%FACTION%", allyFaction.getName()));
            for (Player allyPlayer : allyFaction.getOnlinePlayers()) {
                if (allyPlayer.getUniqueId().equals(allyFaction.getLeader())) {
                    new FancyMessage()
                            .text(langConfig.getString("ANNOUNCEMENTS.FACTION_RECEIVE_ALLY_REQUEST").replace("%FACTION%", playerFaction.getName()))
                            .command("/f ally " + playerFaction.getName())
                            .send(allyPlayer);
                } else {
                    allyPlayer.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION_RECEIVE_ALLY_REQUEST").replace("%FACTION%", playerFaction.getName()));
                }
            }
        }
    }
}
