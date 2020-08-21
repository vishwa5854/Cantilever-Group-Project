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
        System.out.println(requestBody);
        JSONParser parser = new JSONParser();
        try {
            JSONObject ticket = (JSONObject) parser.parse(requestBody);
            boolean status = Buses.bookASeat(Integer.parseInt(ticket.get("busId").toString()), Integer.parseInt(ticket.get("seatNumber").toString()), ticket.get("customerName").toString());
            if (status){
                String append = "<script>document.getElementById('busId').innerHTML = " + "'Bus Id : " + ticket.get("busId").toString() + "';document.getElementById('seatNumber').innerHTML = 'Seat Number : " + ticket.get("seatNumber").toString() + "';document.getElementById('passenger').innerHTML = 'Seat Number : " + ticket.get("customerName").toString() + "'</script>";
                System.out.println(append);
                Headers headers = httpExchange.getResponseHeaders();
                headers.add("Content-Type", "text/HTML");
                headers.add("Access-Control-Allow-Origin", "*");
                String response = Helpers.readFile(Config.PAYMENT_PAGE) + append;
                System.out.println("In payment page" + response);
                httpExchange.sendResponseHeaders(200, response.length());
                OutputStream outputStream = httpExchange.getResponseBody();
                outputStream.write(response.getBytes());
                outputStream.close();
                String logMessage = httpExchange.getRequestMethod() +  httpExchange.getRequestURI().toString() + " " + httpExchange.getResponseCode();
                logger.log(Level.INFO, logMessage);
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
