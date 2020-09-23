package com.cantilever.routes;

import com.cantilever.models.Buses;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class AdminHandler implements HttpHandler {

    LogManager logManager = LogManager.getLogManager();
    Logger logger = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        // POST request
        // gives a bus id bus name numberOfSeats source destination (sent in the body)

        String busDetails = new String(httpExchange.getRequestBody().readAllBytes());
        JSONParser parser = new JSONParser();
        try {
            JSONObject busInfo = (JSONObject) parser.parse(busDetails);
                if(Buses.createBus(Integer.parseInt(busInfo.get("busId").toString()), busInfo.get("busName").toString(), busInfo.get("source").toString(), busInfo.get("destination").toString(), Integer.parseInt(busInfo.get("numberOfSeats").toString()))){
                String response = "Successfully added the bus";
                Headers headers = httpExchange.getResponseHeaders();
                headers.add("Content-Type", "text");
                OutputStream outputStream = httpExchange.getResponseBody();
                httpExchange.sendResponseHeaders(200, response.length());
                outputStream.write(response.getBytes());
                outputStream.close();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String logMessage = httpExchange.getRequestMethod() +  httpExchange.getRequestURI().toString() + " " + httpExchange.getResponseCode();
        logger.log(Level.INFO, logMessage);


    }

}
