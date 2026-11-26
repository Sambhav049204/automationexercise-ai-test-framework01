package com.automationexercise.framework.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/** Recognises the site's own "heavy load (queue full)" page, so environment problems are not mistaken for defects. */
public final class SiteHealth {

    // Observed: the overload page has an empty title and the heading "This website is under heavy load (queue full)"
    private static final String SCRIPT = """
            return (document.title || '').trim() === ''
                && !!document.body
                && document.body.innerText.toLowerCase().indexOf('queue full') >= 0;
            """;

    private SiteHealth() {
        // utility class
    }

    /** True if the browser is currently showing the overload page. Never throws. */
    public static boolean isOverloaded(WebDriver driver) {
        try {
            return Boolean.TRUE.equals(((JavascriptExecutor) driver).executeScript(SCRIPT));
        } catch (RuntimeException e) {
            return false;
        }
    }
}