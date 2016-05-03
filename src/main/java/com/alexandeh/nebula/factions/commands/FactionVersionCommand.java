package com.alexandeh.nebula.factions.commands;

import com.alexandeh.nebula.utils.command.Command;
import com.alexandeh.nebula.utils.command.CommandArgs;
import org.bukkit.ChatColor;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionVersionCommand extends FactionCommand {
    @Command(name = "f.ver", aliases = {"faction.ver", "f.version", "factions.ver", "faction.version", "factions.version"}, description = "Base command for all faction-related functions.", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        command.getSender().sendMessage(ChatColor.GOLD + "Nebula v" + main.getDescription().getVersion() + ChatColor.YELLOW + " created by " + ChatColor.GOLD + "Alexandeh" + ChatColor.YELLOW + ".");
        command.getSender().sendMessage(ChatColor.YELLOW + "View source at " + ChatColor.GOLD + "https://www.github.com/Alexandeh/Nebula" + ChatColor.YELLOW + ".");
    }
}
