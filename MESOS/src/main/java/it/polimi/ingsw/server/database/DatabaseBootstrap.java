package it.polimi.ingsw.server.database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Map;
import java.util.stream.Collectors;
import it.polimi.ingsw.utils.ConfigurationManager;

public class DatabaseBootstrap {

    private static final String DB_NAME = System.getenv().getOrDefault("MESOS_DB_NAME", "mesos");
    private static final String ADMIN_URL = System.getenv().getOrDefault(
            "MESOS_DB_ADMIN_URL",
            "jdbc:postgresql://localhost:5432/postgres"
    );
    private static final String DB_URL = ConfigurationManager.getServerInstance().getProperty("db.url", System.getenv().getOrDefault(
            "MESOS_DB_URL",
            "jdbc:postgresql://localhost:5432/" + DB_NAME
    ));
    private static final String DB_USER = ConfigurationManager.getServerInstance().getProperty("db.user", System.getenv().getOrDefault("MESOS_DB_USER", "postgres"));
    private static final String DB_PASSWORD = ConfigurationManager.getServerInstance().getProperty("db.password", System.getenv().getOrDefault("MESOS_DB_PASSWORD", "password"));

    private static final String SCHEMA_RESOURCE = "/db/schema.sql";
    private static final String DATA_RESOURCE = "/db/data.sql";

    public void bootstrap() {
        createDatabaseIfMissing();
        createTablesIfMissing();
        insertDataIfMissing();
    }

    private void createDatabaseIfMissing() {
        try (Connection conn = DriverManager.getConnection(ADMIN_URL, DB_USER, DB_PASSWORD);
             Statement st = conn.createStatement()) {

            boolean exists = databaseExists(conn, DB_NAME);
            if (!exists) {
                st.executeUpdate("CREATE DATABASE " + quoteIdent(DB_NAME));
                System.out.println("[DB] Database created: " + DB_NAME);
            } else {
                System.out.println("[DB] Database already exists: " + DB_NAME);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create/check database " + DB_NAME, e);
        }
    }

    private void createTablesIfMissing() {
        String schemaSql = readResource(SCHEMA_RESOURCE);
        if (schemaSql.isBlank()) {
            throw new RuntimeException("Schema SQL resource not found or empty: " + SCHEMA_RESOURCE);
        }

        executeSqlScript(DB_URL, schemaSql);
        System.out.println("[DB] Schema ensured.");
    }

    private void insertDataIfMissing() {
        String dataSql = readResource(DATA_RESOURCE);
        if (dataSql.isBlank()) {
            throw new RuntimeException("Data SQL resource not found or empty: " + DATA_RESOURCE);
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            if (hasCardDataLoaded(conn)) {
                System.out.println("[DB] Seed data already present, skipping.");
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed checking seed data", e);
        }

        System.out.println("[DB] Card data incomplete or missing. Truncating and re-inserting seed data...");
        // if data is missing or incomplete, truncate tables to avoid duplicates and maintain integrity, then re-insert data.
        executeSql(DB_URL, "TRUNCATE TABLE card, character_card, building_card, event_card CASCADE"); /*RESTART IDENTITY*/
        executeSqlScript(DB_URL, dataSql);
        System.out.println("[DB] Seed data inserted.");
    }

    private void executeSql(String dbUrl, String statement) {
        try (Connection conn = DriverManager.getConnection(dbUrl, DB_USER, DB_PASSWORD);
             Statement st = conn.createStatement()) {
            st.execute(statement);
        } catch (SQLException e) {
            throw new RuntimeException("Failed executing SQL statement on " + dbUrl, e);
        }
    }


    private boolean hasCardDataLoaded(Connection conn) throws SQLException {
        boolean dataFound = false;
        Map<String, Integer> queries = Map.of(
                "SELECT COUNT(*) as count FROM card", 117,
                "SELECT COUNT(*) as count FROM building_card", 21,
                "SELECT COUNT(*) as count FROM event_card", 12,
                "SELECT COUNT(*) as count FROM character_card", 84
        );

        for (String q : queries.keySet()) {
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(q)) {
                if (rs.next()) {
                    if (rs.getInt("count") == queries.get(q))
                        dataFound = true;
                    else
                        return false;
                }
            } catch (SQLException ignored) {
                // table may not exist yet, ignore
            }
        }
        return dataFound;
    }

    private void executeSqlScript(String url, String sqlScript) {
        try (Connection conn = DriverManager.getConnection(url, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            for (String statement : splitSqlStatements(sqlScript)) {
                String trimmed = statement.trim();
                if (trimmed.isEmpty()) continue;
                try (Statement st = conn.createStatement()) {
                    st.execute(trimmed);
                }
            }

            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Failed executing SQL script on " + url, e);
        }
    }

    private String readResource(String resourcePath) {
        try (InputStream is = DatabaseBootstrap.class.getResourceAsStream(resourcePath)) {
            if (is == null) return "";
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return br.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed reading resource " + resourcePath, e);
        }
    }

    private boolean databaseExists(Connection conn, String dbName) throws SQLException {
        String sql = "SELECT 1 FROM pg_database WHERE datname = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dbName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String quoteIdent(String ident) {
        return "\"" + ident.replace("\"", "\"\"") + "\"";
    }

    private String[] splitSqlStatements(String sql) {
        return sql.split(";\\s*(\\r?\\n|$)");
    }
}