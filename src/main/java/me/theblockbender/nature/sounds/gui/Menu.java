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


import me.theblockbender.nature.sounds.utilities.UtilItem;
import me.theblockbender.nature.sounds.utilities.UtilTask;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu implements InventoryHolder {

    // -------------------------------------------- //
    // INSTANCES
    // -------------------------------------------- //
    private final String name;
    private final List<Integer> frame_slots = new ArrayList<>();
    private final Map<Integer, MenuButton> all_items = new HashMap<>();

    {
        frame_slots.add(0);
        frame_slots.add(1);
        frame_slots.add(2);
        frame_slots.add(3);
        frame_slots.add(4);
        frame_slots.add(5);
        frame_slots.add(6);
        frame_slots.add(7);
        frame_slots.add(8);
        frame_slots.add(9);
        frame_slots.add(17);
        frame_slots.add(18);
        frame_slots.add(26);
        frame_slots.add(27);
        frame_slots.add(35);
        frame_slots.add(36);
        frame_slots.add(37);
        frame_slots.add(38);
        frame_slots.add(39);
        frame_slots.add(40);
        frame_slots.add(41);
        frame_slots.add(42);
        frame_slots.add(43);
        frame_slots.add(44);
    }

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public Menu(String name) {
        this.name = name;
    }

    // -------------------------------------------- //
    // SETTERS & GETTERS
    // -------------------------------------------- //
    public void setButton(int slot, MenuButton button) {
        all_items.put(slot, button);
    }

    public MenuButton getButton(int slot) {
        if (slot < 0 || slot > 53) return null;
        if ((all_items.size() - 1) < slot) return null;
        return all_items.get(slot);
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 54, name);

        MenuButton frame = getFrameButton();
        for (int slot : frame_slots) {
            inventory.setItem(slot, frame.getItemStack());
            all_items.put(slot, frame);
        }

        MenuButton exit = getExitButton();
        inventory.setItem(52, exit.getItemStack());
        all_items.put(52, exit);

        for (Map.Entry<Integer, MenuButton> entry : all_items.entrySet()) {
            int slot = entry.getKey();
            if (slot < 0 || slot > 53) continue;
            inventory.setItem(slot, entry.getValue().getItemStack());
        }

        return inventory;
    }

    // -------------------------------------------- //
    // MENU BUTTONS
    // -------------------------------------------- //
    private MenuButton getFrameButton() {
        return new MenuButton(new UtilItem(Material.BLACK_STAINED_GLASS_PANE)
                .setName("§7")
                .hideFlags().create());
    }

    private MenuButton getExitButton() {
        MenuButton exit = new MenuButton(new UtilItem(Material.FIRE_CORAL)
                .setName("§c§lExit")
                .setLore("§8leave this menu", "", "§7Close the menu you are currently", "§7viewing and return to the game.", "", "§b➜ Click to close this menu")
                .hideFlags().create());
        exit.setHandler(event -> UtilTask.sync(task -> event.getWhoClicked().closeInventory()));
        return exit;
    }
}

