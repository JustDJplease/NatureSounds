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
import me.theblockbender.nature.sounds.utilities.UtilChecksum;
import me.theblockbender.nature.sounds.utilities.UtilToken;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("web|website|webtest")
public class WebTestCommand extends BaseCommand {

    // -------------------------------------------- //
    // INSTANCES & VARIABLES
    // -------------------------------------------- //
    private NatureSounds main;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public WebTestCommand(NatureSounds main) {
        this.main = main;
    }

    // -------------------------------------------- //
    // COMMAND HANDLERS
    // -------------------------------------------- //

    @Default
    @HelpCommand
    @Description("View information on commands")
    @CommandPermission("nature.admin.web")
    public void commandHelp(CommandSender sender, CommandHelp help) {
        sender.sendMessage(Lang.format("header"));
        help.showHelp();
    }

    @Subcommand("start")
    @Description("Start the web server")
    @CommandPermission("nature.admin.web")
    public void commandStartWeb(CommandSender sender) {
        sender.sendMessage("§e» §7Starting web server...");
        main.utilWebServer.start();
        sender.sendMessage("§e» §7Started web server!");
    }

    @Subcommand("stop")
    @Description("Stops the web server")
    @CommandPermission("nature.admin.web")
    public void commandStopWeb(CommandSender sender) {
        sender.sendMessage("§e» §7Stopping web server...");
        main.utilWebServer.stop();
        sender.sendMessage("§e» §7Stopped web server!");
    }

    @Subcommand("generate")
    @Description("Stops the web server")
    @CommandPermission("nature.admin.web")
    public void commandGenerate(CommandSender sender) {
        sender.sendMessage("§e» §7Generating pack...");
        main.utilResourcePack.addAllFilesToPack();
        sender.sendMessage("§e» §7Done!");
    }

    @Subcommand("accept")
    @Description("Sends the resource pack to the player executing the command")
    @CommandPermission("nature.admin.web")
    public void commandAccept(Player player) {
        player.sendMessage("§e» §7Sending the resource-pack to you!");
        String url = "http://" + main.utilWebServer.ip + ":" + main.utilWebServer.port + "/" + UtilToken.getToken(player.getUniqueId());
        player.setResourcePack(url, UtilChecksum.getChecksum(UtilChecksum.fileToByteArray(main.utilWebServer.getFileLocation())));
    }

    @Subcommand("reject")
    @Description("Sends a rejected message to the player executing the command")
    @CommandPermission("nature.admin.web")
    public void commandReject(Player player) {
        player.sendMessage("§e» §7You have rejected the resource-pack. If you change your mind, click the accept message above!");
    }
}
