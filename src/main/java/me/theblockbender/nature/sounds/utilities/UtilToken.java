package me.theblockbender.nature.sounds.utilities;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UtilToken {

    private SecureRandom random = new SecureRandom();
    private Map<UUID, String> tokens = new HashMap<>();

    private String randomString() {
        StringBuilder stringBuilder = new StringBuilder(10);
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < 10; i++)
            stringBuilder.append(characters.charAt(random.nextInt(characters.length())));
        return stringBuilder.toString();
    }

    private String generateToken(UUID uuid) {
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

    public String getToken(UUID uuid) {
        if (tokens.containsKey(uuid)) {
            return tokens.get(uuid);
        } else {
            return generateToken(uuid);
        }
    }

    public void removeToken(UUID uuid) {
        tokens.remove(uuid);
    }
}
