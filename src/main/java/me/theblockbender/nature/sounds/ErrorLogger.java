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

import org.bukkit.Bukkit;

import java.util.logging.Logger;

@Deprecated
public class ErrorLogger {

    private static int errors = 0;

    public static void error(String error) {
        errors++;
        Logger log = Bukkit.getLogger();
        log.severe("|--------------------------------------------");
        log.severe("| An error occurred in the NatureSound plugin!");
        log.severe("| ");
        log.severe("| Description:");
        log.severe("| " + error);
        log.severe("| ");
        log.severe("| Unsure how to fix this? Contact JustDJplease!");
        log.severe("|--------------------------------------------");
    }

    public static void errorInFile(String error, String path) {
        errors++;
        Logger log = Bukkit.getLogger();
        log.severe("|--------------------------------------------");
        log.severe("| NatureSounds ran into an error whilst loading a file!");
        log.severe("| ");
        log.severe("| Description:");
        log.severe("| " + error);
        log.severe("| ");
        log.severe("| In File:");
        log.severe("| " + path);
        log.severe("| ");
        log.severe("| Unsure how to fix this? Contact JustDJplease!");
        log.severe("|--------------------------------------------");
    }

    static void supportMessage() {
        if (errors == 0) return;
        Logger log = Bukkit.getLogger();
        log.severe("|--------------------------------------------");
        log.severe("| A bunch of errors occurred in NatureSounds so far!");
        log.severe("| ");
        log.severe("| I've counted:");
        log.severe("| " + errors + " problems.");
        log.severe("| ");
        log.severe("| Unsure how to fix these? Contact JustDJplease!");
        log.severe("|--------------------------------------------");
    }
}
