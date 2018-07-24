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

import me.theblockbender.nature.sounds.conditions.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Sound {
    // -------------------------------------------- //
    // IDENTIFIER & MAIN
    // -------------------------------------------- //
    private NatureSounds main;
    private boolean loaded;
    private String fileName;

    // -------------------------------------------- //
    // CONDITIONS
    // -------------------------------------------- //
    private TimeCondition timeCondition;
    private BiomeCondition biomeCondition;
    private AltitudeCondition altitudeCondition;
    private WeatherCondition weatherCondition;
    private WorldCondition worldCondition;
    private CooldownCondition cooldownCondition;
    private EntityNearCondition entityNearCondition;
    private Double chance;

    // -------------------------------------------- //
    // SOUND PROPERTIES
    // -------------------------------------------- //
    private List<String> soundNames = new ArrayList<>();
    private Float minVolume;
    private Float maxVolume;
    private Float pitch;
    private String subtitle;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    Sound(NatureSounds main, String fileName, YamlConfiguration soundConfiguration) {
        loaded = false;
        assert main != null;
        this.main = main;
        // -------------------------------------------- //
        //  FILE NAME
        // -------------------------------------------- //
        if (fileName == null || fileName.equalsIgnoreCase("")) {
            ErrorLogger.error("Tried to load a file with no name. How is that even possible?");
            return;
        } else {
            this.fileName = fileName;
        }
        // -------------------------------------------- //
        // CONFIGURATION SECTION
        // -------------------------------------------- //
        if (soundConfiguration == null) {
            ErrorLogger.error("Tried to load a configuration file that was not a configuration file");
            return;
        }
        // -------------------------------------------- //
        // SOUND NAME
        // -------------------------------------------- //
        try {
            soundNames = soundConfiguration.getStringList("sound.names");
        } catch (NullPointerException ex) {
            ErrorLogger.errorInFile("No sound name(s) specified", fileName);
            return;
        }
        // -------------------------------------------- //
        // SOUND MIN VOLUME
        // -------------------------------------------- //
        try {
            minVolume = Float.parseFloat("" + soundConfiguration.getDouble("sound.minVolume"));
            if (minVolume < 0) {
                ErrorLogger.errorInFile("Min-volume cannot be negative", fileName);
                return;
            }
        } catch (Exception ex) {
            ErrorLogger.errorInFile("Min-volume was not a number OR it was not specified", fileName);
            return;
        }
        // -------------------------------------------- //
        // SOUND MAX VOLUME
        // -------------------------------------------- //
        try {
            maxVolume = Float.parseFloat("" + soundConfiguration.getDouble("sound.maxVolume"));
            if (maxVolume < 0 || maxVolume < minVolume) {
                ErrorLogger.errorInFile("Max-volume cannot be negative OR smaller than Min-volume", fileName);
                return;
            }
        } catch (Exception ex) {
            ErrorLogger.errorInFile("Max-volume was not a number OR it was not specified", fileName);
            return;
        }
        // -------------------------------------------- //
        // SOUND PITCH
        // -------------------------------------------- //
        try {
            pitch = Float.parseFloat("" + soundConfiguration.getDouble("sound.pitch"));
            if (pitch < 0 || pitch > 2) {
                ErrorLogger.errorInFile("Pitch was not a number between 0 and 2", fileName);
                return;
            }
        } catch (Exception ex) {
            ErrorLogger.errorInFile("Pitch was not a number OR it was not specified", fileName);
            return;
        }
        // -------------------------------------------- //
        // SUBTITLE
        // -------------------------------------------- //
        try {
            subtitle = soundConfiguration.getString("sound.subtitle");
        } catch (Exception ex) {
            ErrorLogger.errorInFile("Subtitle was not specified", fileName);
            return;
        }
        // -------------------------------------------- //
        // CHANCE
        // -------------------------------------------- //
        try {
            chance = soundConfiguration.getDouble("chance");
            if (chance < 0 || chance > 100) {
                ErrorLogger.errorInFile("Chance was not a number between 0 and 100", fileName);
                return;
            }
        } catch (Exception ex) {
            ErrorLogger.errorInFile("Chance was not a number OR it was not specified", fileName);
            return;
        }
        // -------------------------------------------- //
        // WEATHER CONDITION
        // -------------------------------------------- //
        try {
            if (soundConfiguration.contains("condition.weather")) {
                List<String> weatherTypes = soundConfiguration.getStringList("condition.weather");
                if (!weatherTypes.isEmpty()) {
                    weatherCondition = new WeatherCondition(weatherTypes);
                    if (!weatherCondition.parse()) {
                        ErrorLogger.errorInFile("The weather condition is invalid.", fileName);
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            ErrorLogger.errorInFile("The weather condition was not formatted correctly", fileName);
            return;
        }
        // -------------------------------------------- //
        // ALTITUDE CONDITION
        // -------------------------------------------- //
        try {
            if (soundConfiguration.contains("condition.altitude")) {
                Double below = soundConfiguration.getDouble("condition.altitude.below");
                Double above = soundConfiguration.getDouble("condition.altitude.above");

                if (!(below == 0 && above == 0)) {
                    altitudeCondition = new AltitudeCondition(below, above);
                    if (!altitudeCondition.parse()) {
                        ErrorLogger.errorInFile("The altitude condition is invalid", fileName);
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            ErrorLogger.errorInFile("The altitude condition was not formatted correctly", fileName);
            return;
        }
        // -------------------------------------------- //
        // BIOME CONDITION
        // -------------------------------------------- //
        try {
            if (soundConfiguration.contains("condition.biome")) {
                List<String> biomes = soundConfiguration.getStringList("condition.biome");
                if (!biomes.isEmpty()) {
                    biomeCondition = new BiomeCondition(biomes);
                    if (!biomeCondition.parse()) {
                        ErrorLogger.errorInFile("The biome condition is invalid", fileName);
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            ErrorLogger.errorInFile("The biome condition was not formatted correctly", fileName);
            return;
        }

        // -------------------------------------------- //
        // ENTITY NEAR CONDITION
        // -------------------------------------------- //
        try {
            if (soundConfiguration.contains("condition.entityNear.type") && soundConfiguration.contains("condition.entityNear.range")) {
                List<String> types = soundConfiguration.getStringList("condition.entityNear.type");
                if (!types.isEmpty()) {
                    entityNearCondition = new EntityNearCondition(types, soundConfiguration.getDouble("condition.entityNear.range"));
                    if (!entityNearCondition.parse()) {
                        ErrorLogger.errorInFile("The entity condition is invalid", fileName);
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            ErrorLogger.errorInFile("The entity condition was not formatted correctly", fileName);
            return;
        }
        // -------------------------------------------- //
        // TIME CONDITION
        // -------------------------------------------- //
        try {
            if (soundConfiguration.contains("condition.time")) {
                Long before = soundConfiguration.getLong("condition.time.before");
                Long after = soundConfiguration.getLong("condition.time.after");

                if (!(before == 0 && after == 0)) {
                    timeCondition = new TimeCondition(before, after);
                    if (!timeCondition.parse()) {
                        ErrorLogger.errorInFile("The time condition is invalid", fileName);
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            ErrorLogger.errorInFile("The time condition was not formatted correctly", fileName);
            return;
        }
        // -------------------------------------------- //
        // WORLD CONDITION
        // -------------------------------------------- //
        try {
            if (soundConfiguration.contains("condition.world")) {
                List<String> worlds = soundConfiguration.getStringList("condition.world");
                if (!worlds.isEmpty()) {
                    worldCondition = new WorldCondition(worlds);
                    if (!worldCondition.parse()) {
                        ErrorLogger.errorInFile("The world condition is invalid", fileName);
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            ErrorLogger.errorInFile("The world condition was not formatted correctly", fileName);
            return;
        }
        // -------------------------------------------- //
        // COOLDOWN CONDITION
        // -------------------------------------------- //
        try {
            if (soundConfiguration.contains("condition.cooldown")) {
                Long cooldown = soundConfiguration.getLong("condition.cooldown");
                cooldownCondition = new CooldownCondition(cooldown);
                if (!cooldownCondition.parse()) {
                    ErrorLogger.errorInFile("The cooldown condition is invalid", fileName);
                    return;
                }
            }
        } catch (Exception ex) {
            ErrorLogger.errorInFile("The cooldown condition was not formatted correctly", fileName);
            return;
        }
        // -------------------------------------------- //
        // PARSED COMPLETELY
        // -------------------------------------------- //
        loaded = true;
    }

    // -------------------------------------------- //
    // GETTERS
    // -------------------------------------------- //
    private boolean isLoaded() {
        return loaded;
    }

    private Float getVolumeBetween(Float minVolume, Float maxVolume) {
        return minVolume + main.random.nextFloat() * (maxVolume - minVolume);
    }

    public List<String> getSoundNames() {
        return soundNames;
    }

    private String getRandomSoundName() {
        try {
            return soundNames.get(main.random.nextInt(soundNames.size()));
        } catch (IllegalArgumentException ex) {
            return soundNames.get(0);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public String getSubtitle() {
        return subtitle;
    }

    // -------------------------------------------- //
    // RUNNER
    // -------------------------------------------- //
    public void run(Player player, Location location) {
        if (!isLoaded()) return;
        World world = location.getWorld();
        if (weatherCondition != null) {
            if (!weatherCondition.isTrue(world)) return;
        }
        if (timeCondition != null) {
            if (!timeCondition.isTrue(world)) return;
        }
        if (biomeCondition != null) {
            if (!biomeCondition.isTrue(location)) return;
        }
        if (altitudeCondition != null) {
            if (!altitudeCondition.isTrue(location)) return;
        }
        if (worldCondition != null) {
            if (!worldCondition.isTrue(world)) return;
        }
        if (entityNearCondition != null) {
            if (entityNearCondition.isTrue(location)) return;
        }
        if (cooldownCondition != null) {
            if (cooldownCondition.isOnCooldown(player)) return;
        }
        double random = 100 * main.random.nextDouble();
        if (random <= chance) {
            playSound(player, location, getRandomSoundName(), minVolume, maxVolume, pitch);
        }
    }

    private void playSound(Player player, Location location, String soundName, Float minVolume, Float maxVolume, Float pitch) {
        cooldownCondition.setCooldown(player);
        player.playSound(location, soundName, getVolumeBetween(minVolume, maxVolume), pitch);
    }

    public void forceRun(Player player) {
        player.playSound(player.getLocation(), getRandomSoundName(), getVolumeBetween(minVolume, maxVolume), pitch);
    }
}
