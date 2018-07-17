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
    public String ip;

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
            httpServer.requestHandler(httpServerRequest -> httpServerRequest.response().sendFile(getFileLocation()));
            httpServer.listen(port);
        }catch(Exception ex){
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
    private String getFileLocation() {
        return main.getDataFolder().getPath() + File.separator + "web" + File.separator + "index.html";
    }
}
