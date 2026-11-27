package com.automationexercise.tests.ai;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

import com.automationexercise.framework.ai.client.AiConfig;
import java.time.Duration;
import org.testng.SkipException;
import org.testng.annotations.Test;

public class AiConfigTest {

    @Test
    public void shouldUseTheDocumentedDefaults() {
        assertEquals(AiConfig.provider(), "openai");
        assertEquals(AiConfig.baseUrl(), "https://api.openai.com/v1");
        assertEquals(AiConfig.timeout(), Duration.ofSeconds(60));
        assertEquals(AiConfig.maxSteps(), 6);
    }

    @Test
    public void shouldTakeTheModelFromASystemProperty() {
        System.setProperty("ai.model", "test-model");
        try {
            assertEquals(AiConfig.model(), "test-model");
        } finally {
            System.clearProperty("ai.model");
        }
    }

    @Test
    public void shouldFailClearlyWhenNoModelIsConfigured() {
        if (System.getenv("AI_MODEL") != null) {
            throw new SkipException("AI_MODEL is set in this environment");
        }

        IllegalStateException error = expectThrows(IllegalStateException.class, AiConfig::model);

        assertTrue(error.getMessage().contains("ai.model"), error.getMessage());
    }

    @Test
    public void shouldFailClearlyWhenNoApiKeyIsConfigured() {
        if (System.getenv("OPENAI_API_KEY") != null) {
            throw new SkipException("OPENAI_API_KEY is set in this environment");
        }

        IllegalStateException error = expectThrows(IllegalStateException.class, AiConfig::apiKey);

        assertTrue(error.getMessage().contains("OPENAI_API_KEY"), error.getMessage());
    }
}
