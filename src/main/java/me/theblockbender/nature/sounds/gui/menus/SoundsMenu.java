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
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;

public class SoundsMenu {
    // * list of all sounds
    // * onClick to sound properties
    //
    // * refresh button
    // * add sounds button
    // * exit button
    // * paginated

    private PaginatedMenu menu;


    public SoundsMenu(NatureSounds main) {
        menu = new PaginatedMenu("§7Sounds §a»§7 List");
        MenuButton refresh = new MenuButton(new UtilItem(Material.BUBBLE_CORAL)
                .setName("§5Refresh List")
                .setLore("§8Reload, again", "", "§7Refresh the menu you are currently", "§7viewing and read all files again.", "", "§a➡ Click to refresh this menu")
                .hideFlags().create());
        refresh.setHandler(event -> {
            event.setCancelled(true);
            Bukkit.getScheduler().runTask(main, () -> menu.refreshInventory(event.getWhoClicked()));
        });
        MenuButton create = new MenuButton(new UtilItem(Material.TUBE_CORAL)
                .setName("§8Create New Sound")
                .setLore("§8New, add", "", "§7Add a new sound configuration", "§7and modify its settings.", "", "§a➡ Click to continue")
                .hideFlags().create());
        create.setHandler(event -> {
            event.setCancelled(true);
            Bukkit.getScheduler().runTask(main, () -> {
                event.getWhoClicked().closeInventory();
                main.menus.soundPropertiesMenu.show(event.getWhoClicked());
            });
        });
        menu.setEveryPageItem(46, refresh);
        menu.setEveryPageItem(48, create);
        for (Sound sound : main.getSounds()) {
            MenuButton button = new MenuButton(new UtilItem(Material.DROWNED_SPAWN_EGG)
                    .setName("§6Sound: §e" + sound.getFileName().replace(".yml", ""))
                    .setLore(sound.getLore())
                    .hideFlags().create());
            button.setHandler(event -> {
                event.setCancelled(true);
                Bukkit.getScheduler().runTask(main, () -> {
                    event.getWhoClicked().closeInventory();
                    main.menus.currentlyModifying.put(event.getWhoClicked().getUniqueId(), sound);
                    main.menus.soundPropertiesMenu.show(event.getWhoClicked());
                });
            });
            menu.addContentItem(button);
        }
    }

    public void show(HumanEntity player) {
        player.openInventory(menu.getInventory());
    }
}
