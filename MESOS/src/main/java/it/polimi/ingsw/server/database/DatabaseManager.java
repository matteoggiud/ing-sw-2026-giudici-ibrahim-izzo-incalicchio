package it.polimi.ingsw.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://ep-crimson-block-alo6l7uh-pooler.c-3.eu-central-1.aws.neon.tech/neondb?user=neondb_owner&password=npg_gI0ru4jahYAQ&sslmode=require&channelBinding=require";
    /*private static final String USER = "neondb_owner";
    private static final String PASSWORD = "neondb_owner";*/

    private static Connection connection;

    private DatabaseManager() {
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
                System.out.println("Connected to the PostgreSQL database.");
            }
        } catch (SQLException e) {
            System.err.println("Error connecting to the PostgreSQL database.");
            e.printStackTrace();
        }

        return connection;
    }
}
