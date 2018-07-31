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
import me.theblockbender.nature.sounds.utilities.UtilTask;
import me.theblockbender.nature.sounds.utilities.UtilText;
import me.theblockbender.nature.sounds.utilities.UtilToken;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

    private final NatureSounds main;

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
        UtilTask.syncLater(task -> {
            Player player = event.getPlayer();
            player.sendMessage(Lang.format("header"));
            player.sendMessage(" ");
            UtilText.sendCenteredMessage(player, Lang.format("join.can-send-question"));
            UtilText.sendCenteredMessage(player, Lang.format("join.what-does-it-do"));
            player.sendMessage(" ");
            BaseComponent[] confirm = new ComponentBuilder("                 ").reset()
                    .append(Lang.format("join.yes-go-ahead")).color(ChatColor.GREEN).bold(true)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Lang.format("join.click-to-accept")).color(ChatColor.GREEN).create()))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/resourcepack accept")).append("   ").reset()
                    .append(Lang.format("join.no-thanks")).color(ChatColor.RED).bold(true)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Lang.format("join.click-to-reject")).color(ChatColor.RED).create()))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/resourcepack reject")).create();
            player.spigot().sendMessage(confirm);
            player.sendMessage(" ");
            player.sendMessage(Lang.format("header"));
        }, 2 * 20L);
    }
}
