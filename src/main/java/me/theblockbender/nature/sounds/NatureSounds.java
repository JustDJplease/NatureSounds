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

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.MessageType;
import com.google.common.collect.ImmutableList;
import me.theblockbender.nature.sounds.commands.NatureCommand;
import me.theblockbender.nature.sounds.commands.ResourcePackCommand;
import me.theblockbender.nature.sounds.listeners.PlayerListener;
import me.theblockbender.nature.sounds.listeners.ReloadListener;
import me.theblockbender.nature.sounds.listeners.ResourcePackListener;
import me.theblockbender.nature.sounds.utilities.SoundTask;
import me.theblockbender.nature.sounds.utilities.UtilResourcePack;
import me.theblockbender.nature.sounds.utilities.UtilText;
import me.theblockbender.nature.sounds.utilities.UtilWebServer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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
    public final List<UUID> playersWithRP = new ArrayList<>();
    private final Map<String, Sound> sounds = new HashMap<>();
    public UtilWebServer utilWebServer;
    public UtilResourcePack utilResourcePack;

    Random random;

    private Logger logger;

    // -------------------------------------------- //
    // ENABLING
    // -------------------------------------------- //
    @Override
    public void onEnable() {
        logger = getLogger();
        random = new Random();
        createFiles();
        registerLanguage();
        registerEvents();
        registerCommands();
        registerSounds();
        registerRunnables();
        utilResourcePack = new UtilResourcePack(this);
        registerWebServer();
        ErrorLogger.supportMessage();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void registerLanguage() {
        File messagesFile = new File(getDataFolder(), "language.yml");
        if (!messagesFile.exists()) {
            messagesFile.getParentFile().mkdirs();
            saveResource("language.yml", false);
        }
        YamlConfiguration messages = new YamlConfiguration();
        try {
            messages.load(messagesFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        Lang.languageFile = messages;
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

    @SuppressWarnings("deprecation")
    private void registerCommands() {
        BukkitCommandManager commandManager = new BukkitCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.setFormat(MessageType.HELP, Lang.getColor("primary"), Lang.getColor("secondary"), Lang.getColor("argument"));
        commandManager.setFormat(MessageType.INFO, Lang.getColor("primary"), Lang.getColor("secondary"), Lang.getColor("argument"));
        commandManager.setFormat(MessageType.SYNTAX, Lang.getColor("primary"), Lang.getColor("secondary"), Lang.getColor("argument"));
        commandManager.getCommandCompletions().registerCompletion("reload", c -> ImmutableList.of("language", "sounds", "resource-pack"));
        commandManager.registerCommand(new ResourcePackCommand(this));
        commandManager.registerCommand(new NatureCommand(this));
    }

    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ResourcePackListener(this), this);
        pluginManager.registerEvents(new PlayerListener(this), this);
        pluginManager.registerEvents(new ReloadListener(), this);
    }

    private void registerRunnables() {
        Long interval;
        try {
            interval = getConfig().getLong("interval");
        } catch (Exception ex) {
            ErrorLogger.errorInFile("Interval specified is invalid", "config.yml");
            return;
        }
        Bukkit.getScheduler().runTaskTimer(this, new SoundTask(this), 1L, 20L * interval);
    }

    public void registerSounds() {
        int counter = 0;
        sounds.clear();
        for (File soundFile : getSoundFiles()) {
            counter++;
            YamlConfiguration soundConfiguration = new YamlConfiguration();
            try {
                soundConfiguration.load(soundFile);
            } catch (IOException | InvalidConfigurationException ex) {
                ErrorLogger.errorInFile("Unable to save & load configuration file", soundFile.getName());
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
        ErrorLogger.supportMessage();
        for (UUID uuid : playersWithRP) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.sendMessage(Lang.format("header"));
                player.sendMessage(" ");
                UtilText.sendCenteredMessage(player, Lang.color("<primary>This plugin was disabled."));
                UtilText.sendCenteredMessage(player, Lang.color("<primary>You will no longer hear any sounds."));
                player.sendMessage(" ");
                player.sendMessage(Lang.format("header"));
            }
        }
    }


    public void debug(String debugMessage) {
        if (getConfig().getBoolean("debug")) logger.info("[+] " + debugMessage);
    }

    // -------------------------------------------- //
    // GETTERS
    // -------------------------------------------- //
    public List<Sound> getSounds() {
        return new ArrayList<>(sounds.values());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File[] getSoundFiles() {
        File folder = new File(getDataFolder().getPath() + File.separator + "sounds");
        if (!folder.exists())
            folder.mkdirs();
        return folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
    }
}
