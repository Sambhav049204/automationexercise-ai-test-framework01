package com.automationexercise.framework.utils;

import com.automationexercise.framework.config.ConfigReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/** Saves browser screenshots as PNG files. */
public final class ScreenshotUtils {

    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    private ScreenshotUtils() {
        // utility class
    }

    /**
     * Captures the current viewport and saves it as {@code <screenshot.dir>/<timestamp>_<name>.png}.
     *
     * @return the path of the saved file
     */
    public static Path capture(WebDriver driver, String name) {
        if (!(driver instanceof TakesScreenshot screenshotDriver)) {
            throw new IllegalArgumentException("Driver cannot take screenshots: " + driver.getClass().getName());
        }
        byte[] png = screenshotDriver.getScreenshotAs(OutputType.BYTES);

        Path directory = Path.of(ConfigReader.get("screenshot.dir", "screenshots"));
        Path file = directory.resolve(LocalDateTime.now().format(TIMESTAMP) + "_" + sanitize(name) + ".png");
        try {
            Files.createDirectories(directory);
            Files.write(file, png);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not save screenshot " + file, e);
        }
        return file;
    }

    private static String sanitize(String name) {
        return name == null || name.isBlank() ? "unnamed" : name.replaceAll("[^A-Za-z0-9._-]", "_");
    }
}