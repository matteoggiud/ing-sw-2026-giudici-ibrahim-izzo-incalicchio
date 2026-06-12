package it.polimi.ingsw.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import it.polimi.ingsw.common.utils.ConfigurationManager;

public class DatabaseManager {
    // OLD private static final String URL = "jdbc:postgresql://ep-crimson-block-alo6l7uh-pooler.c-3.eu-central-1.aws.neon.tech/neondb?user=neondb_owner&password=npg_gI0ru4jahYAQ&sslmode=require&channelBinding=require";
    private static final String URL = ConfigurationManager.getServerInstance().getProperty("db.url", System.getenv().getOrDefault("MESOS_DB_URL", "jdbc:postgresql://localhost:5432/mesos"));
    private static final String USER = ConfigurationManager.getServerInstance().getProperty("db.user", System.getenv().getOrDefault("MESOS_DB_USER", "postgres"));
    private static final String PASSWORD = ConfigurationManager.getServerInstance().getProperty("db.password", System.getenv().getOrDefault("MESOS_DB_PASSWORD", "password"));

    private DatabaseManager() {
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL,  USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("[ERROR] Exception caught in DatabaseManager: " + e.getMessage());
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }
}
