package com.bizarrealex.ekko.factions.commands.leader;

import com.bizarrealex.ekko.Ekko;
import com.bizarrealex.ekko.factions.Faction;
import com.bizarrealex.ekko.factions.commands.FactionCommand;
import com.bizarrealex.ekko.factions.events.player.PlayerDisbandFactionEvent;
import com.bizarrealex.ekko.factions.type.PlayerFaction;
import com.bizarrealex.ekko.profiles.Profile;
import com.bizarrealex.ekko.utils.command.Command;
import com.bizarrealex.ekko.utils.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionDisbandCommand extends FactionCommand {
    @Command(name = "f.disband", aliases = {"faction.disband", "factions.disband"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = Profile.getByUuid(player.getUniqueId());
        PlayerFaction playerFaction;

        if (command.getArgs().length >= 1 && player.hasPermission("ekko.admin")) {
            String name = command.getArgs(0);
            Faction faction = PlayerFaction.getAnyByString(name);
            if (faction != null) {
                if (faction instanceof PlayerFaction) {
                    playerFaction = (PlayerFaction) faction;
                } else {
                    //TODO: Disband system faction..
                    return;
                }
            } else {
                player.sendMessage(langConfig.getString("ERROR.NO_FACTIONS_FOUND").replace("%NAME%", name));
                return;
            }
        } else {
            playerFaction = profile.getFaction();

            if (playerFaction == null) {
                player.sendMessage(langConfig.getString("ERROR.NOT_IN_FACTION"));
                return;
            }

            if (!playerFaction.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(langConfig.getString("ERROR.NOT_LEADER"));
                return;
            }
        }


        for (UUID member : playerFaction.getAllPlayerUuids()) {
            Profile memberProfile = Profile.getByUuid(member);
            if (memberProfile != null && memberProfile.getFaction().equals(playerFaction)) {
                memberProfile.setFaction(null);
            }
        }

        main.getEconomy().depositPlayer(player, playerFaction.getBalance());

        Ekko.getInstance().getServer().getPluginManager().callEvent(new PlayerDisbandFactionEvent(player, playerFaction));

        Ekko.getInstance().getServer().broadcastMessage(langConfig.getString("ANNOUNCEMENTS.FACTION_DISBANDED").replace("%PLAYER%", player.getName()).replace("%NAME%", playerFaction.getName()));
        Faction.getFactions().remove(playerFaction);

        for (PlayerFaction ally : playerFaction.getAllies()) {
            ally.getAllies().remove(playerFaction);
        }
    }
}
