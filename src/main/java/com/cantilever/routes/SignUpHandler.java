package com.cantilever.routes;

import com.cantilever.Config;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static com.cantilever.routes.Helpers.renderHTMLPage;
import static com.cantilever.models.Users.saveUser;

public class SignUpHandler implements HttpHandler {

    LogManager logManager = LogManager.getLogManager();
    Logger logger = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String methodName = exchange.getRequestMethod();
        if(Objects.equals(methodName, "GET")){
            sendSignUpPage(exchange);
        }
        else if(Objects.equals(methodName, "POST")){
            try {
                receiveUserSignUpData(exchange);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    public void sendSignUpPage(HttpExchange httpExchange) throws IOException {

        // this is a get request
        // load the signup page html into the string variable
        renderHTMLPage(httpExchange, Config.SIGN_UP);
        String logMessage = httpExchange.getRequestMethod() +  httpExchange.getRequestURI().toString() + " " + httpExchange.getResponseCode();
        logger.log(Level.INFO, logMessage);

    }

    public void receiveUserSignUpData(HttpExchange exchange) throws IOException, ParseException {

        // this is a post request
        // we receive the user details as body in POST request
        // store these details in DB
        // render login page for user

        String userData = new String(exchange.getRequestBody().readAllBytes());
        JSONParser parser = new JSONParser();
        JSONObject user = (JSONObject) parser.parse(userData);
        if(saveUser(user.get("name").toString(), user.get("password").toString(), user.get("email").toString())){
//            renderHTMLPage(exchange, Config.HOME_PAGE);
            Headers headers = exchange.getResponseHeaders();
            headers.add("Content-Type", "text/HTML");
            headers.add("Access-Control-Allow-Origin", "*");
            String response = Config.ROOT;
            exchange.sendResponseHeaders(200, response.length());
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        }
        else {
            renderHTMLPage(exchange, "signUpRe.html");
        }
        String logMessage = exchange.getRequestMethod() +  exchange.getRequestURI().toString() + " " + exchange.getResponseCode();
        logger.log(Level.INFO, logMessage);
    }


}
