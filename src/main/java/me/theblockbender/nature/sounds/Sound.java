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

import java.util.List;

public class Sound {
    // -------------------------------------------- //
    // IDENTIFIER & MAIN
    // -------------------------------------------- //
    private String name;
    private NatureSounds main;
    private boolean loaded;

    // -------------------------------------------- //
    // CONDITIONS
    // -------------------------------------------- //
    private TimeCondition timeCondition;
    private BiomeCondition biomeCondition;
    private AltitudeCondition altitudeCondition;
    private WeatherCondition weatherCondition;
    private WorldCondition worldCondition;
    private CooldownCondition cooldownCondition;
    private Double chance;

    // -------------------------------------------- //
    // SOUND PROPERTIES
    // -------------------------------------------- //
    private String soundName;
    private Float minVolume;
    private Float maxVolume;
    private Float pitch;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    Sound(NatureSounds main, String fileName, YamlConfiguration soundConfiguration) {
        loaded = false;
        assert main != null;
        this.main = main;
        // -------------------------------------------- //
        // FILENAME
        // -------------------------------------------- //
        if (fileName == null) {
            main.outputError("File name cannot be null for sound files.");
            return;
        }
        name = fileName;
        // -------------------------------------------- //
        // CONFIGURATION SECTION
        // -------------------------------------------- //
        if (soundConfiguration == null) {
            main.outputError("Sound Configuration cannot be null");
            return;
        }
        // -------------------------------------------- //
        // SOUND NAME
        // -------------------------------------------- //
        try {
            soundName = soundConfiguration.getString("sound.name");
        } catch (NullPointerException ex) {
            main.outputError("Invalid sound name inside soundconfiguration file " + fileName);
            return;
        }
        // -------------------------------------------- //
        // SOUND MIN VOLUME
        // -------------------------------------------- //
        try {
            minVolume = Float.parseFloat("" + soundConfiguration.getDouble("sound.minVolume"));
            if (minVolume < 0) {
                main.outputError("Invalid minVolume (< 0) inside soundconfiguration file " + fileName);
                return;
            }
        } catch (Exception ex) {
            main.outputError("Invalid minVolume inside soundconfiguration file " + fileName);
            return;
        }
        // -------------------------------------------- //
        // SOUND MAX VOLUME
        // -------------------------------------------- //
        try {
            maxVolume = Float.parseFloat("" + soundConfiguration.getDouble("sound.maxVolume"));
            if (maxVolume < 0 || maxVolume < minVolume) {
                main.outputError("Invalid maxVolume (< 0 or < minVolume) inside soundconfiguration file " + fileName);
                return;
            }
        } catch (Exception ex) {
            main.outputError("Invalid maxVolume inside soundconfiguration file " + fileName);
            return;
        }
        // -------------------------------------------- //
        // SOUND PITCH
        // -------------------------------------------- //
        try {
            pitch = Float.parseFloat("" + soundConfiguration.getDouble("sound.pitch"));
            if (pitch < 0 || pitch > 2) {
                main.outputError("Invalid pitch (< 0 or > 2) inside soundconfiguration file " + fileName);
                return;
            }
        } catch (Exception ex) {
            main.outputError("Invalid pitch inside soundconfiguration file " + fileName);
            return;
        }
        // -------------------------------------------- //
        // CHANCE
        // -------------------------------------------- //
        try {
            chance = soundConfiguration.getDouble("chance");
            if (chance < 0 || chance > 100) {
                main.outputError("Invalid chance (< 0 or > 100) inside soundconfiguration file " + fileName);
                return;
            }
        } catch (Exception ex) {
            main.outputError("Invalid chance inside soundconfiguration file " + fileName);
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
                        main.outputError("Invalid weatherCondition (unknown weather type) inside soundconfiguration file " + fileName);
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            main.outputError("Invalid weatherCondition inside soundconfiguration file " + fileName);
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
                        main.outputError("Invalid altitudeCondition (< 0 or > 256 or above > below) inside soundconfiguration file " + fileName);
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            main.outputError("Invalid altitudeCondition inside soundconfiguration file " + fileName);
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
                        main.outputError("Invalid biomeCondition (unknown biome type) inside soundconfiguration file " + fileName);
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            main.outputError("Invalid biomeCondition inside soundconfiguration file " + fileName);
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
                        main.outputError("Invalid timeCondition (< 0 or > 24000 or after > before) inside soundconfiguration file " + fileName);
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            main.outputError("Invalid timeCondition inside soundconfiguration file " + fileName);
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
                        main.outputError("Invalid worldCondition (unknown world) inside soundconfiguration file " + fileName);
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            main.outputError("Invalid worldCondition inside soundconfiguration file " + fileName);
            return;
        }
        // -------------------------------------------- //
        // COOLDOWN CONDITION
        // -------------------------------------------- //
        try {
            if (soundConfiguration.contains("condition.cooldown")) {
                Long cooldown = soundConfiguration.getLong("condition.cooldown");
                if (cooldown != 0) {
                    cooldownCondition = new CooldownCondition(cooldown);
                    if (!cooldownCondition.parse()) {
                        main.outputError("Invalid cooldownCondition (< 0) inside soundconfiguration file " + fileName);
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            main.outputError("Invalid cooldownCondition inside soundconfiguration file " + fileName);
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

    public String getFileName() {
        return name;
    }

    private Float getVolumeBetween(Float minVolume, Float maxVolume) {
        return minVolume + main.random.nextFloat() * (maxVolume - minVolume);
    }

    public String getSoundName() {
        return soundName;
    }

    // -------------------------------------------- //
    // RUNNER
    // -------------------------------------------- //
    public boolean run(Player player, Location location) {
        if (!isLoaded()) return false;
        main.debug("+ Sound loaded");
        World world = location.getWorld();
        if (weatherCondition != null) {
            if (!weatherCondition.isTrue(world)) return false;
            main.debug("+ Weather passed");
        }
        if (timeCondition != null) {
            if (!timeCondition.isTrue(world)) return false;
            main.debug("+ Time passed");
        }
        if (biomeCondition != null) {
            if (!biomeCondition.isTrue(location)) return false;
            main.debug("+ Biome passed");
        }
        if (altitudeCondition != null) {
            if (!altitudeCondition.isTrue(location)) return false;
            main.debug("+ Altitude passed");
        }
        if (weatherCondition != null) {
            if (!worldCondition.isTrue(world)) return false;
            main.debug("+ World passed");
        }
        if (cooldownCondition != null) {
            if (cooldownCondition.isOnCooldown(player)) return false;
            main.debug("+ Cooldown passed");
        }
        double random = 100 * main.random.nextDouble();
        if (chance > random) return false;
        main.debug("+ Chance passed");
        playSound(player, location, soundName, minVolume, maxVolume, pitch);
        return true;
    }

    private void playSound(Player player, Location location, String soundName, Float minVolume, Float maxVolume, Float pitch) {
        cooldownCondition.setCooldown(player);
        player.playSound(location, soundName, getVolumeBetween(minVolume, maxVolume), pitch);
    }
}
