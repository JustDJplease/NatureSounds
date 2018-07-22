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

public class TimeCondition {
    // -------------------------------------------- //
    // DATA
    // -------------------------------------------- //
    private final Long before;
    private final Long after;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public TimeCondition(Long before, Long after) {
        this.before = before;
        this.after = after;
    }

    // -------------------------------------------- //
    // PARSING DATA
    // -------------------------------------------- //
    public boolean parse() {
        return after <= before && before >= 0 && before <= 24000 && after >= 0 && after <= 24000;
    }

    // -------------------------------------------- //
    // CONDITION VALIDATOR
    // -------------------------------------------- //
    public boolean isTrue(World world) {
        Long time = world.getTime();
        return time <= before && time >= after;
    }
}
