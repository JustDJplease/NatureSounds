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
import me.theblockbender.nature.sounds.gui.MenuButton;
import me.theblockbender.nature.sounds.gui.PaginatedMenu;
import me.theblockbender.nature.sounds.utilities.UtilItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;

import java.io.File;

public class OggFilesMenu {
    // lists all .ogg files in the sounds folder
    // sounds already added = enchanted magma cream
    // sounds not already added = un-enchanted slime
    // onClick - add sound to sound list.
    //
    // refresh button
    // sort button (alphabetical, date added)
    // paginated
    // return to sound properties button

    private NatureSounds main;
    private PaginatedMenu menu;


    public OggFilesMenu(NatureSounds main) {
        this.main = main;
        menu = new PaginatedMenu("§7Sounds §a»§7 Ogg Files");
        MenuButton refresh = new MenuButton(new UtilItem(Material.ANVIL).setName("§a§lRefresh").hideFlags().create());
        refresh.setHandler(event -> {
            event.setCancelled(true);
            Bukkit.getScheduler().runTask(main, () -> menu.refreshInventory(event.getWhoClicked()));
        });
        menu.setEveryPageItem(46, refresh);
        for (File file : getOggFiles()) {
            // TODO fancy item.
            MenuButton button = new MenuButton(new UtilItem(Material.WRITTEN_BOOK).setName("§e§l" + file.getName()).hideFlags().create());
            button.setHandler(event -> {
                event.setCancelled(true);
                Bukkit.getScheduler().runTask(main, () -> {
                    // TODO select / deselect.
                    event.getWhoClicked().closeInventory();
                    main.menus.soundPropertiesMenu.show(event.getWhoClicked());
                });
            });
            menu.addContentItem(button);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File[] getOggFiles() {
        File folder = new File(main.getDataFolder() + File.separator + "sounds");
        if (!folder.exists())
            folder.mkdirs();
        return folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".ogg"));
    }

    public void show(HumanEntity player) {
        player.openInventory(menu.getInventory());
    }

}
