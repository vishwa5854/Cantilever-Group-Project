package com.cantilever.models;

public class dataBaseQueries {
    static String createUserTable = "create table users(username varchar(40) PRIMARY KEY, password varchar(40)\n" +
            ", email varchar(40));";

    static String createSeatsTable = "create table seats (id  int NOT NULL,seatNo int,cname varchar(20),status " +
            "varchar(10),PRIMARY KEY(id),FOREIGN KEY(ID) REFERENCES bus(id));";
}
