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
import me.theblockbender.nature.sounds.listeners.InventoryListener;
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

@SuppressWarnings("unused")
public class Menu implements InventoryHolder {

    private static NatureSounds main;
    private static InventoryListener inventoryListener;
    private Map<Integer, MenuButton> items;
    private String name;
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

    public Menu(String name) {
        items = new HashMap<>();
        this.name = ChatColor.translateAlternateColorCodes('&', name);
    }

    public static void registerListeners(JavaPlugin plugin) {
        main = (NatureSounds) plugin;
        if (inventoryListener == null) {
            inventoryListener = new InventoryListener();
            plugin.getServer().getPluginManager().registerEvents(inventoryListener, plugin);
        }
    }

    public String getDisplayName() {
        return name;
    }

    public void setDisplayName(String name) {
        this.name = ChatColor.translateAlternateColorCodes('&', name);
    }

    public void addButton(MenuButton button) {
        int slot = 0;
        for (int nextSlot : items.keySet()) {
            if (nextSlot > slot) {
                slot = nextSlot;
            }
        }
        if (!items.isEmpty()) {
            slot++;
        }
        items.put(slot, button);
    }

    public void setButton(int slot, MenuButton button) {
        items.put(slot, button);
    }

    public void removeButton(int slot) {
        items.remove(slot);
    }

    public MenuButton getButton(int slot) {
        if (slot < 54) {
            return items.get(slot);
        } else {
            return null;
        }
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
        }

        MenuButton exit = new MenuButton(new UtilItem(Material.SIGN).setName(Lang.format("gui.exit")).hideFlags().create());
        frame.setHandler(event -> Bukkit.getScheduler().runTask(main, () -> event.getWhoClicked().closeInventory()));
        inventory.setItem(52, exit.getItemStack());

        for (Map.Entry<Integer, MenuButton> entry : items.entrySet()) {
            int slot = entry.getKey();
            if (slot < 0 || slot > 53) continue;
            inventory.setItem(entry.getKey(), entry.getValue().getItemStack());
        }
        return inventory;
    }

}

