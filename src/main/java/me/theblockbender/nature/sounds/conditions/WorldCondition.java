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
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class WorldCondition {
    // -------------------------------------------- //
    // DATA
    // -------------------------------------------- //
    private List<String> worlds;
    private boolean isEnabled;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public WorldCondition(YamlConfiguration config, String fileName) {
        try {
            if (config.contains("condition.world")) {
                worlds = config.getStringList("condition.world");
                if (!worlds.isEmpty()) {
                    if (parse(fileName)) {
                        isEnabled = true;
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            ErrorLogger.errorInFile("The world condition was not formatted correctly", fileName);
        }
        isEnabled = false;
    }

    // -------------------------------------------- //
    // PARSING DATA
    // -------------------------------------------- //
    private boolean parse(String fileName) {
        for (String string : worlds) {
            if (Bukkit.getWorld(string) == null) {
                ErrorLogger.errorInFile("The world condition is invalid. The world '" + string + "' does not exist.", fileName);
                return false;
            }
        }
        return true;
    }

    // -------------------------------------------- //
    // CONDITION VALIDATOR
    // -------------------------------------------- //
    public boolean isTrue(World world) {
        return worlds.contains(world.getName());
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
