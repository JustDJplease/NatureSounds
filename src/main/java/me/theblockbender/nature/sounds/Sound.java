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
import me.theblockbender.nature.sounds.conditions.*;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Finished
 */
public class Sound {
    @Getter @Setter public boolean loaded;
    @Getter @Setter public String fileName;
    // -------------------------------------------- //
    // SOUND PROPERTIES
    // -------------------------------------------- //
    @Getter @Setter public List<String> soundNames = new ArrayList<>();

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
    private RegionCondition regionCondition;
    @Getter @Setter public float minVolume;
    @Getter @Setter public float maxVolume;
    @Getter @Setter public float pitch;
    @Getter @Setter public String subtitle;
    @Getter @Setter public Double chance;
    // -------------------------------------------- //
    // IDENTIFIER & MAIN
    // -------------------------------------------- //
    @Getter @Setter private NatureSounds main;


    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //

    /**
     * Create a new Sound Instance.
     *
     * @param main               Instance of the Main Class, extending JavaPlugin
     * @param fileName           Name of the file this Sound Instance is created from.
     * @param soundConfiguration YAMLConfiguration this Sound Instance is created from.
     */
    public Sound(NatureSounds main, String fileName, FileConfiguration soundConfiguration) {
        assert main != null : "Tried to load a sound file without the main instance.";
        assert fileName != null : "Tried to load a sound file without a name.";
        assert soundConfiguration != null : "Tried to load an invalid configuration file.";

        setLoaded(false);
        setMain(main);
        setFileName(fileName);

        setSoundNames(soundConfiguration.getStringList("sound.names"));
        setMinVolume(NumberUtils.toFloat(soundConfiguration.getDouble("sound.minVolume") + "", 0f));
        setMinVolume(NumberUtils.toFloat(soundConfiguration.getDouble("sound.maxVolume") + "", 1f));
        setPitch(NumberUtils.toFloat(soundConfiguration.getDouble("sound.pitch") + "", 1f));
        setSubtitle(soundConfiguration.getString("sound.subtitle"));
        setChance(NumberUtils.toDouble(soundConfiguration.getDouble("chance") + "", 50d));

        assert !soundNames.isEmpty() : "No music file names specified in " + fileName;
        assert minVolume >= 0f : "Min-volume cannot be negative in " + fileName;
        assert maxVolume >= 0f && maxVolume > minVolume : "Max-volume cannot be negative or smaller than min-volume in " + fileName;
        assert pitch >= 0f && pitch <= 2f : "Pitch was not a number between 0 and 2 in " + fileName;
        assert subtitle != null : "Subtitle was not specified in " + fileName;
        assert chance >= 0 && chance <= 100 : "Chance was not a number between 0 and 100 in " + fileName;

        weatherCondition = new WeatherCondition(soundConfiguration, fileName);
        altitudeCondition = new AltitudeCondition(soundConfiguration, fileName);
        biomeCondition = new BiomeCondition(soundConfiguration, fileName);
        entityNearCondition = new EntityNearCondition(soundConfiguration, fileName);
        timeCondition = new TimeCondition(soundConfiguration, fileName);
        worldCondition = new WorldCondition(soundConfiguration, fileName);
        cooldownCondition = new CooldownCondition(soundConfiguration, fileName);
        regionCondition = new RegionCondition(soundConfiguration, fileName, main.isWorldGuardHooked());

        setLoaded(true);
    }

    // -------------------------------------------- //
    // UTIL
    // -------------------------------------------- //

    /**
     * Get a random volume between the two volume bounds associated with this Sound object.
     *
     * @return Randomized volume.
     */
    private Float getRandomVolume() {
        return minVolume + main.getRandom().nextFloat() * (maxVolume - minVolume);
    }

    /**
     * Get a random music file name from the sound names associated with this Sound object.
     *
     * @return Randomized sound name from the sound names list.
     */
    private String getRandomSoundName() {
        return soundNames.get(main.getRandom().nextInt(soundNames.size()));
    }

    // -------------------------------------------- //
    // RUNNER
    // -------------------------------------------- //

    /**
     * Attempts to execute this sound, validate its conditions and then plays the sound.
     *
     * @param player Player that should hear the sound.
     */
    public void run(Player player) {
        if (!isLoaded()) return;
        Location location = player.getLocation();
        World world = location.getWorld();
        if (weatherCondition != null && weatherCondition.isEnabled()) {
            if (!weatherCondition.isTrue(world)) return;
        }
        if (timeCondition != null && timeCondition.isEnabled()) {
            if (!timeCondition.isTrue(world)) return;
        }
        if (biomeCondition != null && biomeCondition.isEnabled()) {
            if (!biomeCondition.isTrue(location)) return;
        }
        if (altitudeCondition != null && altitudeCondition.isEnabled()) {
            if (!altitudeCondition.isTrue(location)) return;
        }
        if (worldCondition != null && worldCondition.isEnabled()) {
            if (!worldCondition.isTrue(world)) return;
        }
        if (entityNearCondition != null && entityNearCondition.isEnabled()) {
            if (!entityNearCondition.isTrue(location)) return;
        }
        if (cooldownCondition != null && cooldownCondition.isEnabled()) {
            if (cooldownCondition.isOnCooldown(player)) return;
        }
        if (regionCondition != null && regionCondition.isEnabled()) {
            if (!regionCondition.isTrue(location)) return;
        }
        double random = 100 * main.random.nextDouble();
        if (random <= chance) {
            playSound(player, location, getRandomSoundName());
        }
    }

    /**
     * Plays a sound to the player.
     *
     * @param player    The player who should be hearing the sound.
     * @param location  The location this sound should be played from.
     * @param soundName The name of the sound to be played.
     */
    private void playSound(Player player, Location location, String soundName) {
        cooldownCondition.setCooldown(player);
        player.playSound(location, soundName, getRandomVolume(), pitch);
    }

    /**
     * Plays the sound to the player regardless of the conditions.
     *
     * @param player The player who should be hearing the sound.
     */
    public void forcePlaySound(Player player) {
        player.playSound(player.getLocation(), getRandomSoundName(), getRandomVolume(), pitch);
    }
}
