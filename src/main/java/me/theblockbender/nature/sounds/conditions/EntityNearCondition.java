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
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.List;

public class EntityNearCondition {
    // -------------------------------------------- //
    // DATA
    // -------------------------------------------- //
    private List<String> entityTypes;
    private double range;
    private boolean isEnabled;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public EntityNearCondition(FileConfiguration config, String fileName) {
        try {
            if (config.contains("condition.entityNear.type") && config.contains("condition.entityNear.range")) {
                entityTypes = config.getStringList("condition.entityNear.type");
                range = config.getDouble("condition.entityNear.range");
                if (parse(fileName)) {
                    isEnabled = true;
                    return;
                }
            }
        } catch (Exception ex) {
            ErrorLogger.errorInFile("The entity condition was not formatted correctly", fileName);
        }
        isEnabled = false;
    }

    // -------------------------------------------- //
    // PARSING DATA
    // -------------------------------------------- //
    private boolean parse(String fileName) {
        for (String string : entityTypes) {
            try {
                EntityType.valueOf(string);
            } catch (IllegalArgumentException ex) {
                ErrorLogger.errorInFile("The entity condition is invalid. No entity of the type '" + string + "' exists.", fileName);
                return false;
            }
        }
        if (range >= 0) {
            return true;
        }
        ErrorLogger.errorInFile("The entity condition is invalid. The range should be '0' or larger.", fileName);
        return false;
    }

    // -------------------------------------------- //
    // CONDITION VALIDATOR
    // -------------------------------------------- //
    public boolean isTrue(Location location) {
        for (Entity entity : location.getWorld().getNearbyEntities(location, range, range, range)) {
            if (entityTypes.contains(entity.getType().name())) return true;
        }
        return false;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
