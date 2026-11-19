package com.automationexercise.tests.framework;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

public class LoggingTest {

    private static final Logger LOG = LogManager.getLogger(LoggingTest.class);

    @Test
    public void shouldWriteLogMessagesToLogFile() throws IOException {
        String marker = "logging-smoke-" + UUID.randomUUID();

        LOG.info(marker);

        Path logFile = Path.of("logs", "automation.log");
        assertTrue(Files.exists(logFile), "Log file was not created: " + logFile.toAbsolutePath());
        assertTrue(Files.readString(logFile).contains(marker),
                "Marker message was not found in " + logFile.toAbsolutePath());
    }
}