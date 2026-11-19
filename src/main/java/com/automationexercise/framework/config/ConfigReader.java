package com.automationexercise.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

/**
 * Central access point for framework configuration.
 *
 * <p>Resolution order for every key (first non-blank value wins):
 * <ol>
 *   <li>JVM system property, e.g. {@code -Dbrowser=edge}</li>
 *   <li>Environment variable, e.g. {@code BROWSER=edge}
 *       (key is upper-cased, dots and hyphens become underscores: {@code base.url} -> {@code BASE_URL})</li>
 *   <li>{@code config/config.properties} on the classpath</li>
 * </ol>
 */
public final class ConfigReader {

    private static final String CONFIG_FILE = "config/config.properties";
    private static final Properties FILE_PROPERTIES = loadFile();

    private ConfigReader() {
        // utility class, not meant to be instantiated
    }

    /** Returns the value for the key, or throws if it is not configured anywhere. */
    public static String get(String key) {
        String value = resolve(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing configuration key: " + key);
        }
        return value;
    }

    /** Returns the value for the key, or the default when it is not configured. */
    public static String get(String key, String defaultValue) {
        String value = resolve(key);
        return value != null ? value : defaultValue;
    }

    public static int getInt(String key) {
        String value = get(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Configuration key '" + key + "' must be an integer but was: " + value, e);
        }
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    private static String resolve(String key) {
        String value = System.getProperty(key);
        if (isBlank(value)) {
            value = System.getenv(toEnvName(key));
        }
        if (isBlank(value)) {
            value = FILE_PROPERTIES.getProperty(key);
        }
        return isBlank(value) ? null : value.trim();
    }

    private static String toEnvName(String key) {
        return key.replace('.', '_').replace('-', '_').toUpperCase(Locale.ROOT);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static Properties loadFile() {
        Properties properties = new Properties();
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (in == null) {
                throw new IllegalStateException("Configuration file not found on classpath: " + CONFIG_FILE);
            }
            properties.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read configuration file: " + CONFIG_FILE, e);
        }
        return properties;
    }
}