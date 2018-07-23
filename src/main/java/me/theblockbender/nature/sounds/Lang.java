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

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class Lang {

    static FileConfiguration languageFile;

    // -------------------------------------------- //
    // MESSAGE FORMATTERS
    // -------------------------------------------- //
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String format(String key) {
        if (!languageFile.contains(key)) return "§cError! Message §e" + key + "§c was not found!";
        String message = languageFile.getString(key);
        message = ChatColor.translateAlternateColorCodes('&', message);
        message.replace("<error>", ChatColor.translateAlternateColorCodes('&', languageFile.getString("color.error")));
        message.replace("<argument>", ChatColor.translateAlternateColorCodes('&', languageFile.getString("color.argument")));
        message.replace("<primary>", ChatColor.translateAlternateColorCodes('&', languageFile.getString("color.primary")));
        message.replace("<secondary>", ChatColor.translateAlternateColorCodes('&', languageFile.getString("color.secondary")));
        message.replace("<bold>", "" + ChatColor.BOLD);
        return message;
    }

    public static String formatWithPrefix(String key) {
        return format("prefix") + format(key);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String color(String message) {
        message.replace("<error>", ChatColor.translateAlternateColorCodes('&', languageFile.getString("color.error")));
        message.replace("<argument>", ChatColor.translateAlternateColorCodes('&', languageFile.getString("color.argument")));
        message.replace("<primary>", ChatColor.translateAlternateColorCodes('&', languageFile.getString("color.primary")));
        message.replace("<secondary>", ChatColor.translateAlternateColorCodes('&', languageFile.getString("color.secondary")));
        message.replace("<bold>", "" + ChatColor.BOLD);
        return message;
    }
}
