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
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class WeatherCondition {
    // -------------------------------------------- //
    // DATA
    // -------------------------------------------- //
    private List<String> weatherTypes;
    private boolean isEnabled;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public WeatherCondition(YamlConfiguration soundConfiguration, String fileName) {
        try {
            if (soundConfiguration.contains("condition.weather")) {
                weatherTypes = soundConfiguration.getStringList("condition.weather");
                if (parse(fileName)) {
                    isEnabled = true;
                    return;
                }

            }
        } catch (Exception ex) {
            ErrorLogger.errorInFile("The weather condition was not formatted correctly", fileName);
        }
        isEnabled = false;
    }

    // -------------------------------------------- //
    // PARSING DATA
    // -------------------------------------------- //
    private boolean parse(String fileName) {
        for (String string : weatherTypes) {
            if (!string.equals("THUNDER") && !string.equals("RAIN") && !string.equals("CLEAR")) {
                ErrorLogger.errorInFile("The weather condition is invalid. No weather of the type '" + string + "' exists.", fileName);
                return false;
            }
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

    public boolean isEnabled() {
        return isEnabled;
    }
}
