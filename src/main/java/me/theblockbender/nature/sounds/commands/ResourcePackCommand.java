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

@CommandAlias("resourcepack|rp|pack")
public class ResourcePackCommand extends BaseCommand {

    // -------------------------------------------- //
    // INSTANCES & VARIABLES
    // -------------------------------------------- //
    private final NatureSounds main;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public ResourcePackCommand(NatureSounds main) {
        this.main = main;
    }

    // -------------------------------------------- //
    // COMMAND HANDLERS
    // -------------------------------------------- //

    @Default
    @HelpCommand
    @Description("View information on commands")
    @CommandPermission("ns.resourcepack.help")
    public void commandHelp(CommandSender sender, CommandHelp help) {
        sender.sendMessage(Lang.format("header"));
        help.showHelp();
    }

    @Subcommand("generate")
    @Description("Regenerates the resource pack")
    @CommandPermission("ns.resourcepack.generate")
    public void commandGenerate(CommandSender sender) {
        sender.sendMessage(Lang.formatWithPrefix("generating"));
        main.getResourcePack().addAllFilesToPack();
        sender.sendMessage(Lang.formatWithPrefix("generated"));
    }

    @Subcommand("accept")
    @Description("Sends the resource pack to the player")
    @CommandPermission("ns.resourcepack.download")
    public void commandAccept(Player player) {
        if (main.playersWithRP.contains(player.getUniqueId())) {
            player.sendMessage(Lang.formatWithPrefix("resourcepack.already-accepted"));
        } else {
            String url = "http://" + main.getWebServer().ip + ":" + main.getWebServer().port + "/" + UtilToken.getToken(player.getUniqueId());
            player.setResourcePack(url, UtilChecksum.getChecksum(UtilChecksum.fileToByteArray(main.getWebServer().getFileLocation())));
        }
    }

    @Subcommand("reject")
    @Description("Sends a rejected message to the player")
    @CommandPermission("ns.resourcepack.download")
    public void commandReject(Player player) {
        if (main.playersWithRP.contains(player.getUniqueId())) {
            player.sendMessage(Lang.formatWithPrefix("resourcepack.already-accepted"));
        } else {
            player.sendMessage(Lang.formatWithPrefix("resourcepack.rejected"));
        }
    }
}
