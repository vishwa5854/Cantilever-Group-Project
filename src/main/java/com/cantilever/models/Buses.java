package com.cantilever.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;

import static com.cantilever.models.init.connection;


public class Buses {

    public static HashSet<String> findBuses(String source, String destination) throws SQLException {

        HashSet<String> response = new HashSet<>();
        String query = "SELECT * FROM buses where source = ? and destination = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, source);
        preparedStatement.setString(2, destination);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String temp = resultSet.getInt(1) + "," + resultSet.getString(2) + "," + resultSet.getString(4) + "," + resultSet.getString(5);
            response.add(temp);
        }

        return response;

    }

    public static boolean bookASeat(int busId, int seatNumber, String customerName) {

        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("update seats set status = 'booked', customerName = ? where busId = ? and seatNumber = ?");
            preparedStatement.setString(1, customerName);
            preparedStatement.setInt(2, busId);
            preparedStatement.setInt(3, seatNumber);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return false;
        }

    }

    public static ArrayList<String> getAllSeats(int busId){
        ArrayList<String> seats = new ArrayList<>();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("select status from seats where busId = ?");
            preparedStatement.setInt(1, busId);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()){
                seats.add(result.getString(1));
            }
            return seats;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static boolean createBus(int busId, String busName, String source, String destination, int numberOfSeats) {


        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into buses values(?,?,?,?,?)");
            preparedStatement.setInt(1, busId);
            preparedStatement.setString(2, busName);
            preparedStatement.setInt(3, numberOfSeats);
            preparedStatement.setString(4, source);
            preparedStatement.setString(5, destination);
            preparedStatement.executeUpdate();
            for (int i = 0; i < 40; i++) {
                PreparedStatement statement = connection.prepareStatement("insert into seats values(?, ?,'lol', 'Not booked')");
                statement.setInt(1, busId);
                statement.setInt(2, i + 1);
                statement.execute();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

}

