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

package me.theblockbender.nature.sounds;

import me.theblockbender.nature.sounds.commands.WebTestCommand;
import me.theblockbender.nature.sounds.listeners.PlayerListener;
import me.theblockbender.nature.sounds.listeners.ResourcePackListener;
import me.theblockbender.nature.sounds.utilities.SoundTask;
import me.theblockbender.nature.sounds.utilities.UtilResourcePack;
import me.theblockbender.nature.sounds.utilities.UtilWebServer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class NatureSounds extends JavaPlugin {

    // -------------------------------------------- //
    // INSTANCES & VARIABLES
    // -------------------------------------------- //
    public List<UUID> playersWithRP = new ArrayList<>();
    public Map<String, Sound> sounds = new HashMap<>();
    public UtilWebServer utilWebServer;
    public UtilResourcePack utilResourcePack;

    Random random;

    private int errorCounter;
    private Logger logger;

    @Deprecated
    public static void lazyStaticLog(String s) {
        Bukkit.getLogger().info(s);
    }

    // -------------------------------------------- //
    // ENABLING
    // -------------------------------------------- //
    @Override
    public void onEnable() {
        logger = getLogger();
        errorCounter = 0;
        random = new Random();
        createFiles();
        registerEvents();
        registerCommands();
        registerSounds();
        registerRunnables();
        utilResourcePack = new UtilResourcePack(this);
        registerWebServer();
        showErrorsFound();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createFiles() {
        saveDefaultConfig();
        File webDirectory = new File(getDataFolder().getPath() + File.separator + "web");
        if (!webDirectory.exists()) webDirectory.mkdirs();
    }

    private void registerWebServer() {
        utilWebServer = new UtilWebServer(this);
        utilResourcePack.addAllFilesToPack();
        utilWebServer.start();
    }

    private void registerCommands() {
        getCommand("web").setExecutor(new WebTestCommand(this));
    }

    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ResourcePackListener(this), this);
        pluginManager.registerEvents(new PlayerListener(this), this);

    }

    private void registerRunnables() {
        Long interval;
        try {
            interval = getConfig().getLong("interval");
        } catch (Exception ex) {
            outputError("Invalid interval specified in the config.yml");
            outputError("None of the sounds will start playing!");
            return;
        }
        Bukkit.getScheduler().runTaskTimer(this, new SoundTask(this), 1L, 20L * interval);
    }

    private void registerSounds() {
        int counter = 0;
        for (File soundFile : getSoundFiles()) {
            counter++;
            YamlConfiguration soundConfiguration = new YamlConfiguration();
            try {
                soundConfiguration.load(soundFile);
            } catch (IOException | InvalidConfigurationException ex) {
                outputError("Unable to load soundconfiguration file " + soundFile.getName());
                ex.printStackTrace();
                continue;
            }
            sounds.put(soundFile.getName(), new Sound(this, soundFile.getName(), soundConfiguration));
        }
        logger.info(counter + " sound configuration files have been loaded.");
    }

    // -------------------------------------------- //
    // DISABLING
    // -------------------------------------------- //
    @Override
    public void onDisable() {
        showErrorsFound();
    }

    // -------------------------------------------- //
    // ERROR & LOGGING
    // -------------------------------------------- //
    public void outputError(String errorMessage) {
        errorCounter++;
        logger.severe("// -------------------------------------------- //");
        logger.severe("// EXCEPTION OCCURED IN NATURESOUNDS PLUGIN:");
        logger.severe("// " + errorMessage);
        logger.severe("// -------------------------------------------- //");
    }

    private void showErrorsFound() {
        if (errorCounter == 0) return;
        logger.severe("// -------------------------------------------- //");
        logger.severe("// SO FAR, " + errorCounter + " ERRORS HAVE OCCURRED!");
        logger.severe("// unsure how to fix these? Contact JustDJplease");
        logger.severe("// on the spigot forums (Via discussion / PM)");
        logger.severe("// -------------------------------------------- //");
    }

    public void debug(String debugMessage) {
        logger.info("[+] " + debugMessage);
    }

    // -------------------------------------------- //
    // GETTERS
    // -------------------------------------------- //
    public List<Sound> getSounds() {
        return new ArrayList<>(sounds.values());
    }

    private File[] getSoundFiles() {
        File folder = new File(getDataFolder().getPath() + File.separator + "sounds");
        if (!folder.exists()) //noinspection ResultOfMethodCallIgnored
            folder.mkdirs();
        return folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
    }
}
