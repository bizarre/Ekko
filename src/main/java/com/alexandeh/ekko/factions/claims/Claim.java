package com.alexandeh.ekko.factions.claims;

import com.alexandeh.ekko.factions.Faction;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
@Getter
public class Claim {

    private static Set<Claim> claims = new HashSet<>();

    private Faction faction;
    private String worldName;
    private int[] coordinates;

    public Claim(Faction faction, int[] coordinates, String worldName) {
        this.faction = faction;
        this.coordinates = coordinates;
        this.worldName = worldName;
    }

    public Claim(Faction faction, int x1, int z1, int x2, int z2, String worldName) {
        this(faction, new int[]{x1, z1, x2, z2}, worldName);
    }

    public int getFirstX() {
        return coordinates[0];
    }

    public int getSecondX() {
        return coordinates[2];
    }

    public int getFirstZ() {
        return coordinates[1];
    }

    public int getSecondZ() {
        return coordinates[3];
    }

    public boolean isInside(Location location) { //bad at math so this probs won't work right out he box, at school though so can't test.
        int minX = Math.min(getFirstX(), getSecondX());
        int maxX = Math.max(getFirstX(), getSecondX());
        int minZ = Math.min(getFirstZ(), getSecondZ());
        int maxZ = Math.max(getFirstZ(), getSecondZ());
        return (location.getWorld().getName().equalsIgnoreCase(worldName) && (location.getBlockX() > minX && location.getBlockX() < maxX) && (location.getBlockZ() > minZ && location.getBlockZ() < maxZ));
    }

    public static Set<Claim> getClaims() {
        return claims;
    }

}
