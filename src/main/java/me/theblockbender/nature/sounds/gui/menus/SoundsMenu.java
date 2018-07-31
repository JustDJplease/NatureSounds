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
import net.wesjd.anvilgui.AnvilGUI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.io.File;

public class SoundsMenu {

    // -------------------------------------------- //
    // INSTANCES
    // -------------------------------------------- //
    private final NatureSounds main;
    private final PaginatedMenu menu;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public SoundsMenu(NatureSounds main) {
        this.main = main;
        menu = new PaginatedMenu("§7Sounds §b»§7 List");
        MenuButton refresh = getRefreshButton();
        menu.setEveryPageItem(46, refresh);
    }

    // -------------------------------------------- //
    // GUI
    // -------------------------------------------- //
    public void show(HumanEntity player) {
        MenuButton create = getCreateButton((Player) player);
        menu.setEveryPageItem(48, create);
        menu.clearContentSlots();
        for (Sound sound : main.getSounds()) {
            MenuButton button = new MenuButton(new UtilItem(Material.DROWNED_SPAWN_EGG)
                    .setName("§9§lSound: " + StringUtils.capitalize(sound.getFileName().replace(".yml", "").replace("_", " ")))
                    .setLore(sound.getDescriptiveLore())
                    .hideFlags().create());
            button.setHandler(event -> UtilTask.sync(task -> {
                event.getWhoClicked().closeInventory();
                main.menus.currentlyModifying.put(event.getWhoClicked().getUniqueId(), sound);
                main.menus.soundPropertiesMenu.show(event.getWhoClicked());
            }));
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
        refresh.setHandler(event -> UtilTask.sync(task -> {
            event.getWhoClicked().closeInventory();
            show(event.getWhoClicked());
        }));
        return refresh;
    }

    private MenuButton getCreateButton(Player myPlayer) {
        MenuButton create = new MenuButton(new UtilItem(Material.HORN_CORAL)
                .setName("§6§lCreate")
                .setLore("§8Create new sound", "", "§7Add a new sound configuration", "§7and modify its settings.", "", "§b➜ Click to continue")
                .hideFlags().create());
        create.setHandler(event -> UtilTask.sync(task -> {
            event.getWhoClicked().closeInventory();
            new AnvilGUI(main, myPlayer, "What should the sound be named?", (player, reply) -> {
                File file = new File(main.getDataFolder() + File.separator + "sounds" + File.separator + reply + ".ogg");
                if (file.exists()) return "This file already exists!";
                //TODO create new sound file.
                UtilTask.sync(task1 -> main.menus.soundPropertiesMenu.show(event.getWhoClicked()));
                return null;
            });
        }));
        return create;
    }
}
