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
import org.bukkit.configuration.file.FileConfiguration;

public class AltitudeCondition {
    // -------------------------------------------- //
    // DATA
    // -------------------------------------------- //
    private double below;
    private double above;
    private boolean isEnabled;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public AltitudeCondition(FileConfiguration soundConfiguration, String fileName) {
        try {
            if (soundConfiguration.contains("condition.altitude")) {
                below = soundConfiguration.getDouble("condition.altitude.below");
                above = soundConfiguration.getDouble("condition.altitude.above");

                if (!(below == 0 && above == 0)) {
                    if (parse(fileName)) {
                        isEnabled = true;
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            ErrorLogger.errorInFile("The altitude condition was not formatted correctly", fileName);
        }
        isEnabled = false;
    }

    // -------------------------------------------- //
    // PARSING DATA
    // -------------------------------------------- //
    private boolean parse(String fileName) {
        if (below >= 0 && below <= 256 && above >= 0 && above <= 256 && below >= above) {
            return true;
        } else {
            ErrorLogger.errorInFile("The altitude condition is invalid", fileName);
            return false;
        }
    }

    // -------------------------------------------- //
    // CONDITION VALIDATOR
    // -------------------------------------------- //
    public boolean isTrue(Location location) {
        double y = location.getY();
        return y <= below && y >= above;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
