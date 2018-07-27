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

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.theblockbender.nature.sounds.ErrorLogger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class RegionCondition {
    // -------------------------------------------- //
    // DATA
    // -------------------------------------------- //
    private List<String> regions;
    private boolean isEnabled;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public RegionCondition(YamlConfiguration config, String fileName, boolean hasWorldGuard) {
        if (!hasWorldGuard) {
            ErrorLogger.errorInFile("WorldGuard is not enabled. Region conditions cannot be used!", fileName);
            isEnabled = false;
            return;
        }
        try {
            if (config.contains("condition.region")) {
                regions = config.getStringList("condition.region");
                if (parse(fileName)) {
                    isEnabled = true;
                    return;
                }
            }
        } catch (Exception ex) {
            ErrorLogger.errorInFile("The region condition was not formatted correctly", fileName);
        }
        isEnabled = false;
    }

    // -------------------------------------------- //
    // PARSING DATA
    // -------------------------------------------- //
    private boolean parse(String fileName) {
        for (String string : regions) {
            boolean found = false;
            for (World world : Bukkit.getWorlds()) {
                RegionManager regionManager = WorldGuardPlugin.inst().getRegionManager(world);
                if (regionManager.hasRegion(string)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                ErrorLogger.errorInFile("The region condition is invalid. The region '" + string + "' does not exist.", fileName);
                return false;
            }
        }
        return true;
    }

    // -------------------------------------------- //
    // CONDITION VALIDATOR
    // -------------------------------------------- //
    public boolean isTrue(Location location) {
        RegionManager regionManager = WorldGuardPlugin.inst().getRegionManager(location.getWorld());
        for (String string : regions) {
            if (regionManager.hasRegion(string)) {
                boolean found = false;
                for (ProtectedRegion protectedRegion : regionManager.getApplicableRegions(location).getRegions()) {
                    if (protectedRegion.getId().equalsIgnoreCase(string)) {
                        found = true;
                        break;
                    }
                }
                if (!found) return false;
            }
        }
        return true;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}