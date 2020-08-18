package com.cantilever.routes;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class FindBusHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        // POST request
        String fromAndTo = new String(httpExchange.getRequestBody().readAllBytes());
        JSONParser parser = new JSONParser();
        try {
            JSONObject startAndEnd = (JSONObject) parser.parse(fromAndTo);
            
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
