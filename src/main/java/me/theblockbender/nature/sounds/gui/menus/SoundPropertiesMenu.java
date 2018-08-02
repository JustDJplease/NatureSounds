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
import me.theblockbender.nature.sounds.conditions.ConditionType;
import me.theblockbender.nature.sounds.gui.Menu;
import me.theblockbender.nature.sounds.gui.MenuButton;
import me.theblockbender.nature.sounds.utilities.UtilItem;
import me.theblockbender.nature.sounds.utilities.UtilTask;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

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
    void show(HumanEntity whoClicked, Sound sound) {
        if (sound == null) {
            whoClicked.sendMessage(Lang.color("<error>You tried to open a menu for a sound that does not exist!"));
            return;
        } else {
            //set items to sound.
            // TODO for SOUND
            menu.setButton(10, getSoundsButton(sound));
            menu.setButton(14, getMinVolumeButton(sound));
            menu.setButton(15, getMaxVolumeButton(sound));
            menu.setButton(28, getLocationConditionButton(sound));
            menu.setButton(29, getBiomeConditionButton(sound));
            menu.setButton(30, getRegionConditionButton(sound));
            menu.setButton(31, getTimeConditionButton(sound));
            menu.setButton(32, getWeatherConditionButton(sound));
            menu.setButton(33, getEntityConditionButton(sound));
            menu.setButton(34, getCooldownConditionButton(sound));
        }
        whoClicked.openInventory(menu.getInventory());
    }


    // -------------------------------------------- //
    // MENU BUTTONS
    // -------------------------------------------- //
    private MenuButton getSoundsButton(Sound sound) {
        MenuButton condition = new MenuButton(new UtilItem(Material.NAME_TAG)
                .setName("§9§lAssociate sound files")
                .setLore("§8Link .ogg files", "", "§7Click to open up a list of loaded", "§7sound files. (De)select them to modify", "§7this sound effect.", "", "§b➜ Click to continue")
                .hideFlags().create());
        condition.setHandler(event -> UtilTask.sync(task -> {
            event.getWhoClicked().closeInventory();
            main.menus.oggFilesMenu.show(event.getWhoClicked(), sound);
        }));
        return condition;
    }

    private MenuButton getMinVolumeButton(Sound sound) {
        MenuButton condition = new MenuButton(new UtilItem(Material.PLAYER_HEAD)
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNlZWI3N2Q0ZDI1NzI0YTljYWYyYzdjZGYyZDg4Mzk5YjE0MTdjNmI5ZmY1MjEzNjU5YjY1M2JlNDM3NmUzIn19fQ==")
                .setName("§aMinimal Volume")
                .setLore("§8Lower bound of the volume", "", "§7Currently set to " + sound.getMinVolume(), "", "§b➜ Click to modify")
                .hideFlags().create());
        condition.setHandler(event -> UtilTask.sync(task -> {
            event.getWhoClicked().closeInventory();
            HumanEntity clicker = event.getWhoClicked();
            clicker.closeInventory();
            if (!(clicker instanceof Player)) {
                clicker.sendMessage(Lang.color("<error>Only players can modify volume!"));
                return;
            }
            Player player = (Player) clicker;
            new AnvilGUI(main, player, "Enter a volume", (executor, reply) -> {
                double vol;
                try {
                    vol = Double.parseDouble(reply);
                } catch (NumberFormatException ex) {
                    executor.sendMessage(Lang.color("<error>You have to enter a number!"));
                    return "ERROR";
                }
                if (vol < 0) {
                    executor.sendMessage(Lang.color("<error>You have to enter a positive number!"));
                    return "ERROR";
                }
                if (vol > sound.getMaxVolume()) {
                    executor.sendMessage(Lang.color("<error>Minimal volume should not be larger than maximal volume!"));
                    return "ERROR";
                }
                // TODO modify sound
                executor.sendMessage(Lang.color("<error>You entered: " + reply + "."));
                UtilTask.sync(task1 -> main.menus.soundPropertiesMenu.show(event.getWhoClicked(), sound));
                return null;
            });
        }));
        return condition;
    }

    private MenuButton getMaxVolumeButton(Sound sound) {
        MenuButton condition = new MenuButton(new UtilItem(Material.PLAYER_HEAD)
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNlZWI3N2Q0ZDI1NzI0YTljYWYyYzdjZGYyZDg4Mzk5YjE0MTdjNmI5ZmY1MjEzNjU5YjY1M2JlNDM3NmUzIn19fQ==")
                .setName("§aMaximal Volume")
                .setLore("§8Upper bound of the volume", "", "§7Currently set to " + sound.getMaxVolume(), "", "§b➜ Click to modify")
                .hideFlags().create());
        condition.setHandler(event -> UtilTask.sync(task -> {
            event.getWhoClicked().closeInventory();
            HumanEntity clicker = event.getWhoClicked();
            clicker.closeInventory();
            if (!(clicker instanceof Player)) {
                clicker.sendMessage(Lang.color("<error>Only players can modify volume!"));
                return;
            }
            Player player = (Player) clicker;
            new AnvilGUI(main, player, "Enter a volume", (executor, reply) -> {
                double vol;
                try {
                    vol = Double.parseDouble(reply);
                } catch (NumberFormatException ex) {
                    executor.sendMessage(Lang.color("<error>You have to enter a number!"));
                    return "ERROR";
                }
                if (vol < 0) {
                    executor.sendMessage(Lang.color("<error>You have to enter a positive number!"));
                    return "ERROR";
                }
                if (vol < sound.getMinVolume()) {
                    executor.sendMessage(Lang.color("<error>Maximal volume should not be smaller than minimal volume!"));
                    return "ERROR";
                }
                // TODO modify sound
                executor.sendMessage(Lang.color("<error>You entered: " + reply + "."));
                UtilTask.sync(task1 -> main.menus.soundPropertiesMenu.show(event.getWhoClicked(), sound));
                return null;
            });
        }));
        return condition;
    }

    private MenuButton getLocationConditionButton(Sound sound) {
        MenuButton condition = new MenuButton(new UtilItem(Material.PLAYER_HEAD)
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmUyY2M0MjAxNWU2Njc4ZjhmZDQ5Y2NjMDFmYmY3ODdmMWJhMmMzMmJjZjU1OWEwMTUzMzJmYzVkYjUwIn19fQ==")
                .setName("§aLocation Condition")
                .setLore("§8Altitude and world conditions", "", "§7Not set for current sound configuration.", "", "§b➜ Click to configure")
                .hideFlags().create());
        condition.setHandler(event -> UtilTask.sync(task -> {
            event.getWhoClicked().closeInventory();
            main.menus.conditionsMenu.show(event.getWhoClicked(), sound, ConditionType.LOCATION);
        }));
        return condition;
    }

    private MenuButton getRegionConditionButton(Sound sound) {
        MenuButton condition = new MenuButton(new UtilItem(Material.PLAYER_HEAD)
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTdlNTYxNDA2ODZlNDc2YWVmNTUyMGFjYmFiYzIzOTUzNWZmOTdlMjRiMTRkODdmNDk4MmYxMzY3NWMifX19")
                .setName("§aRegion Condition")
                .setLore("§8WorldGuard region conditions", "", "§7Not set for current sound configuration.", "", "§b➜ Click to configure")
                .hideFlags().create());
        condition.setHandler(event -> UtilTask.sync(task -> {
            event.getWhoClicked().closeInventory();
            main.menus.conditionsMenu.show(event.getWhoClicked(), sound, ConditionType.REGION);
        }));
        return condition;
    }

    private MenuButton getBiomeConditionButton(Sound sound) {
        MenuButton condition = new MenuButton(new UtilItem(Material.PLAYER_HEAD)
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzk1ZDM3OTkzZTU5NDA4MjY3ODQ3MmJmOWQ4NjgyMzQxM2MyNTBkNDMzMmEyYzdkOGM1MmRlNDk3NmIzNjIifX19")
                .setName("§aBiome Condition")
                .setLore("§8Biome type conditions", "", "§7Not set for current sound configuration.", "", "§b➜ Click to configure")
                .hideFlags().create());
        condition.setHandler(event -> UtilTask.sync(task -> {
            event.getWhoClicked().closeInventory();
            main.menus.conditionsMenu.show(event.getWhoClicked(), sound, ConditionType.BIOME);
        }));
        return condition;
    }

    private MenuButton getTimeConditionButton(Sound sound) {
        MenuButton condition = new MenuButton(new UtilItem(Material.PLAYER_HEAD)
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQ3N2RhZmM4YzllYTA3OTk2MzMzODE3OTM4NjZkMTQ2YzlhMzlmYWQ0YzY2ODRlNzExN2Q5N2U5YjZjMyJ9fX0=")
                .setName("§aTime Condition")
                .setLore("§8Day and night conditions", "", "§7Not set for current sound configuration.", "", "§b➜ Click to configure")
                .hideFlags().create());
        condition.setHandler(event -> UtilTask.sync(task -> {
            event.getWhoClicked().closeInventory();
            main.menus.conditionsMenu.show(event.getWhoClicked(), sound, ConditionType.TIME);
        }));
        return condition;
    }

    private MenuButton getWeatherConditionButton(Sound sound) {
        MenuButton condition = new MenuButton(new UtilItem(Material.PLAYER_HEAD)
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjg5MDQyMDgyYmI3YTc2MThiNzg0ZWU3NjA1YTEzNGM1ODgzNGUyMWUzNzRjODg4OTM3MTYxMDU3ZjZjNyJ9fX0=")
                .setName("§aWeather Condition")
                .setLore("§8Sun, rain and thunder conditions", "", "§7Not set for current sound configuration.", "", "§b➜ Click to configure")
                .hideFlags().create());
        condition.setHandler(event -> UtilTask.sync(task -> {
            event.getWhoClicked().closeInventory();
            main.menus.conditionsMenu.show(event.getWhoClicked(), sound, ConditionType.WEATHER);
        }));
        return condition;
    }

    private MenuButton getEntityConditionButton(Sound sound) {
        MenuButton condition = new MenuButton(new UtilItem(Material.PLAYER_HEAD)
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWM3OTc0ODJhMTRiZmNiODc3MjU3Y2IyY2ZmMWI2ZTZhOGI4NDEzMzM2ZmZiNGMyOWE2MTM5Mjc4YjQzNmIifX19")
                .setName("§aEntity Condition")
                .setLore("§8Nearby entity conditions", "", "§7Not set for current sound configuration.", "", "§b➜ Click to configure")
                .hideFlags().create());
        condition.setHandler(event -> UtilTask.sync(task -> {
            event.getWhoClicked().closeInventory();
            main.menus.conditionsMenu.show(event.getWhoClicked(), sound, ConditionType.ENTITY);
        }));
        return condition;
    }

    private MenuButton getCooldownConditionButton(Sound sound) {
        MenuButton condition = new MenuButton(new UtilItem(Material.PLAYER_HEAD)
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlN2Q0NjMyMjQ3N2Q2MWQ0MWMxODc4OGY1YzFhZmQyNGVkNTI2ZWIzZWQ4NDEyN2YyMTJlMjUxNWIxODgzIn19fQ==")
                .setName("§aCooldown Condition")
                .setLore("§8Time waiting conditions", "", "§7Not set for current sound configuration.", "", "§b➜ Click to configure")
                .hideFlags().create());
        condition.setHandler(event -> UtilTask.sync(task -> {
            event.getWhoClicked().closeInventory();
            main.menus.conditionsMenu.show(event.getWhoClicked(), sound, ConditionType.COOLDOWN);
        }));
        return condition;
    }
}
