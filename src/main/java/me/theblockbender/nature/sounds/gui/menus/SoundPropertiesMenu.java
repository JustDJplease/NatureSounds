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
import me.theblockbender.nature.sounds.gui.Menu;
import me.theblockbender.nature.sounds.gui.MenuButton;
import me.theblockbender.nature.sounds.utilities.UtilItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.List;

public class SoundPropertiesMenu {
    private Menu menu;
    private NatureSounds main;

    public SoundPropertiesMenu(NatureSounds main) {
        this.main = main;
        menu = new Menu("§7Sounds §a»§7 Sound");
    }

    void show(HumanEntity whoClicked) {
        Sound sound = main.menus.currentlyModifying.get(whoClicked.getUniqueId());
        if (sound == null) {
            //set items to default.
            MenuButton soundNames = new MenuButton(new UtilItem(Material.NOTE_BLOCK).setName("§6Add music files").setLore("§7§lMusic files§7:", "§7  - None", "", "§7Click to add music files").create());
            soundNames.setHandler(event -> {
                event.setCancelled(true);
                Bukkit.getScheduler().runTask(main, () -> {
                    event.getWhoClicked().closeInventory();
                    main.menus.oggFilesMenu.show(event.getWhoClicked());
                });
            });
            menu.setButton(10, soundNames);
            MenuButton conditions = new MenuButton(new UtilItem(Material.COMPASS).setName("§6Modify Conditions").setLore("§7§lConditions§7:", "§7  - None", "", "§7Click to add conditions").create());
            conditions.setHandler(event -> {
                event.setCancelled(true);
                Bukkit.getScheduler().runTask(main, () -> {
                    event.getWhoClicked().closeInventory();
                    main.menus.oggFilesMenu.show(event.getWhoClicked());
                });
            });
            menu.setButton(11, conditions);
        } else {
            //set items to sound.
            MenuButton soundNames = new MenuButton(new UtilItem(Material.NOTE_BLOCK).setName("§6Add music files").setLore(getLore(sound)).create());
            soundNames.setHandler(event -> {
                event.setCancelled(true);
                Bukkit.getScheduler().runTask(main, () -> {
                    event.getWhoClicked().closeInventory();
                    main.menus.oggFilesMenu.show(event.getWhoClicked());
                });
            });
            menu.setButton(10, soundNames);
            // TODO condition lore.
            MenuButton conditions = new MenuButton(new UtilItem(Material.COMPASS).setName("§6Modify Conditions").setLore("§7§lConditions§7:", "§7  - ERROR", "", "§7Click to add conditions").create());
            conditions.setHandler(event -> {
                event.setCancelled(true);
                Bukkit.getScheduler().runTask(main, () -> {
                    event.getWhoClicked().closeInventory();
                    main.menus.oggFilesMenu.show(event.getWhoClicked());
                });
            });
            menu.setButton(11, conditions);
        }
        whoClicked.openInventory(menu.getInventory());
    }

    private List<String> getLore(Sound sound) {
        List<String> lore = new ArrayList<>();
        lore.add("§7§lMusic files§7:");
        if (sound.getSoundNames().size() < 1) {
            lore.add("§7  - None");
        } else {
            for (String name : sound.getSoundNames()) {
                lore.add("§7  - " + name);
            }
        }
        lore.add(" ");
        lore.add("§7Click to add music files");
        return lore;
    }

    // Allows to configure basic sound properties
    // has overview of advanced settings per sound.
    //
    // Subtitle (anvil input)
    // MaxMin vol & pitch & chance
    // button to ogg list.
    //
    // button to return to sounds list
    // button to condition settings

}
