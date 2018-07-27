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

package me.theblockbender.nature.sounds.utilities;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import me.theblockbender.nature.sounds.ErrorLogger;
import me.theblockbender.nature.sounds.NatureSounds;
import org.bukkit.Bukkit;

import java.io.File;

public class UtilWebServer {

    private final NatureSounds main;
    // -------------------------------------------- //
    // INSTANCES & VARIABLES
    // -------------------------------------------- //
    public int port;
    public String ip;
    private HttpServer httpServer;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public UtilWebServer(NatureSounds main) {
        this.main = main;
    }


    // -------------------------------------------- //
    // WEB HANDLERS
    // -------------------------------------------- //
    public void start() {
        ip = main.getServer().getIp();
        if (ip == null || ip.equals("")) ip = "localhost";
        try {
            port = main.getConfig().getInt("port");
        } catch (Exception ex) {
            ErrorLogger.errorInFile("Invalid port specified", "config.yml");
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            try {
                httpServer = Vertx.vertx().createHttpServer();
                httpServer.requestHandler(httpServerRequest -> {
                    main.debug("| HTTP Request:");
                    if (httpServerRequest.uri().contains("/favicon.ico")) {
                        main.debug("| Rejected: Favicon sending");
                        httpServerRequest.response().setStatusCode(401);
                        httpServerRequest.response().end();
                    } else {
                        if (!httpServerRequest.uri().contains("/")) {
                            main.debug("| Rejected: Invalid URL");
                            httpServerRequest.response().setStatusCode(401);
                            httpServerRequest.response().end();
                        } else {
                            String[] parts = httpServerRequest.uri().split("/");
                            if (parts.length != 2) {
                                main.debug("| Rejected: No token");
                                httpServerRequest.response().setStatusCode(401);
                                httpServerRequest.response().end();
                            } else {
                                String token = parts[1];
                                if (UtilToken.isValidToken(token)) {
                                    main.debug("| Sending file...");
                                    httpServerRequest.response().sendFile(getFileLocation());
                                } else {
                                    main.debug("| Rejected: Invalid token");
                                    httpServerRequest.response().setStatusCode(401);
                                    httpServerRequest.response().end();
                                }
                            }
                        }
                    }
                });
                httpServer.listen(port);
                main.debug("| Started internal webserver");
            } catch (Exception ex) {
                ErrorLogger.error("Unable to bind to port " + port + ". Please assign the plugin to a different port!");
                ex.printStackTrace();
            }
        });
    }

    // -------------------------------------------- //
    // GETTERS
    // -------------------------------------------- //
    public final String getFileLocation() {
        return main.getDataFolder().getPath() + File.separator + "web" + File.separator + "rp.zip";
    }

    final String getUnzippedFileLocation() {
        return main.getDataFolder().getPath() + File.separator + "web" + File.separator + "rp";
    }

    final File getWebDirectory() {
        return new File(main.getDataFolder().getPath() + File.separator + "web");
    }
}
