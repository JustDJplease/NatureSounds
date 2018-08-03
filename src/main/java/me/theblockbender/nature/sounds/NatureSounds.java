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

import lombok.Getter;
import lombok.Setter;
import me.theblockbender.nature.sounds.commands.CommandHandler;
import me.theblockbender.nature.sounds.gui.Menus;
import me.theblockbender.nature.sounds.listeners.EventHandler;
import me.theblockbender.nature.sounds.utilities.UtilMain;
import me.theblockbender.nature.sounds.utilities.UtilResourcePack;
import me.theblockbender.nature.sounds.utilities.UtilText;
import me.theblockbender.nature.sounds.utilities.UtilWebServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class NatureSounds extends JavaPlugin {

    // -------------------------------------------- //
    // INSTANCES & VARIABLES
    // -------------------------------------------- //
    @Getter @Setter public static NatureSounds inst;
    public final Map<String, Sound> sounds = new HashMap<>();
    @Getter @Setter public Random random;
    @Getter @Setter public UtilWebServer webServer;
    @Getter @Setter public UtilResourcePack resourcePack;
    @Getter @Setter public Menus menuInstances;

    public final Set<UUID> playersWithRP = new HashSet<>();
    @Getter @Setter boolean WorldGuardHooked = false;

    // -------------------------------------------- //
    // ENABLING
    // -------------------------------------------- //
    @Override
    public void onEnable() {
        setInst(this);
        setRandom(new Random());

        UtilMain.createFiles(this);
        UtilMain.hookWorldGuard(this);
        UtilMain.loadLanguage(this);
        EventHandler.registerEvents(this);
        UtilMain.loadSounds(this);
        CommandHandler.registerCommands(this);
        UtilMain.startRunnable(this);

        setResourcePack(new UtilResourcePack(this));
        setMenuInstances(new Menus(this));

        registerWebServer();
        ErrorLogger.supportMessage();
    }


    private void registerWebServer() {
        setWebServer(new UtilWebServer(this));
        resourcePack.addAllFilesToPack();
        webServer.start();
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
        inst = null;
    }

    public void debug(String debugMessage) {
        if (getConfig().getBoolean("debug")) getLogger().warning("[+] " + debugMessage);
    }

    // -------------------------------------------- //
    // GETTERS
    // -------------------------------------------- //
    public List<Sound> getSounds() {
        return new ArrayList<>(sounds.values());
    }

    public Sound getSound(String fileName) {
        return sounds.get(fileName);
    }
}
