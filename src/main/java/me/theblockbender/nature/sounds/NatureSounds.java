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

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.logging.Logger;

public class NatureSounds extends JavaPlugin {

    // -------------------------------------------- //
    // INSTANCES
    // -------------------------------------------- //
    private Logger logger;
    private int errorCounter;
    public Random random;

    // -------------------------------------------- //
    // ENABLING
    // -------------------------------------------- //
    @Override
    public void onEnable() {
        logger = getLogger();
        errorCounter = 0;
        random = new Random();
        showErrorsFound();
    }

    // -------------------------------------------- //
    // DISABLING
    // -------------------------------------------- //
    @Override
    public void onDisable() {
        showErrorsFound();
    }

    // -------------------------------------------- //
    // ERROR LOGGING
    // -------------------------------------------- //
    public void outputError(String errorMessage) {
        errorCounter++;
        logger.severe("// -------------------------------------------- //");
        logger.severe("// EXCEPTION OCCURED IN NATURESOUNDS PLUGIN:");
        logger.severe("// " + errorMessage);
        logger.severe("// -------------------------------------------- //");
    }

    private void showErrorsFound() {
        if (errorCounter == 0) return;
        logger.severe("// -------------------------------------------- //");
        logger.severe("// SO FAR, " + errorCounter + " ERRORS HAVE OCCURRED!");
        logger.severe("// unsure how to fix these? Contact JustDJplease");
        logger.severe("// on the spigot forums (Via discussion / PM)");
        logger.severe("// -------------------------------------------- //");
    }
}
