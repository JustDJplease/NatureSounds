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

import me.theblockbender.nature.sounds.ErrorLogger;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class BiomeCondition {
    // -------------------------------------------- //
    // DATA
    // -------------------------------------------- //
    private List<String> biomes;
    private boolean isEnabled;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public BiomeCondition(FileConfiguration soundConfiguration, String fileName) {
        try {
            if (soundConfiguration.contains("condition.biome")) {
                biomes = soundConfiguration.getStringList("condition.biome");
                if (parse(fileName)) {
                    isEnabled = true;
                    return;
                }
            }
        } catch (Exception ex) {
            ErrorLogger.errorInFile("The biome condition was not formatted correctly", fileName);
        }
        isEnabled = false;
    }

    // -------------------------------------------- //
    // PARSING DATA
    // -------------------------------------------- //
    private boolean parse(String fileName) {
        for (String string : biomes) {
            try {
                Biome.valueOf(string);
            } catch (IllegalArgumentException ex) {
                ErrorLogger.errorInFile("The biome condition is invalid. No biome of the type '" + string + "' exists.", fileName);
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
        return biomes.contains(biome.name());
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
