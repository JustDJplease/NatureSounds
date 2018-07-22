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

import org.bukkit.entity.Player;

import java.util.*;

public class CooldownCondition {
    // -------------------------------------------- //
    // DATA
    // -------------------------------------------- //
    private Map<UUID, Long> onCooldown = new HashMap<>();
    private Long cooldown;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public CooldownCondition(Long cooldown) {
        this.cooldown = cooldown;
    }

    // -------------------------------------------- //
    // PARSING DATA
    // -------------------------------------------- //
    public boolean parse() {
        return cooldown >= 0L;
    }

    // -------------------------------------------- //
    // SETTER
    // -------------------------------------------- //
    public void setCooldown(Player player) {
        onCooldown.put(player.getUniqueId(), System.currentTimeMillis() + cooldown);
    }

    // -------------------------------------------- //
    // CONDITION VALIDATOR
    // -------------------------------------------- //
    public boolean isOnCooldown(Player player) {
        cleanUpMap();
        return onCooldown.containsKey(player.getUniqueId());
    }

    private void cleanUpMap() {
        List<UUID> cleanThese = new ArrayList<>();
        for (Map.Entry<UUID, Long> cooldowns : onCooldown.entrySet()) {
            if (System.currentTimeMillis() >= cooldowns.getValue()) {
                cleanThese.add(cooldowns.getKey());
            }
        }
        for (UUID uuid : cleanThese) {
            onCooldown.remove(uuid);
        }
        cleanThese.clear();
    }
}
