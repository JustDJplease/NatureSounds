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

package me.theblockbender.nature.sounds.listeners;

import me.theblockbender.nature.sounds.NatureSounds;
import me.theblockbender.nature.sounds.gui.Menu;
import me.theblockbender.nature.sounds.gui.MenuButton;
import me.theblockbender.nature.sounds.gui.PaginatedMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryListener implements Listener {

    private final NatureSounds main;

    public InventoryListener(NatureSounds main) {
        this.main = main;
    }

    // -------------------------------------------- //
    // EVENT
    // -------------------------------------------- //
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof Menu) {
            Menu menu = (Menu) event.getInventory().getHolder();
            MenuButton button = menu.getButton(event.getSlot());
            if (button != null && button.getHandler() != null) {
                button.getHandler().onClick(event);
            }
        }
        if (event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof PaginatedMenu) {
            PaginatedMenu menu = (PaginatedMenu) event.getInventory().getHolder();
            MenuButton button = menu.getButton(event.getSlot());
            if (button != null && button.getHandler() != null) {
                button.getHandler().onClick(event);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        //noinspection StatementWithEmptyBody // TODO remove <---
        if (event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof Menu) {
            // TODO save player progress async.
        }
        //noinspection StatementWithEmptyBody // TODO remove <---
        if (event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof PaginatedMenu) {
            // TODO save player progress async.
        }
        // TODO LAST STEP!
        main.menus.currentlyModifying.remove(event.getPlayer().getUniqueId());
    }
}

