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

package me.theblockbender.nature.sounds.utilities;

import me.theblockbender.nature.sounds.ErrorLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UtilChecksum {

    // -------------------------------------------- //
    // CHECKSUM CALCULATOR
    // -------------------------------------------- //
    @SuppressWarnings("UnusedAssignment")
    public static byte[] getChecksum(byte[] input) {
        try {
            byte[] hash = new byte[20];
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            hash = md.digest(input);
            return hash;
        } catch (NoSuchAlgorithmException ex) {
            ErrorLogger.error("Unable to find SHA-1 algorithm");
            ex.printStackTrace();
        }
        return null;
    }

    // -------------------------------------------- //
    // BYTE-ARRAY FROM FILE
    // -------------------------------------------- //
    public static byte[] fileToByteArray(String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            ErrorLogger.error("Unable to read bytes from file");
            e.printStackTrace();
        }
        return null;
    }
}
