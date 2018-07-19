package me.theblockbender.nature.sounds.utilities;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import me.theblockbender.nature.sounds.NatureSounds;

import java.io.File;

public class UtilWebServer {

    // -------------------------------------------- //
    // INSTANCES & VARIABLES
    // -------------------------------------------- //
    public int port;
    public String ip;

    private HttpServer httpServer;
    private NatureSounds main;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public UtilWebServer(NatureSounds main) {
        this.main = main;
    }

    // -------------------------------------------- //
    // WEB HANDLERS
    // -------------------------------------------- //
    public boolean start() {
        ip = main.getServer().getIp();
        if (ip == null || ip.equals("")) ip = "localhost";
        try {
            port = main.getConfig().getInt("port");
        } catch (Exception ex) {
            main.outputError("Invalid port configured in the config.yml");
            return false;
        }
        try {
            httpServer = Vertx.vertx().createHttpServer();
            httpServer.requestHandler(httpServerRequest -> {
                main.debug("http request of " + httpServerRequest.uri());
                if (!httpServerRequest.uri().contains("/")) {
                    main.debug("uri did not contain a /.");
                    httpServerRequest.response().end();
                } else {
                    String[] parts = httpServerRequest.uri().split("/");
                    main.debug("parts length = " + parts.length);
                    if (parts.length != 2) {
                        main.debug("uri did not contain a token");
                        httpServerRequest.response().end();
                    } else {
                        String token = parts[1];
                        main.debug("token = " + token);
                        if (UtilToken.isValidToken(token)) {
                            main.debug("token is valid, sending file.");
                            httpServerRequest.response().sendFile(getFileLocation());
                        } else {
                            main.debug("token is invalid.");
                            httpServerRequest.response().end();
                        }
                    }
                }
            });
            httpServer.listen(port);
        } catch (Exception ex) {
            main.outputError("Unable to bind to port. Please assign the plugin to a different port!");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    // TODO this does not unassign the web server from the port.
    public void stop() {
        httpServer.close();
    }

    // -------------------------------------------- //
    // GETTERS
    // -------------------------------------------- //
    public String getFileLocation() {
        return main.getDataFolder().getPath() + File.separator + "web" + File.separator + "rp.zip";
    }

    String getUnzippedFileLocation() {
        return main.getDataFolder().getPath() + File.separator + "web" + File.separator + "rp";
    }

    File getWebDirectory() {
        return new File(main.getDataFolder().getPath() + File.separator + "web");
    }
}
