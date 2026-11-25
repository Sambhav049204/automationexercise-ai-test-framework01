package com.automationexercise.framework.components;

import org.openqa.selenium.By;

/** Links in the top navigation bar. Paths were observed in the site's navigation (href values). */
public enum NavLink {
    HOME("/"),
    PRODUCTS("/products"),
    CART("/view_cart"),
    SIGNUP_LOGIN("/login"),
    TEST_CASES("/test_cases"),
    API_TESTING("/api_list"),
    CONTACT_US("/contact_us");

    private final String path;

    NavLink(String path) {
        this.path = path;
    }

    public String path() {
        return path;
    }

    public By locator() {
        if (this == HOME) {
            // two links point to "/" (the logo and "Home"), so the visible text tells them apart
            return By.xpath("//a[@href='/' and normalize-space()='Home']");
        }
        return By.cssSelector("a[href='" + path + "']");
    }
}