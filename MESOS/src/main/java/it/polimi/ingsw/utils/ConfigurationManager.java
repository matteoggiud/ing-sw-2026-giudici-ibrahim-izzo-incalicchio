package it.polimi.ingsw.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationManager {

    private static final ConfigurationManager SERVER_INSTANCE = new ConfigurationManager("server.properties");
    private static final ConfigurationManager CLIENT_INSTANCE = new ConfigurationManager("client.properties");

    private final Properties properties = new Properties();

    private ConfigurationManager(String fileName) {
        loadProperties(fileName);
    }

    public static ConfigurationManager getServerInstance() {
        return SERVER_INSTANCE;
    }

    public static ConfigurationManager getClientInstance() {
        return CLIENT_INSTANCE;
    }

    private void loadProperties(String fileName) {
        // First try to load from an external file in the current working directory
        File externalFile = new File(fileName);
        if (externalFile.exists() && externalFile.isFile()) {
            try (InputStream input = new FileInputStream(externalFile)) {
                properties.load(input);
                System.out.println("[CONFIG] Loaded external " + fileName);
                return;
            } catch (IOException e) {
                System.err.println("[CONFIG] Failed to load external " + fileName + ", falling back to internal: " + e.getMessage());
            }
        }

        // Fallback to internal properties file inside the jar
        try (InputStream input = ConfigurationManager.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                System.err.println("[CONFIG] Internal " + fileName + " not found");
            } else {
                properties.load(input);
                System.out.println("[CONFIG] Loaded internal " + fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                System.err.println("[CONFIG] Invalid integer format for key " + key + ": " + value);
            }
        }
        return defaultValue;
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            return Boolean.parseBoolean(value.trim());
        }
        return defaultValue;
    }
}
