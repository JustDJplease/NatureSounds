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

import org.bukkit.World;

import java.util.List;

public class WeatherCondition {
    // -------------------------------------------- //
    // DATA
    // -------------------------------------------- //
    private final List<String> weatherTypes;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public WeatherCondition(List<String> weatherTypes) {
        this.weatherTypes = weatherTypes;
    }

    // -------------------------------------------- //
    // PARSING DATA
    // -------------------------------------------- //
    public boolean parse() {
        for (String string : weatherTypes) {
            if (!string.equals("THUNDER") && !string.equals("RAIN") && !string.equals("CLEAR")) return false;
        }
        return true;
    }

    // -------------------------------------------- //
    // CONDITION VALIDATOR
    // -------------------------------------------- //
    public boolean isTrue(World world) {
        return weatherTypes.contains(getWeatherType(world));
    }

    private String getWeatherType(World world) {
        if (world.hasStorm()) return "RAIN";
        if (world.isThundering()) return "THUNDER";
        return "CLEAR";
    }
}
