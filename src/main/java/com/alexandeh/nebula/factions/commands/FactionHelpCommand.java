package com.alexandeh.nebula.factions.commands;

import com.alexandeh.nebula.factions.FactionCommand;
import com.alexandeh.nebula.utils.command.Command;
import com.alexandeh.nebula.utils.command.CommandArgs;
import org.apache.commons.lang.math.NumberUtils;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionHelpCommand extends FactionCommand {
    @Command(name = "faction", aliases = {"f", "factions"}, description = "Base command for all faction-related functions.")
    public void onCommand(CommandArgs command) {
        String[] args = command.getArgs();

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("help")) {
                if (NumberUtils.isNumber(args[1])) {
                    int page = Integer.parseInt(args[1]);
                    if (page > 0 && page < 5) {
                        for (String msg : langConfigCache.getHelp().get(page - 1)) {
                            command.getSender().sendMessage(msg);
                        }
                        return;
                    }
                }
            }
        }

        for (String msg : langConfigCache.getHelp().get(0)) {
            command.getSender().sendMessage(msg);
        }
    }
}
