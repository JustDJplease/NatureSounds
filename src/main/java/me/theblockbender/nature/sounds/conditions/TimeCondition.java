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

public class TimeCondition {
    // -------------------------------------------- //
    // DATA
    // -------------------------------------------- //
    private long before;
    private long after;
    private boolean isEnabled;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public TimeCondition(YamlConfiguration config, String fileName) {
        try {
            if (config.contains("condition.time")) {
                before = config.getLong("condition.time.before");
                after = config.getLong("condition.time.after");
                if (parse(fileName)) {
                    isEnabled = true;
                    return;
                }
            }
        } catch (Exception ex) {
            ErrorLogger.errorInFile("The time condition was not formatted correctly", fileName);
        }
        isEnabled = false;
    }

    // -------------------------------------------- //
    // PARSING DATA
    // -------------------------------------------- //
    private boolean parse(String fileName) {
        if (after <= before && before >= 0 && before <= 24000 && after >= 0) {
            return true;
        }
        ErrorLogger.errorInFile("The time condition is invalid", fileName);
        return false;
    }

    // -------------------------------------------- //
    // CONDITION VALIDATOR
    // -------------------------------------------- //
    public boolean isTrue(World world) {
        long time = world.getTime();
        return time <= before && time >= after;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
