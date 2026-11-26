package com.automationexercise.framework.base;

import com.automationexercise.framework.config.ConfigReader;
import com.automationexercise.framework.utils.WaitUtils;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.TimeoutException;

/**
 * Parent of all page objects. Provides waiting, logging and safe element actions.
 * Actions are protected on purpose: tests talk to page methods (business actions),
 * never to raw locators. Pages contain no assertions.
 */
public abstract class BasePage {

    protected final Logger log = LogManager.getLogger(getClass());
    protected final WebDriver driver;
    protected final WaitUtils waits;

    protected BasePage(WebDriver driver) {
        this.driver = Objects.requireNonNull(driver, "driver must not be null");
        this.waits = new WaitUtils(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait.seconds")));
    }

    // ----- navigation -----

    protected void open(String url) {
        log.info("Opening {}", url);
        driver.get(url);
    }

    /** Opens base.url plus the given path, e.g. openPath("/login"). */
        /** Opens base.url plus the given path, e.g. openPath("/login"). */
    protected void openPath(String path) {
        String base = ConfigReader.get("base.url");
        String normalizedBase = base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
        String normalizedPath = path.startsWith("/") ? path : "/" + path;
        open(normalizedBase + normalizedPath);
        awaitApplicationPage();
    }

    private static final int MAX_LOAD_ATTEMPTS = 3;

    /**
     * The site sometimes answers "heavy load (queue full)" with an empty page title.
     * Reload until a real page appears, and fail with a clear message if it never does.
     */
    private void awaitApplicationPage() {
        for (int attempt = 1; attempt <= MAX_LOAD_ATTEMPTS; attempt++) {
            try {
                waits.waitForNonBlankTitle();
                return;
            } catch (TimeoutException e) {
                log.warn("Page not ready (attempt {} of {}): {}", attempt, MAX_LOAD_ATTEMPTS, driver.getCurrentUrl());
                if (attempt == MAX_LOAD_ATTEMPTS) {
                    throw new IllegalStateException(
                            "The site kept answering with an error page: " + driver.getCurrentUrl(), e);
                }
                driver.navigate().refresh();
                waits.waitForPageLoad();
            }
        }
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected void waitForPageLoad() {
        waits.waitForPageLoad();
    }

    // ----- element actions (each one waits for the right condition first) -----

    protected void click(By locator) {
        log.debug("Clicking {}", locator);
        waits.waitForClickable(locator).click();
    }

    /** Clears the field and types the text. The text itself is never logged (may be sensitive). */
    protected void type(By locator, String text) {
        log.debug("Typing into {}", locator);
        WebElement element = waits.waitForVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return waits.waitForVisible(locator).getText();
    }

    protected String getDomAttribute(By locator, String name) {
        return waits.waitForPresent(locator).getDomAttribute(name);
    }

    protected String getDomProperty(By locator, String name) {
        return waits.waitForPresent(locator).getDomProperty(name);
    }

    /** Immediate check, does NOT wait: true only if the element exists and is visible right now. */
    protected boolean isDisplayed(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    /** Immediate lookup, does NOT wait. Returns an empty list when nothing matches. */
    protected List<WebElement> findAll(By locator) {
        return driver.findElements(locator);
    }

    protected void selectByVisibleText(By locator, String text) {
        log.debug("Selecting '{}' in {}", text, locator);
        new Select(waits.waitForVisible(locator)).selectByVisibleText(text);
    }

    protected String getSelectedOptionText(By locator) {
        return new Select(waits.waitForVisible(locator)).getFirstSelectedOption().getText();
    }

    protected void scrollIntoView(By locator) {
        WebElement element = waits.waitForPresent(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    /** Last resort for elements a normal click cannot reach. Prefer click(). */
    protected void jsClick(By locator) {
        log.debug("JavaScript click on {}", locator);
        WebElement element = waits.waitForPresent(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
}