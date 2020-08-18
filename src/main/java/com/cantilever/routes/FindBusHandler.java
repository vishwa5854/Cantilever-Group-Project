package com.cantilever.routes;

import com.cantilever.models.Buses;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class FindBusHandler implements HttpHandler {

    LogManager logManager = LogManager.getLogManager();
    Logger logger = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        // POST request
        String fromAndTo = new String(httpExchange.getRequestBody().readAllBytes());
        JSONParser parser = new JSONParser();
        try {
            JSONObject startAndEnd = (JSONObject) parser.parse(fromAndTo);
            HashSet<String> busDetails = Buses.findBuses(startAndEnd.get("from").toString(), startAndEnd.get("to").toString());
            JSONObject response = new JSONObject();
            JSONArray allBuses = new JSONArray();
            for(String bus : busDetails){
                allBuses.add(bus);
            }
            response.put("buses", allBuses);
            Headers headers = httpExchange.getResponseHeaders();
            headers.add("Content-Type", "text/json");
            httpExchange.sendResponseHeaders(200, response.toJSONString().length());
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.toJSONString().getBytes());
            outputStream.close();
            String logMessage = httpExchange.getRequestMethod() +  httpExchange.getRequestURI().toString() + " " + httpExchange.getResponseCode();
            logger.log(Level.INFO, logMessage);
        }
        catch (ParseException | SQLException e) {
            e.printStackTrace();
        }

    }

}
