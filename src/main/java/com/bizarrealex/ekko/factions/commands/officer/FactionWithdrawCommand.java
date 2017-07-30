package com.bizarrealex.ekko.factions.commands.officer;

import com.bizarrealex.ekko.factions.commands.FactionCommand;
import com.bizarrealex.ekko.factions.type.PlayerFaction;
import com.bizarrealex.ekko.profiles.Profile;
import com.bizarrealex.ekko.utils.command.Command;
import com.bizarrealex.ekko.utils.command.CommandArgs;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.entity.Player;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionWithdrawCommand extends FactionCommand {

    private Economy economy = main.getEconomy();

    @Command(name = "f.withdraw", aliases = {"faction.withdraw", "factions.withdraw", "f.w", "faction.w", "factions.w"}, inFactionOnly = true, isOfficerOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(langConfig.getString("TOO_FEW_ARGS.WITHDRAW"));
            return;
        }

        Profile profile = Profile.getByUuid(player.getUniqueId());
        PlayerFaction playerFaction = profile.getFaction();
        int amount;

        if (args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("a")) {
            amount = playerFaction.getBalance();
        } else {
            if (!(NumberUtils.isNumber(args[0]))) {
                player.sendMessage(langConfig.getString("ERROR.INVALID_NUMBER").replace("%STRING%", args[0]));
                return;
            }

            amount = (int) Math.floor(Double.valueOf(args[0]));

            if (amount > playerFaction.getBalance()) {
                player.sendMessage(langConfig.getString("ERROR.FACTION_NOT_ENOUGH_MONEY"));
                return;
            }
        }

        if (amount <= 0) {
            player.sendMessage(langConfig.getString("ERROR.INVALID_WITHDRAW_AMOUNT"));
            return;
        }

        economy.depositPlayer(player, amount);

        playerFaction.setBalance(playerFaction.getBalance() - amount);
        playerFaction.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION.PLAYER_WITHDRAW_MONEY").replace("%PLAYER%", player.getName()).replace("%AMOUNT%", amount + ""));
    }
}
