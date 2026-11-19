package com.automationexercise.tests.framework;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import com.automationexercise.framework.config.ConfigReader;
import org.testng.annotations.Test;

public class ConfigReaderTest {

    @Test
    public void shouldReadValueFromPropertiesFile() {
        assertEquals(ConfigReader.get("base.url"), "https://automationexercise.com");
    }

    @Test
    public void shouldReturnDefaultWhenKeyIsMissing() {
        assertEquals(ConfigReader.get("does.not.exist", "fallback"), "fallback");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldFailWhenRequiredKeyIsMissing() {
        ConfigReader.get("does.not.exist");
    }

    @Test
    public void systemPropertyShouldOverrideFileValue() {
        System.setProperty("browser", "edge");
        try {
            assertEquals(ConfigReader.get("browser"), "edge");
        } finally {
            System.clearProperty("browser");
        }
    }

    @Test
    public void shouldParseIntegerValues() {
        System.setProperty("test.int.value", "42");
        try {
            assertEquals(ConfigReader.getInt("test.int.value"), 42);
        } finally {
            System.clearProperty("test.int.value");
        }
    }

    @Test
    public void shouldParseBooleanValues() {
        System.setProperty("test.flag.value", "true");
        try {
            assertTrue(ConfigReader.getBoolean("test.flag.value"));
        } finally {
            System.clearProperty("test.flag.value");
        }
    }
}