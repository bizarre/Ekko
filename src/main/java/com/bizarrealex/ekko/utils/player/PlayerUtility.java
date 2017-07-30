package com.bizarrealex.ekko.utils.player;

import com.bizarrealex.ekko.Ekko;
import org.bukkit.entity.Player;

import java.util.Collection;

public class PlayerUtility {

    public static Collection<? extends Player> getOnlinePlayers() {
        return Ekko.getInstance().getServer().getOnlinePlayers();
    }
}
