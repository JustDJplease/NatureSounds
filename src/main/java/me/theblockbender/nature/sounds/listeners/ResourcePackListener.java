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

import me.theblockbender.nature.sounds.Lang;
import me.theblockbender.nature.sounds.NatureSounds;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import java.util.UUID;

public class ResourcePackListener implements Listener {

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    private NatureSounds main;

    public ResourcePackListener(NatureSounds main) {
        this.main = main;
    }

    // -------------------------------------------- //
    // EVENT
    // -------------------------------------------- //
    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        switch (event.getStatus()) {
            case SUCCESSFULLY_LOADED:
                main.playersWithRP.add(uuid);
                player.sendMessage(Lang.format("resourcepack.loaded"));
                break;
            case DECLINED:
                main.playersWithRP.remove(uuid);
                player.sendMessage(Lang.format("resourcepack.rejected"));
                break;
            case FAILED_DOWNLOAD:
                main.playersWithRP.remove(uuid);
                player.sendMessage(Lang.format("resourcepack.rejected"));
                break;
            case ACCEPTED:
                main.playersWithRP.add(uuid);
                player.sendMessage(Lang.format("resourcepack.downloading"));
                break;
        }
    }
}
