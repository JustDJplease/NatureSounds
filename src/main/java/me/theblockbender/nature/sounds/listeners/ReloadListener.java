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

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.HashSet;
import java.util.Set;

public class ReloadListener implements Listener {
    private final Set<String> blacklist = new HashSet<>();

    {
        blacklist.add("reload");
        blacklist.add("reload confirm");
        blacklist.add("rl");
        blacklist.add("rl confirm");
        blacklist.add("bukkit:reload");
        blacklist.add("bukkit:reload confirm");
        blacklist.add("bukkit:rl");
        blacklist.add("bukkit:rl confirm");
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().toLowerCase();
        if (command.startsWith("/") && blacklist.contains(command.substring(1))) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.DARK_RED + "Reloading is not supported. Restart instead.");
            event.getPlayer().sendMessage(ChatColor.DARK_RED + "This command was disabled by NatureSounds.");
        }
    }

    @EventHandler
    public void onConsoleCommand(ServerCommandEvent event) {
        String command = event.getCommand().toLowerCase();
        if (command.startsWith("/")) {
            command = command.substring(1);
        }
        if (blacklist.contains(command)) {
            event.setCancelled(true);
            event.getSender().sendMessage(ChatColor.DARK_RED + "Reloading is not supported. Restart instead.");
            event.getSender().sendMessage(ChatColor.DARK_RED + "This command was disabled by NatureSounds.");
        }
    }
}
