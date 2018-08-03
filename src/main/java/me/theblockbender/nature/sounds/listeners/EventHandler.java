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

package me.theblockbender.nature.sounds.listeners;

import me.theblockbender.nature.sounds.NatureSounds;
import org.bukkit.plugin.PluginManager;

/**
 * Finished
 */
public class EventHandler {
    /**
     * Registers all EventListeners.
     *
     * @param main Instance of the main class.
     */
    public static void registerEvents(NatureSounds main) {
        PluginManager pluginManager = main.getServer().getPluginManager();
        pluginManager.registerEvents(new ResourcePackListener(main), main);
        pluginManager.registerEvents(new PlayerListener(main), main);
        pluginManager.registerEvents(new ReloadListener(), main);
        pluginManager.registerEvents(new InventoryListener(main), main);
    }
}
