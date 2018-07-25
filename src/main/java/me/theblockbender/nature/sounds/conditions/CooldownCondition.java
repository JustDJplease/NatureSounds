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
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class CooldownCondition {
    // -------------------------------------------- //
    // DATA
    // -------------------------------------------- //
    private final Map<UUID, Long> onCooldown = new HashMap<>();
    private long cooldown;
    private boolean isEnabled;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //

    public CooldownCondition(YamlConfiguration soundConfiguration, String fileName) {
        try {
            if (soundConfiguration.contains("condition.cooldown")) {
                cooldown = soundConfiguration.getLong("condition.cooldown");
                if (parse(fileName)) {
                    isEnabled = true;
                    return;
                }
            }
        } catch (Exception ex) {
            ErrorLogger.errorInFile("The cooldown condition was not formatted correctly", fileName);
        }
        isEnabled = false;
    }

    // -------------------------------------------- //
    // PARSING DATA
    // -------------------------------------------- //
    private boolean parse(String fileName) {
        if (cooldown >= 0L) {
            return true;
        }
        ErrorLogger.errorInFile("The cooldown condition is invalid. The cooldown should be '0' or larger.", fileName);
        return false;
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

    public boolean isEnabled() {
        return isEnabled;
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
