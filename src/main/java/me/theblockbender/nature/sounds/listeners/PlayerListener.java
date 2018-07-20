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
import me.theblockbender.nature.sounds.utilities.UtilToken;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

    private NatureSounds main;

    public PlayerListener(NatureSounds main) {
        this.main = main;
    }

    // -------------------------------------------- //
    // EVENT
    // -------------------------------------------- //
    @EventHandler
    public void PlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        UtilToken.removeToken(uuid);
        main.playersWithRP.remove(uuid);
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(main, () -> {
            Player player = event.getPlayer();
            player.sendMessage("§6§lWould you like to hear nature sounds on this server?");
            // TODO lang file + textcomponents
        }, 2 * 20L);

    }
}
