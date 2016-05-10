package com.alexandeh.ekko.factions.commands;

import com.alexandeh.ekko.factions.type.PlayerFaction;
import com.alexandeh.ekko.profiles.Profile;
import com.alexandeh.ekko.utils.command.Command;
import com.alexandeh.ekko.utils.command.CommandArgs;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.entity.Player;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionDepositCommand extends FactionCommand {

    private Economy economy = main.getEconomy();

    @Command(name = "f.deposit", aliases = {"faction.deposit", "factions.deposit", "f.d", "faction.d", "factions.d"}, inFactionOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(langConfig.getString("TOO_FEW_ARGS.DEPOSIT"));
            return;
        }

        int amount;

        if (args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("a")) {
            amount = (int) Math.floor(economy.getBalance(player));
        } else {
            if (!(NumberUtils.isNumber(args[0]))) {
                player.sendMessage(langConfig.getString("ERROR.INVALID_NUMBER").replace("%STRING%", args[0]));
                return;
            }

            amount = (int) Math.floor(Double.valueOf(args[0]));

            if (amount > economy.getBalance(player)) {
                player.sendMessage(langConfig.getString("ERROR.NOT_ENOUGH_MONEY"));
                return;
            }
        }

        if (amount <= 0) {
            player.sendMessage(langConfig.getString("ERROR.INVALID_DEPOSIT_AMOUNT"));
            return;
        }

        economy.withdrawPlayer(player, amount);

        Profile profile = Profile.getByUuid(player.getUniqueId());
        PlayerFaction playerFaction = profile.getFaction();
        playerFaction.setBalance(playerFaction.getBalance() + amount);
        playerFaction.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION.PLAYER_DEPOSIT_MONEY").replace("%PLAYER%", player.getName()).replace("%AMOUNT%", amount + ""));
    }
}
