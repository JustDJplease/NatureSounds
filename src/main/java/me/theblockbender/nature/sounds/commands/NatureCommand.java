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
import me.theblockbender.nature.sounds.utilities.UtilText;
import org.bukkit.command.CommandSender;

@CommandAlias("naturesounds|nature|ns")
public class NatureCommand extends BaseCommand {

    // -------------------------------------------- //
    // INSTANCES & VARIABLES
    // -------------------------------------------- //
    private final NatureSounds main;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public NatureCommand(NatureSounds main) {
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

    @Subcommand("reload")
    @Description("Reloads the plugin")
    @CommandPermission("nature.admin.reload")
    @CommandCompletion("@reload")
    public void commandReload(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Lang.format("error.no-reload-type"));
            return;
        }
        switch (args[0].toLowerCase()) {
            case "language":
                sender.sendMessage(Lang.formatWithPrefix("reloading").replace("%0%", "Language"));
                main.registerLanguage();
                sender.sendMessage(Lang.formatWithPrefix("reloaded").replace("%0%", "Language"));
                break;
            case "sounds":
                sender.sendMessage(Lang.formatWithPrefix("reloading").replace("%0%", "Sounds"));
                main.registerSounds();
                sender.sendMessage(Lang.formatWithPrefix("reloaded").replace("%0%", "Sounds"));
                break;
            case "resource-pack":
                sender.sendMessage(Lang.formatWithPrefix("reloading").replace("%0%", "Resource-pack"));
                main.utilResourcePack.addAllFilesToPack();
                sender.sendMessage(Lang.formatWithPrefix("reloaded").replace("%0%", "Resource-pack"));
                sender.sendMessage(Lang.formatWithPrefix("send-pack-again"));
                break;
            default:
                sender.sendMessage(Lang.format("error.no-reload-type"));
                break;
        }
    }

    @Subcommand("version")
    @Description("Display plugin version")
    public void commandVersion(CommandSender sender) {
        sender.sendMessage(Lang.format("header"));
        sender.sendMessage(" ");
        UtilText.sendCenteredMessage(sender, Lang.color("<primary>Appreciate the subtle sounds whilst roaming?"));
        UtilText.sendCenteredMessage(sender, Lang.color("<primary>They bring this game to a next level!"));
        sender.sendMessage(" ");
        UtilText.sendCenteredMessage(sender, Lang.color("<primary>Running <argument>NatureSounds <primary>version <argument>" + main.getDescription().getVersion() + "<primary>."));
        UtilText.sendCenteredMessage(sender, Lang.color("<primary>by <argument>TheBlockBender<primary> & <argument>JustDJplease<primary>."));
        sender.sendMessage(" ");
        sender.sendMessage(Lang.format("header"));
    }
}
