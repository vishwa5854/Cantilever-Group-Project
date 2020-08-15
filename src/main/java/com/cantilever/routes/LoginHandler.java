package com.cantilever.routes;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static com.cantilever.models.Users.signIn;
import static com.cantilever.routes.Helpers.renderHTMLPage;


public class LoginHandler  implements HttpHandler {

    LogManager logManager = LogManager.getLogManager();
    Logger logger = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String userData = new String(exchange.getRequestBody().readAllBytes());
        JSONParser parser = new JSONParser();
        try {
            JSONObject user = (JSONObject) parser.parse(userData);
            if(signIn(user.get("name").toString(), user.get("password").toString())){
                System.out.println("YES");
                renderHTMLPage(exchange, "busDetails.html");
            }
            else {
                renderHTMLPage(exchange, "login.html");
            }
            String logMessage = exchange.getRequestMethod() +  exchange.getRequestURI().toString() + " " + exchange.getResponseCode();
            logger.log(Level.INFO, logMessage);
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
        }

    }

}
