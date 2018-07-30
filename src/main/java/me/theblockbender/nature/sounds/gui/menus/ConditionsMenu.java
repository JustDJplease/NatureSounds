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

import me.theblockbender.nature.sounds.NatureSounds;
import me.theblockbender.nature.sounds.gui.Menu;
import org.bukkit.entity.HumanEntity;

public class ConditionsMenu {
    // toggleable conditions
    // upon condition click. open from condition class.
    //
    // back to sound properties button
    private Menu menu;


    public ConditionsMenu(NatureSounds main) {
        menu = new Menu("§7Sounds §a»§7 Conditions");
        // TODO add conditions.
    }

    public void show(HumanEntity player) {
        player.openInventory(menu.getInventory());
    }
}
