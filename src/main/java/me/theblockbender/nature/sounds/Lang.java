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
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Finished
 */
public class Lang {

    @Getter @Setter public static FileConfiguration languageFile;

    // -------------------------------------------- //
    // MESSAGE FORMATTERS
    // -------------------------------------------- //
    public static String format(String key) {
        if (!languageFile.contains(key)) return "§cError! Message §e" + key + "§c was not found!";
        String message = languageFile.getString(key);
        message = ChatColor.translateAlternateColorCodes('&', message);
        message = message.replace("<error>", ChatColor.translateAlternateColorCodes('&', languageFile.getString("color.error")));
        message = message.replace("<argument>", ChatColor.translateAlternateColorCodes('&', languageFile.getString("color.argument")));
        message = message.replace("<primary>", ChatColor.translateAlternateColorCodes('&', languageFile.getString("color.primary")));
        message = message.replace("<secondary>", ChatColor.translateAlternateColorCodes('&', languageFile.getString("color.secondary")));
        message = message.replace("<bold>", "" + ChatColor.BOLD);
        return message;
    }

    public static String formatWithPrefix(String key) {
        return format("prefix") + format(key);
    }

    public static String color(String message) {
        message = message.replace("<error>", ChatColor.translateAlternateColorCodes('&', languageFile.getString("color.error")));
        message = message.replace("<argument>", ChatColor.translateAlternateColorCodes('&', languageFile.getString("color.argument")));
        message = message.replace("<primary>", ChatColor.translateAlternateColorCodes('&', languageFile.getString("color.primary")));
        message = message.replace("<secondary>", ChatColor.translateAlternateColorCodes('&', languageFile.getString("color.secondary")));
        message = message.replace("<bold>", "" + ChatColor.BOLD);
        return message;
    }

    public static ChatColor getColor(String subkey) {
        try {
            return ChatColor.valueOf(languageFile.getString("color." + subkey + "-word").toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ChatColor.DARK_RED;
        }
    }

    public static org.bukkit.ChatColor getBukkitColor(String subkey) {
        try {
            return org.bukkit.ChatColor.valueOf(languageFile.getString("color." + subkey + "-word").toUpperCase());
        } catch (IllegalArgumentException ex) {
            return org.bukkit.ChatColor.DARK_RED;
        }
    }
}
