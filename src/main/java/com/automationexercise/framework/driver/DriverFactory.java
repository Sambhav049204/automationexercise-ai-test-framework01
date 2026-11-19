package com.automationexercise.framework.driver;

import com.automationexercise.framework.config.ConfigReader;
import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

/** Creates configured WebDriver instances. Stateless: it never stores a driver. */
public final class DriverFactory {

    private static final Logger LOG = LogManager.getLogger(DriverFactory.class);

    private DriverFactory() {
        // utility class
    }

    public static WebDriver createDriver(BrowserType browser, boolean headless) {
        LOG.info("Creating {} driver (headless={})", browser, headless);

        WebDriver driver = switch (browser) {
            case CHROME -> new ChromeDriver(chromeOptions(headless));
            case EDGE -> new EdgeDriver(edgeOptions(headless));
        };

        try {
            // No implicit wait on purpose: all waiting is explicit (WaitUtils, next step).
            driver.manage().timeouts()
                    .pageLoadTimeout(Duration.ofSeconds(ConfigReader.getInt("page.load.timeout.seconds")));
            if (!headless) {
                driver.manage().window().maximize();
            }
        } catch (RuntimeException e) {
            driver.quit(); // do not leak a browser if configuration fails
            throw e;
        }
        return driver;
    }

    private static ChromeOptions chromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        if (headless) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        return options;
    }

    private static EdgeOptions edgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-notifications");
        if (headless) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        return options;
    }
}