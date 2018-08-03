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
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class UtilFile {
    /**
     * Get the YAMLConfiguration from a file.
     *
     * @param file File to get the configuration from.
     * @return FileConfiguration of the input file.
     */
    public static FileConfiguration getConfiguration(File file) {
        YamlConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException ex) {
            ex.printStackTrace();
            return null;
        }
        return configuration;
    }

    /**
     * Save a jar resource to the plugin directory.
     *
     * @param main     Instance of the main class.
     * @param resource Name of the resource to save, include the extension.
     * @return Saved file.
     */
    public static File fileFromJar(NatureSounds main, String resource) {
        File file = new File(main.getDataFolder(), resource);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            main.saveResource(resource, false);
        }
        return file;
    }
}
