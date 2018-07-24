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

package me.theblockbender.nature.sounds.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import me.theblockbender.nature.sounds.Lang;
import me.theblockbender.nature.sounds.NatureSounds;
import me.theblockbender.nature.sounds.Sound;
import me.theblockbender.nature.sounds.utilities.UtilText;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("sound|sounds")
public class SoundCommand extends BaseCommand {

    // -------------------------------------------- //
    // INSTANCES & VARIABLES
    // -------------------------------------------- //
    private final NatureSounds main;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public SoundCommand(NatureSounds main) {
        this.main = main;
    }

    // -------------------------------------------- //
    // COMMAND HANDLERS
    // -------------------------------------------- //

    @Default
    @HelpCommand
    @Description("View information on commands")
    @CommandPermission("nature.help")
    public void commandHelp(CommandSender sender, CommandHelp help) {
        sender.sendMessage(Lang.format("header"));
        help.showHelp();
    }

    @Subcommand("list")
    @Description("Display loaded sounds")
    @CommandPermission("nature.list")
    @Syntax("[page]")
    public void commandList(CommandSender sender, String[] args) {
        int page;
        try {
            page = Integer.parseInt(args[0]);
        } catch (IndexOutOfBoundsException ex) {
            page = 1;
        } catch (NumberFormatException ex) {
            sender.sendMessage(Lang.format("error.no-number").replace("{0}", args[0]));
            return;
        }
        if (page < 1) page = 1;
        sender.sendMessage(Lang.format("header"));
        sender.sendMessage(" ");
        sender.sendMessage(Lang.color("<secondary>Page: <argument>" + page));
        for (Sound sound : getSoundPage(page)) {
            BaseComponent[] entry = new ComponentBuilder("")
                    .append(TextComponent.fromLegacyText(Lang.format("prefix")))
                    .append(sound.getFileName()).color(Lang.getColor("argument"))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to view detailed information about this sound").color(ChatColor.GRAY).create()))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sound info " + sound.getFileName()))
                    .append(" (" + sound.getSoundNames().size() + " sounds)").color(Lang.getColor("primary"))
                    .create();
            sender.spigot().sendMessage(entry);
        }
        sender.sendMessage(" ");
        sender.sendMessage(Lang.format("header"));
    }

    private List<Sound> getSoundPage(int page) {
        List<Sound> selection = new ArrayList<>();
        List<Sound> all = main.getSounds();
        int start = (page - 1) * 10;
        int end = start + 11;
        for (int i = start; i < end; i++) {
            try {
                selection.add(all.get(i));
            } catch (IndexOutOfBoundsException ex) {
                break;
            }
        }
        return selection;
    }

    @Subcommand("play")
    @Description("Plays the given sounds")
    @CommandPermission("nature.play")
    @CommandCompletion("@sounds")
    @Syntax("<sound>")
    public void commandPlay(Player player, String[] args) {
        String fileName;
        try {
            fileName = args[0];
        } catch (IndexOutOfBoundsException ex) {
            player.sendMessage(Lang.format("error.no-filename"));
            return;
        }
        Sound sound = main.getSound(fileName);
        if (sound == null) {
            player.sendMessage(Lang.format("error.file-not-found").replace("{0}", fileName));
            return;
        }
        player.sendMessage(Lang.format("header"));
        player.sendMessage(" ");
        UtilText.sendCenteredMessage(player, Lang.format("playing-sound").replace("{0}", fileName));
        player.sendMessage(" ");
        player.sendMessage(Lang.format("header"));
        sound.forceRun(player);
    }

    @Subcommand("info")
    @Description("Display details of sounds")
    @CommandPermission("nature.info")
    @CommandCompletion("@sounds")
    @Syntax("<sound>")
    public void commandInfo(CommandSender sender, String[] args) {
        String fileName;
        try {
            fileName = args[0];
        } catch (IndexOutOfBoundsException ex) {
            sender.sendMessage(Lang.format("error.no-filename"));
            return;
        }
        Sound sound = main.getSound(fileName);
        if (sound == null) {
            sender.sendMessage(Lang.format("error.file-not-found").replace("{0}", fileName));
            return;
        }
        sender.sendMessage(Lang.format("header"));
        sender.sendMessage(" ");
        sender.sendMessage(Lang.color("<secondary>Showing details of: <argument>" + fileName));
        BaseComponent[] entry = new ComponentBuilder("")
                .append(TextComponent.fromLegacyText(Lang.format("prefix")))
                .append("Sound files: ").color(Lang.getColor("primary"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getSoundNamesHover(sound.getSoundNames(), Lang.getColor("primary"), Lang.getColor("argument"))).color(ChatColor.GRAY).create()))
                .append(sound.getSoundNames().size() + " ").color(Lang.getColor("argument"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getSoundNamesHover(sound.getSoundNames(), Lang.getColor("primary"), Lang.getColor("argument"))).color(ChatColor.GRAY).create()))
                .create();
        sender.spigot().sendMessage(entry);
        BaseComponent[] play = new ComponentBuilder("")
                .append(TextComponent.fromLegacyText(Lang.format("prefix")))
                .append("Play this sound").color(Lang.getColor("secondary")).bold(true)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Lang.format("click-to-play")).color(ChatColor.GRAY).create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sound play " + sound.getFileName()))
                .create();
        sender.spigot().sendMessage(play);
        sender.sendMessage(" ");
        sender.sendMessage(Lang.format("header"));
    }

    private String getSoundNamesHover(List<String> soundNames, ChatColor c1, ChatColor c2) {
        StringBuilder hover = new StringBuilder();
        for (String name : soundNames) {
            hover.append(c1).append("- ").append(c2).append(name).append("\n");
        }
        return hover.toString();
    }
}
