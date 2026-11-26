package com.automationexercise.framework.utils;

import java.util.Locale;
import org.openqa.selenium.By;

/** Builders for XPath locators that ignore letter case (the site shows some text in capitals). */
public final class XPaths {

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private XPaths() {
        // utility class
    }

    /** An element with this tag whose whole (whitespace-normalised) text equals the text, ignoring case. */
    public static By tagWithText(String tag, String text) {
        return By.xpath("//" + tag + "[translate(normalize-space(.),'" + LOWER + "','" + UPPER + "')='"
                + checked(text).toUpperCase(Locale.ROOT) + "']");
    }

    /** Any element whose own text contains the text, ignoring case. */
    public static By containingText(String text) {
        return By.xpath("//body//*[not(self::script) and not(self::style) and contains(translate(text(),'"
                + UPPER + "','" + LOWER + "'),'" + checked(text).toLowerCase(Locale.ROOT) + "')]");
    }

    private static String checked(String text) {
        if (text == null || text.isBlank() || text.contains("'")) {
            throw new IllegalArgumentException("Text must be non-empty and must not contain an apostrophe: " + text);
        }
        return text;
    }
}