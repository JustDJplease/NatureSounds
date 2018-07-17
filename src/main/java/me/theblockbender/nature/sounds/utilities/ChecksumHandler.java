package me.theblockbender.nature.sounds.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumHandler {

    // -------------------------------------------- //
    // CHECKSUM CALCULATOR
    // -------------------------------------------- //
    public static byte[] getChecksum(byte[] input) {
        try {
            byte[] hash = new byte[20];
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            hash = md.digest(input);
            return hash;
        } catch (NoSuchAlgorithmException ex) {
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
            e.printStackTrace();
        }
        return null;
    }
}
