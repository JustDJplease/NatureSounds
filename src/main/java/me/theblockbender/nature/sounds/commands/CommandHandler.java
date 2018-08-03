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

package me.theblockbender.nature.sounds.commands;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.MessageType;
import com.google.common.collect.ImmutableList;
import me.theblockbender.nature.sounds.Lang;
import me.theblockbender.nature.sounds.NatureSounds;

import java.util.Collections;

/**
 * Finished
 */
public class CommandHandler {
    /**
     * Loads all commands.
     *
     * @param main Instance of the main class.
     */
    @SuppressWarnings("deprecation")
    public static void registerCommands(NatureSounds main) {
        BukkitCommandManager commandManager = new BukkitCommandManager(main);
        commandManager.enableUnstableAPI("help");
        commandManager.setFormat(MessageType.HELP, Lang.getBukkitColor("primary"), Lang.getBukkitColor("secondary"), Lang.getBukkitColor("argument"));
        commandManager.setFormat(MessageType.INFO, Lang.getBukkitColor("primary"), Lang.getBukkitColor("secondary"), Lang.getBukkitColor("argument"));
        commandManager.setFormat(MessageType.SYNTAX, Lang.getBukkitColor("primary"), Lang.getBukkitColor("secondary"), Lang.getBukkitColor("argument"));
        commandManager.getCommandCompletions().registerCompletion("reload", c -> ImmutableList.of("language", "sounds", "resource-pack"));
        commandManager.getCommandCompletions().registerCompletion("sounds", c -> Collections.unmodifiableCollection(main.sounds.keySet()));
        commandManager.registerCommand(new ResourcePackCommand(main));
        commandManager.registerCommand(new NatureCommand(main));
        commandManager.registerCommand(new SoundCommand(main));
        commandManager.registerCommand(new GUICommand(main));
    }
}
