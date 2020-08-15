package com.cantilever.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Objects;

import static com.cantilever.models.init.connection;

public class Users {

    public static boolean saveUser(String username, String password, String emailAddress){
        try{
            PreparedStatement state = connection.prepareStatement("insert into users values(?, ?, ?)");
            state.setString(1,username);
            state.setString(2,password);
            state.setString(3,emailAddress);
            state.executeUpdate();
        }
        catch (SQLIntegrityConstraintViolationException exception){
            // PRIMARY KEY ERROR i.e., user already exists
            return false;
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return true;
    }

    public static boolean signIn(String username, String password) throws SQLException {
        String query = "SELECT password from users where username = (?)";
        PreparedStatement state = connection.prepareStatement(query);
        state.setString(1, username);
        ResultSet resultSet = state.executeQuery();
        String passwordFromDB = "";
        while (resultSet.next()){
            passwordFromDB = resultSet.getString(1);
        }
        return Objects.equals(passwordFromDB, password);
    }

}
