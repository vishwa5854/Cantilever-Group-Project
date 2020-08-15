package com.cantilever;

import com.cantilever.routes.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Server {

    public static void main(String[] args) throws IOException {

        LogManager logManager = LogManager.getLogManager();
        Logger logger = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.log(Level.INFO, "Server Starting on port 3000");
        HttpServer server = HttpServer.create(new InetSocketAddress(3000), 0);
        server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
        server.createContext("/", new HomeHandler());
        server.createContext("/login", new LoginHandler());
        server.createContext("/signup", new SignUpHandler());
        server.start();

    }

}
