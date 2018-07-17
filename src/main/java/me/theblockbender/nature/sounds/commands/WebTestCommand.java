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

import me.theblockbender.nature.sounds.NatureSounds;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WebTestCommand implements CommandExecutor {

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
//    @Default
//    @HelpCommand
//    @Description("View information on commands")
//    @CommandPermission("nature.admin.web")
//    public void commandHelp(CommandSender sender, CommandHelp help) {
//        sender.sendMessage(Lang.format("header"));
//        help.showHelp();
//    }
//
//    @Subcommand("start")
//    @Description("Start the web server")
//    @CommandPermission("nature.admin.web")
//    public void commandStartWeb(CommandSender sender) {
//        sender.sendMessage("[Test] Starting web server...");
//        if (main.webServerHandler.start()) {
//            sender.sendMessage("[Test] Started web server!");
//            return;
//        }
//        sender.sendMessage("[Test] Failed to start web server! See the console for errors!");
//    }
//
//    @Subcommand("stop")
//    @Description("Stops the web server")
//    @CommandPermission("nature.admin.web")
//    public void commandStopWeb(CommandSender sender) {
//        sender.sendMessage("[Test] Stopping web server...");
//        main.webServerHandler.stop();
//        sender.sendMessage("[Test] Stopped web server!");
//    }
//
//    @Subcommand("link")
//    @Description("Sends a link to the web-page in chat.")
//    @CommandPermission("nature.admin.web")
//    public void commandLinkWeb(CommandSender sender) {
//        sender.sendMessage("[Test] URL of web-page is: " + main.getServer().getIp() + ":" + main.webServerHandler.port);
//    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) return false;
        switch (args[0].toLowerCase()) {
            case "start":
                sender.sendMessage("[Test] Starting web server...");
                if (main.webServerHandler.start()) {
                    sender.sendMessage("[Test] Started web server!");
                    return true;
                }
                sender.sendMessage("[Test] Failed to start web server! See the console for errors!");
                return true;
            case "stop":
                sender.sendMessage("[Test] Stopping web server...");
                main.webServerHandler.stop();
                sender.sendMessage("[Test] Stopped web server!");
                return true;
            case "link":
                sender.sendMessage("[Test] URL of web-page is: " + main.webServerHandler.ip + ":" + main.webServerHandler.port);
                return true;
            case "send":
                sender.sendMessage("[Test] Sending you the resource pack...");
                String hash = "7036BA280057E0726433";
                Player player = (Player) sender;
                String url = main.webServerHandler.ip + ":" + main.webServerHandler.port + "/rp.zip";
                url = "https://www.dropbox.com/s/anc2ucrrxa8qugq/rp.zip?dl=1";
                sender.sendMessage("[Test] Fabricated url = " + url);
                player.setResourcePack(url, hash.getBytes());
                return true;
            default:
                return false;
        }
    }
}
