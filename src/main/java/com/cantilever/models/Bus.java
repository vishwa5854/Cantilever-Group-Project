package com.cantilever.models;

public class Bus {

    int busId;
    String busName;
    int numberOfSeatsAvailable;
    String from;
    String to;

    Bus(int busId, String busName, int numberOfSeatsAvailable, String from, String to) {

        this.busId = busId;
        this.busName = busName;
        this.numberOfSeatsAvailable = numberOfSeatsAvailable;
        this.from = from;
        this.to = to;

    }

}
