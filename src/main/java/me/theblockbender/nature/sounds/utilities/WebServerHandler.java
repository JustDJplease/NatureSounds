package me.theblockbender.nature.sounds.utilities;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import me.theblockbender.nature.sounds.NatureSounds;

public class WebServerHandler {

    private HttpServer httpServer;
    private NatureSounds main;

    public WebServerHandler(NatureSounds main){
        this.main = main;
    }

    public void start(){
        httpServer = Vertx.vertx().createHttpServer();
        httpServer.requestHandler(httpServerRequest -> httpServerRequest.response().end("test"));
        httpServer.listen(main.getConfig().getInt("port"));
    }
}
