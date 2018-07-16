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

public class AltitudeCondition {
    // -------------------------------------------- //
    // DATA
    // -------------------------------------------- //
    private Double below;
    private Double above;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public AltitudeCondition(Double below, Double above) {
        this.below = below;
        this.above = above;
    }

    // -------------------------------------------- //
    // PARSING DATA
    // -------------------------------------------- //
    public boolean parse() {
        return below >= 0 && below <= 256 && above >= 0 && above <= 256 && below >= above;
    }

    // -------------------------------------------- //
    // CONDITION VALIDATOR
    // -------------------------------------------- //
    public boolean isTrue(Location location) {
        Double y = location.getY();
        return y <= below && y >= above;
    }
}
