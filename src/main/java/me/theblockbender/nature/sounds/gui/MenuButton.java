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

import org.bukkit.inventory.ItemStack;

public class MenuButton {

    private final ItemStack item;
    // -------------------------------------------- //
    // INSTANCES
    // -------------------------------------------- //
    private ButtonHandler handler;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public MenuButton(ItemStack item) {
        this.item = item;
    }

    // -------------------------------------------- //
    // SETTERS & GETTERS
    // -------------------------------------------- //
    public ButtonHandler getHandler() {
        return handler;
    }

    public void setHandler(ButtonHandler handler) {
        this.handler = handler;
    }

    ItemStack getItemStack() {
        return item;
    }
}

