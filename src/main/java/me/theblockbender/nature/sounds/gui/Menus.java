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

package me.theblockbender.nature.sounds.gui;

import me.theblockbender.nature.sounds.NatureSounds;
import me.theblockbender.nature.sounds.gui.menus.ConditionsMenu;
import me.theblockbender.nature.sounds.gui.menus.OggFilesMenu;
import me.theblockbender.nature.sounds.gui.menus.SoundPropertiesMenu;
import me.theblockbender.nature.sounds.gui.menus.SoundsMenu;

public class Menus {

    // -------------------------------------------- //
    // INSTANCES
    // -------------------------------------------- //
    public final ConditionsMenu conditionsMenu;
    public final OggFilesMenu oggFilesMenu;
    public final SoundPropertiesMenu soundPropertiesMenu;
    public final SoundsMenu soundsMenu;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public Menus(NatureSounds main) {
        conditionsMenu = new ConditionsMenu(main);
        oggFilesMenu = new OggFilesMenu(main);
        soundPropertiesMenu = new SoundPropertiesMenu(main);
        soundsMenu = new SoundsMenu(main);
    }
}
