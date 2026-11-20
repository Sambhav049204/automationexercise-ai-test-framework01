package com.automationexercise.framework.utils;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Explicit-wait helpers bound to one driver and one timeout.
 * Every method either returns once the condition holds or throws
 * {@link org.openqa.selenium.TimeoutException} with a description of what was awaited.
 */
public final class WaitUtils {

    private static final Duration POLLING_INTERVAL = Duration.ofMillis(250);

    private final WebDriverWait wait;

    public WaitUtils(WebDriver driver, Duration timeout) {
        this.wait = new WebDriverWait(driver, timeout);
        this.wait.pollingEvery(POLLING_INTERVAL).ignoring(StaleElementReferenceException.class);
    }

    public WebElement waitForPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public List<WebElement> waitForAllVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean waitForInvisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public boolean waitForTextPresent(By locator, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public boolean waitForUrlContains(String fragment) {
        return wait.until(ExpectedConditions.urlContains(fragment));
    }

    public boolean waitForTitleContains(String fragment) {
        return wait.until(ExpectedConditions.titleContains(fragment));
    }

    public void waitForPageLoad() {
        wait.until(d -> "complete".equals(
                ((JavascriptExecutor) d).executeScript("return document.readyState")));
    }
}