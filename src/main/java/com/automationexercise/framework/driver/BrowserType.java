package com.automationexercise.framework.driver;

import java.util.Arrays;
import java.util.Locale;

/** Browsers supported by the framework. */
public enum BrowserType {
    CHROME,
    EDGE;

    /** Parses a browser name case-insensitively and fails with a helpful message if unsupported. */
    public static BrowserType fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Browser name must not be empty. Supported browsers: " + Arrays.toString(values()));
        }
        try {
            return valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Unsupported browser '" + value + "'. Supported browsers: " + Arrays.toString(values()), e);
        }
    }
}