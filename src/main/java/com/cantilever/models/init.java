package com.cantilever.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class init {

    static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/crueger", "root", "");
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }



}
