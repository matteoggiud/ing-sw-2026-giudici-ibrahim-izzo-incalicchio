package it.polimi.ingsw.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationManager {

    private static ConfigurationManager serverInstance;
    private static ConfigurationManager clientInstance;

    private final Properties properties = new Properties();

    private ConfigurationManager(String fileName) {
        loadProperties(fileName);
    }

    public static synchronized ConfigurationManager getServerInstance() {
        if (serverInstance == null) {
            serverInstance = new ConfigurationManager("server.properties");
        }
        return serverInstance;
    }

    public static synchronized ConfigurationManager getClientInstance() {
        if (clientInstance == null) {
            clientInstance = new ConfigurationManager("client.properties");
        }
        return clientInstance;
    }

    private void loadProperties(String fileName) {
        // 1. Load internal properties file inside the jar (as default baseline)
        try (InputStream input = ConfigurationManager.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                System.err.println("[CONFIG] Internal " + fileName + " not found");
            } else {
                properties.load(input);
                System.out.println("[CONFIG] Loaded internal " + fileName);
            }
        } catch (IOException e) {
            System.err.println("[CONFIG] Failed to load internal " + fileName + ": " + e.getMessage());
        }

        // 2. Try to load from an external file in the current working directory (overrides internal ones)
        File externalFile = new File(fileName);
        if (externalFile.exists() && externalFile.isFile()) {
            try (InputStream input = new FileInputStream(externalFile)) {
                properties.load(input);
                System.out.println("[CONFIG] Loaded external " + fileName + " (overriding defaults)");
            } catch (IOException e) {
                System.err.println("[CONFIG] Failed to load external " + fileName + ": " + e.getMessage());
            }
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
