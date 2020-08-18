package com.cantilever.routes;

import com.cantilever.models.Buses;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class PaymentHandler implements HttpHandler {

    LogManager logManager = LogManager.getLogManager();
    Logger logger = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        // POST request where we get bus details and seat details along with username
        String requestBody = new String(httpExchange.getRequestBody().readAllBytes());
        JSONParser parser = new JSONParser();
        try {
            JSONObject ticket = (JSONObject) parser.parse(requestBody);
            boolean status = Buses.bookASeat(Integer.parseInt(ticket.get("busId").toString()), Integer.parseInt(ticket.get("seatNumber").toString()), ticket.get("customerName").toString());
            if (status){
                String pageName = "payment.html";
                Helpers.renderHTMLPage(httpExchange, pageName);
            }
            else {

            }
            String logMessage = httpExchange.getRequestMethod() +  httpExchange.getRequestURI().toString() + " " + httpExchange.getResponseCode();
            logger.log(Level.INFO, logMessage);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

}
