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
import me.theblockbender.nature.sounds.NatureSounds;
import me.theblockbender.nature.sounds.utilities.UtilItem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaginatedMenu implements InventoryHolder {

    private static NatureSounds main;
    private Map<Integer, MenuButton> everyPageItems;
    private List<MenuButton> contentItems;
    private String name;
    private int page;
    private List<Integer> frameSlots = new ArrayList<Integer>() {{
        add(0);
        add(1);
        add(2);
        add(3);
        add(4);
        add(5);
        add(6);
        add(7);
        add(8);
        add(9);
        add(17);
        add(18);
        add(26);
        add(27);
        add(35);
        add(36);
        add(37);
        add(38);
        add(39);
        add(40);
        add(41);
        add(42);
        add(43);
        add(44);
    }};
    private Map<Integer, Integer> contentSlots = new HashMap<Integer, Integer>() {{
        put(10, 0);
        put(11, 1);
        put(12, 2);
        put(13, 3);
        put(14, 4);
        put(15, 5);
        put(16, 6);
        put(19, 7);
        put(20, 8);
        put(21, 9);
        put(22, 10);
        put(23, 11);
        put(24, 12);
        put(25, 13);
        put(28, 14);
        put(29, 15);
        put(30, 16);
        put(31, 17);
        put(32, 18);
        put(33, 19);
        put(34, 20);
    }};

    public PaginatedMenu(String name) {
        everyPageItems = new HashMap<>();
        contentItems = new ArrayList<>();
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        page = 0;
    }

    public static void register(JavaPlugin plugin) {
        main = (NatureSounds) plugin;
    }

    public void addContentItem(MenuButton button) {
        contentItems.add(button);
    }

    public void setEveryPageItem(int slot, MenuButton button) {
        everyPageItems.put(slot, button);
    }

    public MenuButton getButton(int slot) {
        if (contentSlots.containsKey(slot)) {
            int id = page * contentSlots.get(slot);
            return contentItems.get(id);
        } else {
            return everyPageItems.get(slot);
        }
    }

    private boolean nextPage() {
        if (page < getFinalPage()) {
            page++;
            return true;
        } else {
            return false;
        }
    }

    private boolean previousPage() {
        if (page > 0) {
            page--;
            return true;
        } else {
            return false;
        }
    }

    private int getFinalPage() {
        int amountOfItems = contentItems.size();
        return (int) Math.ceil(amountOfItems / 21d) - 1;
    }

    public void refreshInventory(HumanEntity holder) {
        holder.closeInventory();
        holder.openInventory(getInventory());
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 54, name);
        MenuButton frame = new MenuButton(new UtilItem(Material.GRAY_STAINED_GLASS_PANE).setName("").hideFlags().create());
        frame.setHandler(event -> event.setCancelled(true));

        for (int slot : frameSlots) {
            inventory.setItem(slot, frame.getItemStack());
            everyPageItems.put(slot, frame);
        }

        MenuButton exit = new MenuButton(new UtilItem(Material.SIGN).setName("§c§lExit").hideFlags().create());
        exit.setHandler(event -> Bukkit.getScheduler().runTask(main, () -> event.getWhoClicked().closeInventory()));
        inventory.setItem(52, exit.getItemStack());
        everyPageItems.put(52, exit);


        // Content slots 10 till 34
        // & while frame.contains(int), int++;
        for (int i = 0; i < 21; i++) {
            int index = (21 * page) + i - 1;
            // don't do anything if there are no more content items.
            if (contentItems.size() - 1 < index) break;
            // we get the next menu button item.
            MenuButton button = contentItems.get(index);
            // we get the next free slot
            int slot = 10 + i;
            while (!contentSlots.containsKey(slot)) {
                slot++;
            }
            inventory.setItem(slot, button.getItemStack());
        }

        for (Map.Entry<Integer, MenuButton> entry : everyPageItems.entrySet()) {
            int slot = entry.getKey();
            if (slot < 0 || slot > 53) continue;
            inventory.setItem(entry.getKey(), entry.getValue().getItemStack());
        }

        MenuButton next = new MenuButton(new UtilItem(Material.PLAYER_HEAD).setName("§e§lNext Page").hideFlags().create());
        next.setHandler(event -> {
            if (!nextPage()) {
                event.getWhoClicked().sendMessage(Lang.color("<error>This is the last page."));
            } else {
                Bukkit.getScheduler().runTask(main, () -> refreshInventory(event.getWhoClicked()));
            }
        });
        inventory.setItem(36, next.getItemStack());
        everyPageItems.put(36, next);

        MenuButton prev = new MenuButton(new UtilItem(Material.PLAYER_HEAD).setName("§e§lPrevious Page").hideFlags().create());
        prev.setHandler(event -> {
            if (!previousPage()) {
                event.getWhoClicked().sendMessage(Lang.color("<error>This is the first page."));
            } else {
                Bukkit.getScheduler().runTask(main, () -> refreshInventory(event.getWhoClicked()));
            }
        });
        inventory.setItem(45, prev.getItemStack());
        everyPageItems.put(45, prev);
        return inventory;
    }

}

