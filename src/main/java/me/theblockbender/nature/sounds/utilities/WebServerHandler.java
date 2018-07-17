package me.theblockbender.nature.sounds.utilities;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import me.theblockbender.nature.sounds.NatureSounds;

import java.io.File;

public class WebServerHandler {

    // -------------------------------------------- //
    // INSTANCES & VARIABLES
    // -------------------------------------------- //
    public int port;

    private HttpServer httpServer;
    private NatureSounds main;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public WebServerHandler(NatureSounds main) {
        this.main = main;
    }

    // -------------------------------------------- //
    // WEB HANDLERS
    // -------------------------------------------- //
    // TODO verify if this works.
    public boolean start() {
        try {
            port = main.getConfig().getInt("port");
        } catch (Exception ex) {
            main.outputError("Invalid port configured in the config.yml");
            return false;
        }
        httpServer = Vertx.vertx().createHttpServer();
        httpServer.requestHandler(httpServerRequest -> httpServerRequest.response().sendFile(getFileLocation()).end());
        httpServer.listen(port);
        return true;
    }

    // TODO verify if this works.
    public void stop() {
        httpServer.close();
    }

    // -------------------------------------------- //
    // GETTERS
    // -------------------------------------------- //
    // TODO verify if this works.
    private String getFileLocation() {
        return main.getDataFolder().getPath() + File.separator + "web" + File.separator + "rp.zip";
    }
}
