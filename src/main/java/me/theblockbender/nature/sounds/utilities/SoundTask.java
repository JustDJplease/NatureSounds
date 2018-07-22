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

package me.theblockbender.nature.sounds.utilities;

import me.theblockbender.nature.sounds.NatureSounds;
import me.theblockbender.nature.sounds.Sound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SoundTask implements Runnable {

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    private final NatureSounds main;

    public SoundTask(NatureSounds main) {
        this.main = main;
    }

    // -------------------------------------------- //
    // RUNNER
    // -------------------------------------------- //
    @Override
    public void run() {
        List<UUID> cleanup = new ArrayList<>();
        for (Sound sound : main.getSounds()) {
            for (UUID uuid : main.playersWithRP) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) {
                    cleanup.add(uuid);
                } else {
                    sound.run(player, player.getLocation());
                }
            }
        }
        for (UUID uuid : cleanup) {
            main.playersWithRP.remove(uuid);
        }
        cleanup.clear();
    }
}
