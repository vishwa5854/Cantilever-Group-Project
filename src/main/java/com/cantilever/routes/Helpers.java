package com.cantilever.routes;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;



public class Helpers {

    static void renderHTMLPage(HttpExchange httpExchange, String page) throws IOException {
        Headers headers = httpExchange.getResponseHeaders();
        headers.add("Content-Type", "text/HTML");
        String response = readFile(page);
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }

    static String readFile(String fileName) throws IOException {
        ClassLoader classLoader = Helpers.class.getClassLoader();
        String yay = classLoader.getResource(fileName).getFile();
        File file = new File(yay);

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        StringBuilder ans = new StringBuilder();
        String temp = bufferedReader.readLine();
        while (temp != null){
            ans.append(temp);
            temp = bufferedReader.readLine();
        }
        return new String(ans);
    }

}
