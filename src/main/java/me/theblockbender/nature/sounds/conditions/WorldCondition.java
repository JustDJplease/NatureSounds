/*
 * NatureSounds plugin - Adds ambient sounds to Minecraft.
 * Copyright (C) 2018 Floris Jolink (TheBlockBender / JustDJplease) - All Rights Reserved
 *
 * You are allowed to:
 * - Modify this code, and use it for personal projects. (Private servers, small networks)
 * - Take ideas and / or formats of this plugin and use it for personal projects. (Private servers, small networks)
 *
 * You are NOT allowed to:
 * - Resell the original plugin or a modification of it.
 * - Claim this plugin as your own.
 * - Distribute the source-code or a modification of it without prior consent of the original author.
 *
 */

package me.theblockbender.nature.sounds.conditions;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.List;

public class WorldCondition {
    // -------------------------------------------- //
    // DATA
    // -------------------------------------------- //
    private List<String> worlds;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public WorldCondition(List<String> worlds) {
        this.worlds = worlds;
    }

    // -------------------------------------------- //
    // PARSING DATA
    // -------------------------------------------- //
    public boolean parseWorlds() {
        for (String string : worlds) {
            if (Bukkit.getWorld(string) == null) return false;
        }
        return true;
    }

    // -------------------------------------------- //
    // CONDITION VALIDATOR
    // -------------------------------------------- //
    public boolean isTrue(World world) {
        //noinspection SuspiciousMethodCalls
        return worlds.contains(world);
    }
}
