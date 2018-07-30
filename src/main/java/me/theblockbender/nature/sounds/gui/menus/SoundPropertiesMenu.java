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

package me.theblockbender.nature.sounds.gui.menus;

import me.theblockbender.nature.sounds.Lang;
import me.theblockbender.nature.sounds.NatureSounds;
import me.theblockbender.nature.sounds.Sound;
import me.theblockbender.nature.sounds.gui.Menu;
import org.bukkit.entity.HumanEntity;

public class SoundPropertiesMenu {

    // -------------------------------------------- //
    // INSTANCES
    // -------------------------------------------- //
    private final NatureSounds main;
    private final Menu menu;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public SoundPropertiesMenu(NatureSounds main) {
        this.main = main;
        menu = new Menu("§7Sounds §b»§7 Editor");
    }

    // -------------------------------------------- //
    // GUI
    // -------------------------------------------- //
    void show(HumanEntity whoClicked) {
        Sound sound = main.menus.currentlyModifying.get(whoClicked.getUniqueId());
        if (sound == null) {
            //set items to default.
            whoClicked.sendMessage(Lang.color("<error>TODO: Creating a new sound"));
        } else {
            //set items to sound.
            whoClicked.sendMessage(Lang.color("<error>TODO: Editing sound: " + sound.getFileName()));
        }
        whoClicked.openInventory(menu.getInventory());
    }
}
