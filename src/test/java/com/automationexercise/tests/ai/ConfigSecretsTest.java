package com.automationexercise.tests.ai;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import org.testng.annotations.Test;

/** A safety net: secrets must come from environment variables and never sit in config.properties. */
public class ConfigSecretsTest {

    @Test
    public void configFileShouldNotContainSecrets() throws IOException {
        Properties properties = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("config/config.properties")) {
            assertNotNull(in, "config/config.properties not found on the classpath");
            properties.load(in);
        }

        List<String> suspicious = properties.stringPropertyNames().stream()
                .filter(ConfigSecretsTest::looksSensitive)
                .sorted()
                .toList();

        assertTrue(suspicious.isEmpty(),
                "Secrets must come from environment variables, not config.properties: " + suspicious);
    }

    private static boolean looksSensitive(String key) {
        String lower = key.toLowerCase(Locale.ROOT);
        return lower.contains("api.key") || lower.contains("secret") || lower.contains("password")
                || lower.endsWith(".token");
    }
}
