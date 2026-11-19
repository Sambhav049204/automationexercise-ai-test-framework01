package com.automationexercise.framework.driver;

import com.automationexercise.framework.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/** Holds one WebDriver per thread so tests can run in parallel safely. */
public final class DriverManager {

    private static final Logger LOG = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
        // utility class
    }

    /** Starts a driver using the "browser" and "headless" configuration values. */
    public static void initDriver() {
        initDriver(BrowserType.fromString(ConfigReader.get("browser")), ConfigReader.getBoolean("headless"));
    }

    public static void initDriver(BrowserType browser, boolean headless) {
        if (DRIVER.get() != null) {
            throw new IllegalStateException("A driver is already initialised for thread '"
                    + Thread.currentThread().getName() + "'. Call quitDriver() first.");
        }
        DRIVER.set(DriverFactory.createDriver(browser, headless));
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("No driver for thread '"
                    + Thread.currentThread().getName() + "'. Call initDriver() first.");
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            return;
        }
        try {
            driver.quit();
        } catch (RuntimeException e) {
            LOG.warn("Driver did not quit cleanly: {}", e.getMessage());
        } finally {
            DRIVER.remove();
        }
    }
}