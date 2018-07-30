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
import me.theblockbender.nature.sounds.Sound;
import me.theblockbender.nature.sounds.gui.MenuButton;
import me.theblockbender.nature.sounds.gui.PaginatedMenu;
import me.theblockbender.nature.sounds.utilities.UtilItem;
import me.theblockbender.nature.sounds.utilities.UtilTask;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;

import java.io.File;
import java.util.Objects;

public class OggFilesMenu {

    // -------------------------------------------- //
    // INSTANCES
    // -------------------------------------------- //
    private final NatureSounds main;
    private final PaginatedMenu menu;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public OggFilesMenu(NatureSounds main) {
        this.main = main;
        menu = new PaginatedMenu("§7Sounds §b»§7 Sound Files");
        MenuButton refresh = getRefreshButton();
        menu.setEveryPageItem(46, refresh);
    }

    // -------------------------------------------- //
    // GETTERS
    // -------------------------------------------- //
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File[] getOggFiles() {
        File folder = new File(main.getDataFolder() + File.separator + "sounds");
        if (!folder.exists()) return null;
        return folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".ogg"));
    }

    // -------------------------------------------- //
    // GUI
    // -------------------------------------------- //
    @SuppressWarnings("WeakerAccess")
    // REMOVE <---
    void show(HumanEntity player) {
        menu.clearContentSlots();
        Sound sound = main.menus.currentlyModifying.get(player.getUniqueId());
        for (File file : Objects.requireNonNull(getOggFiles())) {
            MenuButton button;
            if (sound.getSoundNames().contains(file.getName())) {
                button = new MenuButton(new UtilItem(Material.MAGMA_CREAM).setName("§9§l" + file.getName())
                        .setLore("§8Sound file", "", "§7This sound is currently added", "§7and will be played whenever this", "§7sound configuration matches all conditions.", "", "§b➜ Click to remove this sound")
                        .addEnchant(Enchantment.DURABILITY, 2)
                        .hideFlags().create());
                button.setHandler(event -> {
                    // TODO REMOVE SOUND FROM FILE.
                    event.setCancelled(true);
                    Bukkit.getScheduler().runTask(main, () -> {
                        event.getWhoClicked().closeInventory();
                        show(event.getWhoClicked());
                    });
                });
            } else {
                button = new MenuButton(new UtilItem(Material.MAGMA_CREAM).setName("§9§l" + file.getName())
                        .setLore("§8Sound file", "", "§7This sound is NOT added to this", "§7sound configuration.", "", "§b➜ Click to add this sound")
                        .hideFlags().create());
                button.setHandler(event -> {
                    // TODO ADD SOUND TO FILE.
                    event.setCancelled(true);
                    Bukkit.getScheduler().runTask(main, () -> {
                        event.getWhoClicked().closeInventory();
                        show(event.getWhoClicked());
                    });
                });
            }
            menu.addContentItem(button);
        }
        player.openInventory(menu.getInventory());
    }

    // -------------------------------------------- //
    // MENU BUTTONS
    // -------------------------------------------- //
    private MenuButton getRefreshButton() {
        MenuButton refresh = new MenuButton(new UtilItem(Material.BUBBLE_CORAL)
                .setName("§5§lRefresh")
                .setLore("§8Reload this page", "", "§7Refresh the menu you are currently", "§7viewing and read all files again.", "", "§b➜ Click to refresh this menu")
                .hideFlags().create());
        refresh.setHandler(event -> {
            event.setCancelled(true);
            UtilTask.sync(task -> {
                event.getWhoClicked().closeInventory();
                show(event.getWhoClicked());
            });
        });
        return refresh;
    }
}
