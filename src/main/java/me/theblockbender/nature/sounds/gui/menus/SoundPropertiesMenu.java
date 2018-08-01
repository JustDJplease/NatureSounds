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
import me.theblockbender.nature.sounds.gui.MenuButton;
import me.theblockbender.nature.sounds.utilities.UtilItem;
import me.theblockbender.nature.sounds.utilities.UtilTask;
import org.bukkit.Material;
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
            menu.setButton(11, getNameButton());

            menu.setButton(19, getLocationConditionButton());
            menu.setButton(20, getBiomeConditionButton());
            menu.setButton(21, getRegionConditionButton());
            menu.setButton(22, getTimeConditionButton());
            menu.setButton(23, getWeatherConditionButton());
            menu.setButton(24, getEntityConditionButton());
            menu.setButton(25, getCooldownConditionButton());
        } else {
            //set items to sound.
            // TODO for SOUND
            menu.setButton(11, getNameButton());

            menu.setButton(19, getLocationConditionButton());
            menu.setButton(20, getBiomeConditionButton());
            menu.setButton(21, getRegionConditionButton());
            menu.setButton(22, getTimeConditionButton());
            menu.setButton(23, getWeatherConditionButton());
            menu.setButton(24, getEntityConditionButton());
            menu.setButton(25, getCooldownConditionButton());
        }
        whoClicked.openInventory(menu.getInventory());
    }


    // -------------------------------------------- //
    // MENU BUTTONS
    // -------------------------------------------- //
    private MenuButton getNameButton() {
        MenuButton condition = new MenuButton(new UtilItem(Material.NAME_TAG)
                .setName("§9§lName: unset")
                .setLore("§8Set a name for your sound", "", "§7Not set for current sound configuration.", "", "§b➜ Click to set a name")
                .hideFlags().create());
        condition.setHandler(event -> UtilTask.sync(task -> {
            event.getWhoClicked().closeInventory();
            event.getWhoClicked().sendMessage(Lang.color("<error>TODO: setting name gui"));
        }));
        return condition;
    }

    private MenuButton getLocationConditionButton() {
        MenuButton condition = new MenuButton(new UtilItem(Material.PLAYER_HEAD)
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmUyY2M0MjAxNWU2Njc4ZjhmZDQ5Y2NjMDFmYmY3ODdmMWJhMmMzMmJjZjU1OWEwMTUzMzJmYzVkYjUwIn19fQ==")
                .setName("§aLocation Condition")
                .setLore("§8Altitude and world conditions", "", "§7Not set for current sound configuration.", "", "§b➜ Click to configure")
                .hideFlags().create());
        condition.setHandler(event -> UtilTask.sync(task -> {
            event.getWhoClicked().closeInventory();
            main.menus.conditionsMenu.show(event.getWhoClicked());
        }));
        return condition;
    }

    private MenuButton getRegionConditionButton() {
        MenuButton condition = new MenuButton(new UtilItem(Material.PLAYER_HEAD)
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTdlNTYxNDA2ODZlNDc2YWVmNTUyMGFjYmFiYzIzOTUzNWZmOTdlMjRiMTRkODdmNDk4MmYxMzY3NWMifX19")
                .setName("§aRegion Condition")
                .setLore("§8WorldGuard region conditions", "", "§7Not set for current sound configuration.", "", "§b➜ Click to configure")
                .hideFlags().create());
        condition.setHandler(event -> UtilTask.sync(task -> {
            event.getWhoClicked().closeInventory();
            main.menus.conditionsMenu.show(event.getWhoClicked());
        }));
        return condition;
    }

    private MenuButton getBiomeConditionButton() {
        MenuButton condition = new MenuButton(new UtilItem(Material.PLAYER_HEAD)
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzk1ZDM3OTkzZTU5NDA4MjY3ODQ3MmJmOWQ4NjgyMzQxM2MyNTBkNDMzMmEyYzdkOGM1MmRlNDk3NmIzNjIifX19")
                .setName("§aBiome Condition")
                .setLore("§8Biome type conditions", "", "§7Not set for current sound configuration.", "", "§b➜ Click to configure")
                .hideFlags().create());
        condition.setHandler(event -> UtilTask.sync(task -> {
            event.getWhoClicked().closeInventory();
            main.menus.conditionsMenu.show(event.getWhoClicked());
        }));
        return condition;
    }

    private MenuButton getTimeConditionButton() {
        MenuButton condition = new MenuButton(new UtilItem(Material.PLAYER_HEAD)
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQ3N2RhZmM4YzllYTA3OTk2MzMzODE3OTM4NjZkMTQ2YzlhMzlmYWQ0YzY2ODRlNzExN2Q5N2U5YjZjMyJ9fX0=")
                .setName("§aTime Condition")
                .setLore("§8Day and night conditions", "", "§7Not set for current sound configuration.", "", "§b➜ Click to configure")
                .hideFlags().create());
        condition.setHandler(event -> UtilTask.sync(task -> {
            event.getWhoClicked().closeInventory();
            main.menus.conditionsMenu.show(event.getWhoClicked());
        }));
        return condition;
    }

    private MenuButton getWeatherConditionButton() {
        MenuButton condition = new MenuButton(new UtilItem(Material.PLAYER_HEAD)
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjg5MDQyMDgyYmI3YTc2MThiNzg0ZWU3NjA1YTEzNGM1ODgzNGUyMWUzNzRjODg4OTM3MTYxMDU3ZjZjNyJ9fX0=")
                .setName("§aWeather Condition")
                .setLore("§8Sun, rain and thunder conditions", "", "§7Not set for current sound configuration.", "", "§b➜ Click to configure")
                .hideFlags().create());
        condition.setHandler(event -> UtilTask.sync(task -> {
            event.getWhoClicked().closeInventory();
            main.menus.conditionsMenu.show(event.getWhoClicked());
        }));
        return condition;
    }

    private MenuButton getEntityConditionButton() {
        MenuButton condition = new MenuButton(new UtilItem(Material.PLAYER_HEAD)
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWM3OTc0ODJhMTRiZmNiODc3MjU3Y2IyY2ZmMWI2ZTZhOGI4NDEzMzM2ZmZiNGMyOWE2MTM5Mjc4YjQzNmIifX19")
                .setName("§aEntity Condition")
                .setLore("§8Nearby entity conditions", "", "§7Not set for current sound configuration.", "", "§b➜ Click to configure")
                .hideFlags().create());
        condition.setHandler(event -> UtilTask.sync(task -> {
            event.getWhoClicked().closeInventory();
            main.menus.conditionsMenu.show(event.getWhoClicked());
        }));
        return condition;
    }

    private MenuButton getCooldownConditionButton() {
        MenuButton condition = new MenuButton(new UtilItem(Material.PLAYER_HEAD)
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlN2Q0NjMyMjQ3N2Q2MWQ0MWMxODc4OGY1YzFhZmQyNGVkNTI2ZWIzZWQ4NDEyN2YyMTJlMjUxNWIxODgzIn19fQ==")
                .setName("§aCooldown Condition")
                .setLore("§8Time waiting conditions", "", "§7Not set for current sound configuration.", "", "§b➜ Click to configure")
                .hideFlags().create());
        condition.setHandler(event -> UtilTask.sync(task -> {
            event.getWhoClicked().closeInventory();
            main.menus.conditionsMenu.show(event.getWhoClicked());
        }));
        return condition;
    }
}
