package com.cantilever.routes;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static com.cantilever.routes.Helpers.renderHTMLPage;

public class HomeHandler implements HttpHandler {

    LogManager logManager = LogManager.getLogManager();
    Logger logger = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String methodName = exchange.getRequestMethod();
        if(Objects.equals(methodName, "GET")){
            handleGETRequest(exchange);
        }
        else if(Objects.equals(methodName, "POST")){
            handlePOSTRequest(exchange);
        }

    }

    public void handleGETRequest(HttpExchange exchange) throws IOException {

        // need to send a home html page as response
        String homePage = "temp.html";
        renderHTMLPage(exchange, homePage);
        String logMessage = exchange.getRequestMethod() +  exchange.getRequestURI().toString() + " " + exchange.getResponseCode();
        logger.log(Level.INFO, logMessage);

    }

    public void handlePOSTRequest(HttpExchange exchange) throws IOException {

        // same as GET for this page
        handleGETRequest(exchange);

    }

}
