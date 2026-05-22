package it.polimi.ingsw.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://ep-crimson-block-alo6l7uh-pooler.c-3.eu-central-1.aws.neon.tech/neondb?user=neondb_owner&password=npg_gI0ru4jahYAQ&sslmode=require&channelBinding=require";

    private DatabaseManager() {
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.err.println("[ERROR] Exception caught in DatabaseManager: " + e.getMessage());
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }
}
