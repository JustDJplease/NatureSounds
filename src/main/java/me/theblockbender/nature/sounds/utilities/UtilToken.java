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

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UtilToken {

    private static final SecureRandom random = new SecureRandom();
    private static final Map<UUID, String> tokens = new HashMap<>();

    private static String randomString() {
        StringBuilder stringBuilder = new StringBuilder(10);
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < 10; i++)
            stringBuilder.append(characters.charAt(random.nextInt(characters.length())));
        return stringBuilder.toString();
    }

    private static String generateToken(UUID uuid) {
        String token = randomString();
        int attempts = 0;
        while (tokens.containsValue(token) && attempts < 20) {
            token = randomString();
            attempts++;
        }
        if (tokens.containsValue(token)) return "";
        tokens.put(uuid, token);
        return token;
    }

    public static String getToken(UUID uuid) {
        if (tokens.containsKey(uuid)) {
            return tokens.get(uuid);
        } else {
            return generateToken(uuid);
        }
    }

    public static void removeToken(UUID uuid) {
        tokens.remove(uuid);
    }

    static boolean isValidToken(String token) {
        return tokens.containsValue(token);
    }
}
