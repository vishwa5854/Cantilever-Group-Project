package com.cantilever.routes;

import com.cantilever.Config;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class DisplayBuses implements HttpHandler {

    LogManager logManager = LogManager.getLogManager();
    Logger logger = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        // post request
        URI uri = httpExchange.getRequestURI();
        String te = uri.getQuery();
        te = te.replace("?", "=");
        String[] queryData = te.split("=");
        System.out.println(queryData[0] + queryData[1] + queryData[2] + queryData[3]);
        String append = "<script>let busIdd='"+ queryData[0] + "';document.getElementById('busId').innerHTML = " + "'Bus Id : " + queryData[0] + "&nbsp;&nbsp;&nbsp;&nbsp;Bus Name : " + queryData[1] + "';document.getElementById('from').innerHTML = " + "'From : " + queryData[2] + "&nbsp;&nbsp;&nbsp;&nbsp;To : " + queryData[3] + "'</script>";
        System.out.println("append" + append);
        Headers headers = httpExchange.getResponseHeaders();
        headers.add("Content-Type", "text/HTML");
        headers.add("Access-Control-Allow-Origin", "*");
        String response = Helpers.readFile(Config.FETCH_BUSES) + append;
        System.out.println(response);
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
        String logMessage = httpExchange.getRequestMethod() +  httpExchange.getRequestURI().toString() + " " + httpExchange.getResponseCode();
        logger.log(Level.INFO, logMessage);
    }

}
