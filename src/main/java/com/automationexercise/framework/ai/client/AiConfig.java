package com.automationexercise.framework.ai.client;

import com.automationexercise.framework.config.ConfigReader;
import java.nio.file.Path;
import java.time.Duration;

/**
 * Settings for the AI layer, read through {@link ConfigReader} (system property, then environment variable,
 * then config.properties). The API key is read from the OPENAI_API_KEY environment variable and must never
 * be written to a file.
 */
public final class AiConfig {

    private AiConfig() {
        // utility class
    }

    public static String provider() {
        return ConfigReader.get("ai.provider", "openai");
    }

    public static String baseUrl() {
        String url = ConfigReader.get("ai.base.url", "https://api.openai.com/v1");
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    /** The model is configuration, not code: models and pricing change. */
    public static String model() {
        String model = ConfigReader.get("ai.model", "");
        if (model.isBlank()) {
            throw new IllegalStateException("No AI model configured. Set ai.model (for example -Dai.model=NAME or "
                    + "the AI_MODEL environment variable), choosing a model from the provider's current model list.");
        }
        return model;
    }

    public static String apiKey() {
        String key = ConfigReader.get("openai.api.key", "");
        if (key.isBlank()) {
            throw new IllegalStateException("No OpenAI API key found. Set the OPENAI_API_KEY environment variable. "
                    + "Never put the key in a file or commit it to Git.");
        }
        return key;
    }

    public static Duration timeout() {
        return Duration.ofSeconds(intValue("ai.timeout.seconds", 60));
    }

    public static int maxOutputTokens() {
        return intValue("ai.max.output.tokens", 2000);
    }

    /** Most model calls one agent run may make. */
    public static int maxSteps() {
        return intValue("ai.max.steps", 6);
    }

    /** Most tokens one agent run may use in total (a cost cap). */
    public static int maxTotalTokens() {
        return intValue("ai.max.total.tokens", 50000);
    }

    public static Path outputDir() {
        return Path.of(ConfigReader.get("ai.output.dir", "ai-output"));
    }

    private static int intValue(String key, int defaultValue) {
        String value = ConfigReader.get(key, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Configuration key '" + key + "' must be an integer but was: " + value, e);
        }
    }
}
