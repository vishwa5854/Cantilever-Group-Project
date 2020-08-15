package com.cantilever.models;

public class dataBaseQueries {
    static String createUserTable = "create table users(username varchar(40) PRIMARY KEY, password varchar(40)\n" +
            ", email varchar(40));";
}
