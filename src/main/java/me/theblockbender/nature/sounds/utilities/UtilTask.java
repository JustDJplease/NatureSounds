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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class UtilTask {

    // -------------------------------------------- //
    // SYNC
    // -------------------------------------------- //
    public static void sync(final Consumer<BukkitRunnable> block) {
        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                block.accept(this);
            }
        };
        runnable.runTask(NatureSounds.inst());
    }

    public static void syncLater(final Consumer<BukkitRunnable> block, long delay) {
        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                block.accept(this);
            }
        };
        runnable.runTaskLater(NatureSounds.inst(), delay);
    }

    public static void syncRepeat(final Consumer<BukkitRunnable> block, long interval) {
        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                block.accept(this);
            }
        };
        runnable.runTaskTimer(NatureSounds.inst(), 0L, interval);
    }

    // -------------------------------------------- //
    // ASYNC
    // -------------------------------------------- //
    public static void async(final Consumer<BukkitRunnable> block) {
        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                block.accept(this);
            }
        };
        runnable.runTaskAsynchronously(NatureSounds.inst());
    }
}
