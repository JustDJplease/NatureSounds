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


import me.theblockbender.nature.sounds.Lang;
import me.theblockbender.nature.sounds.utilities.UtilItem;
import me.theblockbender.nature.sounds.utilities.UtilTask;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaginatedMenu implements InventoryHolder {

    // -------------------------------------------- //
    // INSTANCES
    // -------------------------------------------- //
    private final String name;
    private final List<MenuButton> content_items = new ArrayList<>();
    private final List<Integer> frame_slots = new ArrayList<>();
    private final Map<Integer, MenuButton> every_page_items = new HashMap<>();
    private final Map<Integer, Integer> content_slots = new HashMap<>();

    private int page;

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

        content_slots.put(10, 0);
        content_slots.put(11, 1);
        content_slots.put(12, 2);
        content_slots.put(13, 3);
        content_slots.put(14, 4);
        content_slots.put(15, 5);
        content_slots.put(16, 6);
        content_slots.put(19, 7);
        content_slots.put(20, 8);
        content_slots.put(21, 9);
        content_slots.put(22, 10);
        content_slots.put(23, 11);
        content_slots.put(24, 12);
        content_slots.put(25, 13);
        content_slots.put(28, 14);
        content_slots.put(29, 15);
        content_slots.put(30, 16);
        content_slots.put(31, 17);
        content_slots.put(32, 18);
        content_slots.put(33, 19);
        content_slots.put(34, 20);
    }

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public PaginatedMenu(String name) {
        this.name = name;
        page = 0;
    }

    // -------------------------------------------- //
    // SETTERS & GETTERS
    // -------------------------------------------- //
    public void addContentItem(MenuButton button) {
        content_items.add(button);
    }

    public void setEveryPageItem(int slot, MenuButton button) {
        every_page_items.put(slot, button);
    }

    public MenuButton getButton(int slot) {
        if (content_slots.containsKey(slot)) {
            int id = (page + 1) * content_slots.get(slot);
            if (content_items.size() - 1 < id) return null;
            return content_items.get(id);
        } else {
            if (every_page_items.size() - 1 < slot) return null;
            return every_page_items.get(slot);
        }
    }

    private boolean hasNext() {
        if (page < getFinalPage()) {
            page++;
            return true;
        } else {
            return false;
        }
    }

    private boolean hasPrevious() {
        if (page > 0) {
            page--;
            return true;
        } else {
            return false;
        }
    }

    private int getFinalPage() {
        return (int) Math.ceil(content_items.size() / 21d) - 1;
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 54, name);

        MenuButton frame = getFrameButton();
        for (int slot : frame_slots) {
            inventory.setItem(slot, frame.getItemStack());
            every_page_items.put(slot, frame);
        }

        MenuButton exit = getExitButton();
        inventory.setItem(52, exit.getItemStack());
        every_page_items.put(52, exit);

        for (int i = 0; i < 21; i++) {
            int index = (21 * page) + i;
            if (content_items.size() - 1 < index) break;
            MenuButton button = content_items.get(index);
            int slot = 10 + i;
            while (!content_slots.containsKey(slot) || (inventory.getItem(slot) != null && inventory.getItem(slot).getType() != Material.AIR)) {
                slot++;
            }
            inventory.setItem(slot, button.getItemStack());
        }

        for (Map.Entry<Integer, MenuButton> entry : every_page_items.entrySet()) {
            int slot = entry.getKey();
            if (slot < 0 || slot > 53) continue;
            inventory.setItem(entry.getKey(), entry.getValue().getItemStack());
        }

        MenuButton next = getNextButton();
        inventory.setItem(44, next.getItemStack());
        every_page_items.put(44, next);

        MenuButton prev = getPreviousButton();
        inventory.setItem(36, prev.getItemStack());
        every_page_items.put(36, prev);

        return inventory;
    }

    // -------------------------------------------- //
    // MENU BUTTONS
    // -------------------------------------------- //
    private MenuButton getFrameButton() {
        MenuButton frame = new MenuButton(new UtilItem(Material.BLACK_STAINED_GLASS_PANE)
                .setName("§7")
                .hideFlags().create());
        frame.setHandler(event -> event.setCancelled(true));
        return frame;
    }

    private MenuButton getExitButton() {
        MenuButton exit = new MenuButton(new UtilItem(Material.FIRE_CORAL)
                .setName("§c§lExit")
                .setLore("§8leave this menu", "", "§7Close the menu you are currently", "§7viewing and return to the game.", "", "§b➜ Click to close this menu")
                .hideFlags().create());
        exit.setHandler(event -> {
            event.setCancelled(true);
            UtilTask.sync(task -> event.getWhoClicked().closeInventory());
        });
        return exit;
    }

    private MenuButton getNextButton() {
        MenuButton next = new MenuButton(new UtilItem(Material.PLAYER_HEAD)
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19")
                .setName("§e§lNext")
                .setLore("§8View Next page", "", "§7Continue viewing the remaining entries", "§7on this list on the next page.", "", "§b➜ Click to continue")
                .hideFlags().create());
        next.setHandler(event -> {
            event.setCancelled(true);
            if (!hasNext()) {
                event.getWhoClicked().sendMessage(Lang.color("<error>This is the last page."));
            } else {
                UtilTask.sync(task -> refreshInventory(event.getWhoClicked()));
            }
        });
        return next;
    }

    private MenuButton getPreviousButton() {
        MenuButton prev = new MenuButton(new UtilItem(Material.PLAYER_HEAD)
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==")
                .setName("§e§lPrevious")
                .setLore("§8View previous page", "", "§7Go back to viewing the earlier entries", "§7from this list on the previous page.", "", "§b➜ Click to continue")
                .hideFlags().create());
        prev.setHandler(event -> {
            event.setCancelled(true);
            if (!hasPrevious()) {
                event.getWhoClicked().sendMessage(Lang.color("<error>This is the first page."));
            } else {
                UtilTask.sync(task -> refreshInventory(event.getWhoClicked()));
            }
        });
        return prev;
    }

    // -------------------------------------------- //
    // INVENTORY MANAGEMENT
    // -------------------------------------------- //
    public void clearContentSlots() {
        content_items.clear();
    }

    private void refreshInventory(HumanEntity holder) {
        holder.closeInventory();
        holder.openInventory(getInventory());
    }
}

