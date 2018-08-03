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

import me.theblockbender.nature.sounds.Lang;
import me.theblockbender.nature.sounds.NatureSounds;
import me.theblockbender.nature.sounds.Sound;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Objects;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class UtilMain {

    /**
     * Creates the config.yml and the /web directory in the plugin folder.
     *
     * @param main Instance of the main class.
     */
    public static void createFiles(NatureSounds main) {
        main.saveDefaultConfig();
        File webDirectory = new File(main.getDataFolder().getPath() + File.separator + "web");
        if (!webDirectory.exists()) webDirectory.mkdirs();
    }

    /**
     * Hooks WorldGuard
     *
     * @param main Instance of the main class.
     */
    public static void hookWorldGuard(NatureSounds main) {
        Plugin worldGuard = main.getServer().getPluginManager().getPlugin("WorldGuard");
        if (worldGuard != null) main.setWorldGuardHooked(true);
    }

    /**
     * Creates language files.
     *
     * @param main Instance of the main class.
     */
    public static void loadLanguage(NatureSounds main) {
        File langFile = UtilFile.fileFromJar(main, "language.yml");
        Lang.setLanguageFile(UtilFile.getConfiguration(langFile));
    }

    /**
     * Creates Sound Objects from all sound configuration files.
     *
     * @param main Instance of the main class.
     */
    public static void loadSounds(NatureSounds main) {
        main.sounds.clear();
        for (File soundFile : getSoundFiles(main)) {
            main.sounds.put(soundFile.getName(), new Sound(main, soundFile.getName(), Objects.requireNonNull(UtilFile.getConfiguration(soundFile))));
        }
        main.getLogger().info(main.sounds.size() + " sound configuration files have been loaded.");
    }

    /**
     * Get a list of all sound files.
     *
     * @param main Instance of the main class.
     * @return List of sound files.
     */
    private static File[] getSoundFiles(NatureSounds main) {
        File folder = new File(main.getDataFolder().getPath() + File.separator + "sounds");
        if (!folder.exists())
            folder.mkdirs();
        return folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
    }

    /**
     * Starts the scheduler.
     *
     * @param main Instance of the main class.
     */
    public static void startRunnable(NatureSounds main) {
        long interval = main.getConfig().getLong("interval");
        assert interval > 0L : "Interval specified is invalid in config.yml";
        UtilTask.syncRepeat(task -> new SoundTask(main), 20L * interval);
    }
}
