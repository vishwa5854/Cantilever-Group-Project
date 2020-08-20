package com.cantilever.routes;

import com.cantilever.Config;
import com.cantilever.models.Buses;
import com.mysql.cj.xdevapi.JsonParser;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class SelectSeats implements HttpHandler {

    LogManager logManager = LogManager.getLogManager();
    Logger logger = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String methodName = httpExchange.getRequestMethod();
        if(Objects.equals(methodName, "GET")){
            sendSeatsHtmlPage(httpExchange);
        }
        else if(Objects.equals(methodName, "POST")){
            sendAllSeats(httpExchange);
        }

    }

    public void sendSeatsHtmlPage(HttpExchange httpExchange) throws IOException {

        // GET REQUEST
        URI uri = httpExchange.getRequestURI();
        String te = uri.getQuery();
        te = te.replace("?", "=");
        String[] queryData = te.split("=");
        System.out.println(queryData[0] + queryData[1]);
        String append = "<script>let busIdd='"+ queryData[1] + "';</script>";
        System.out.println("append" + append);
        Headers headers = httpExchange.getResponseHeaders();
        headers.add("Content-Type", "text/HTML");
        headers.add("Access-Control-Allow-Origin", "*");
        String response = Helpers.readFile(Config.BOOK_A_SEAT) + append;
        System.out.println(response);
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
        String logMessage = httpExchange.getRequestMethod() +  httpExchange.getRequestURI().toString() + " " + httpExchange.getResponseCode();
        logger.log(Level.INFO, logMessage);

    }

    public static void sendAllSeats(HttpExchange httpExchange) throws IOException {

        //POST REQUEST
        String body  = new String(httpExchange.getRequestBody().readAllBytes());
        JSONParser parser = new JSONParser();
        try {
            JSONObject data = (JSONObject) parser.parse(body);
            ArrayList<String> lol = Buses.getAllSeats(Integer.parseInt(data.get("busId").toString()));
            JSONObject response = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for(String s : lol){
                jsonArray.add(s);
            }
            response.put("seats", jsonArray);
            Headers headers = httpExchange.getResponseHeaders();
            headers.add("Content-Type", "text/HTML");
            headers.add("Access-Control-Allow-Origin", "*");
            httpExchange.sendResponseHeaders(200, response.toJSONString().length());
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.toJSONString().getBytes());
            outputStream.close();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
