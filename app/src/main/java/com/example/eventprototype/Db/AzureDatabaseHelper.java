package com.example.eventprototype.Db;

import java.sql.*;

public class AzureDatabaseHelper {
    private Connection connection;
    public void connect() throws SQLException {
        String connectionString = "\n" +
                "jdbc:sqlserver://infs3605server.database.windows.net:1433;database=infs3605db;user=CloudSA04b2342e@infs3605server;password={your_password_here};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        connection = DriverManager.getConnection(connectionString);
    }
    public ResultSet executeQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet;
    }


}

