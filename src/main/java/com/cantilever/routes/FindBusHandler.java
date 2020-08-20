package com.cantilever.routes;

import com.cantilever.Config;
import com.cantilever.models.Buses;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class FindBusHandler implements HttpHandler {

    LogManager logManager = LogManager.getLogManager();
    Logger logger = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {


        String methodName = httpExchange.getRequestMethod();
        if(Objects.equals(methodName, "GET")){
            sendFindBusesPage(httpExchange);
        }
        else if(Objects.equals(methodName, "POST")){
            handlePostRequest(httpExchange);
        }

    }

    private void sendFindBusesPage(HttpExchange httpExchange) throws IOException {
        Helpers.renderHTMLPage(httpExchange, Config.FIND_BUSES);
    }

    private void handlePostRequest(HttpExchange httpExchange) throws IOException {
        // POST request
        System.out.println("inside post buses");
        String fromAndTo = new String(httpExchange.getRequestBody().readAllBytes());
        JSONParser parser = new JSONParser();
        try {
            JSONObject startAndEnd = (JSONObject) parser.parse(fromAndTo);
            HashSet<String> busDetails = Buses.findBuses(startAndEnd.get("from").toString(), startAndEnd.get("to").toString());
            String temp = "";
            for(String bus : busDetails){
                temp = bus;
                break;
            }
            System.out.println(temp);
            String[] buses = temp.split(",");
            String append = "<script>document.getElementById('busId').innerHTML = " + "'Bus Id : " + buses[0] + "&nbsp;&nbsp;&nbsp;&nbsp;Bus Name : " + buses[1] + "';document.getElementById('from').innerHTML = " + "'From : " + buses[2] + "&nbsp;&nbsp;&nbsp;&nbsp;To : " + buses[3] + "'</script>";
            System.out.println("append" + append);
            JSONObject response = new JSONObject();
            response.put("busId",buses[0]);
            response.put("busName", buses[1]);
            response.put("from", buses[2]);
            response.put("to", buses[3]);
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
