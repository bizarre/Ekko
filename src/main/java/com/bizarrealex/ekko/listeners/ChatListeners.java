package com.bizarrealex.ekko.listeners;

import com.bizarrealex.ekko.Ekko;
import com.bizarrealex.ekko.factions.type.PlayerFaction;
import com.bizarrealex.ekko.files.ConfigFile;
import com.bizarrealex.ekko.profiles.Profile;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class ChatListeners implements Listener {

    private Ekko main = Ekko.getInstance();
    private ConfigFile mainConfig = main.getMainConfig();

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (mainConfig.getBoolean("FACTION_CHAT.ENABLED")) {
            event.setCancelled(true);

            Player player = event.getPlayer();
            Profile profile = Profile.getByUuid(player.getUniqueId());
            PlayerFaction playerFaction = profile.getFaction();

            String actualMessage = event.getFormat().replace("%1$s", player.getDisplayName()).replace("%2$s", event.getMessage());
            Ekko.getInstance().getServer().getLogger().info
                    (ChatColor.stripColor(actualMessage.replace("{FACTION}", "")));

            for (Player recipient : event.getRecipients()) {
                Profile recipientProfile = Profile.getByUuid(recipient.getUniqueId());

                if (playerFaction == null) {
                    recipient.sendMessage(actualMessage.replace("{FACTION}", mainConfig.getString("FACTION_CHAT.NO_FACTION")));
                    continue;
                }

                if (recipientProfile.getFaction() != null) {
                    if (recipientProfile.getFaction().getUuid().equals(playerFaction.getUuid())) {
                        recipient.sendMessage(actualMessage.replace("{FACTION}", mainConfig.getString("FACTION_CHAT.FRIENDLY").replace("%TAG%", playerFaction.getName())));
                        continue;
                    }

                    if (recipientProfile.getFaction().getAllies().contains(playerFaction)) {
                        recipient.sendMessage(actualMessage.replace("{FACTION}", mainConfig.getString("FACTION_CHAT.ALLY").replace("%TAG%", playerFaction.getName())));
                        continue;
                    }
                }

                recipient.sendMessage(actualMessage.replace("{FACTION}", mainConfig.getString("FACTION_CHAT.ENEMY").replace("%TAG%", playerFaction.getName())));
            }
        }
    }

}
