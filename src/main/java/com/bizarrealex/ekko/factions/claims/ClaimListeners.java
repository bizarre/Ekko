package com.bizarrealex.ekko.factions.claims;

import com.bizarrealex.ekko.Ekko;
import com.bizarrealex.ekko.factions.Faction;
import com.bizarrealex.ekko.profiles.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class ClaimListeners implements Listener {

    private Ekko main = Ekko.getInstance();
    //It's hard to do listeners at school because I can't test anything..

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem().equals(Faction.getWand(main))) {
            Profile profile = Profile.getByUuid(player.getUniqueId());

            Faction faction;
            if (profile.getFaction() == null) {
                /*if (some boolean) {

                } else {
                    player.getInventory().remove(event.getItem());
                }*/
            } else {
                faction = profile.getFaction();
            }

            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                player.sendMessage("first point set");
                return;
            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                player.sendMessage("second point set");
            }

            if (event.getAction() == Action.LEFT_CLICK_AIR && player.isSneaking()) {
                player.sendMessage("claimed");
            }

            if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                // count to 2;
                player.sendMessage("points reset");
            }

        }
    }

}
