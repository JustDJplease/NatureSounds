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

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.List;

public class EntityNearCondition {
    // -------------------------------------------- //
    // DATA
    // -------------------------------------------- //
    private List<String> entityTypes;
    private Double range;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public EntityNearCondition(List<String> entityTypes, Double range) {
        this.entityTypes = entityTypes;
        this.range = range;
    }

    // -------------------------------------------- //
    // PARSING DATA
    // -------------------------------------------- //
    public boolean parse() {
        for (String string : entityTypes) {
            try {
                EntityType.valueOf(string);
            } catch (IllegalArgumentException ex) {
                return false;
            }
        }
        return range >= 0;
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
}
