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

import me.theblockbender.nature.sounds.NatureSounds;
import org.bukkit.Location;
import org.bukkit.block.Biome;

import java.util.List;

public class BiomeCondition {
    // -------------------------------------------- //
    // DATA
    // -------------------------------------------- //
    private List<String> biomes;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public BiomeCondition(List<String> biomes) {
        this.biomes = biomes;
    }

    // -------------------------------------------- //
    // PARSING DATA
    // -------------------------------------------- //
    public boolean parse() {
        for (String string : biomes) {
            try {
                Biome.valueOf(string);
            } catch (IllegalArgumentException ex) {
                return false;
            }
        }
        return true;
    }

    // -------------------------------------------- //
    // CONDITION VALIDATOR
    // -------------------------------------------- //
    public boolean isTrue(Location location) {
        Biome biome = location.getWorld().getBiome(location.getBlockX(), location.getBlockZ());
        NatureSounds.lazyStaticLog("Biome at location is " + biome.name());
        //noinspection SuspiciousMethodCalls
        return biomes.contains(biome.name());
    }
}
