package com.automationexercise.framework.pages;

import com.automationexercise.framework.base.BasePage;
import org.openqa.selenium.WebDriver;

/** Home page of automationexercise.com. Locators and actions are added in Phase 4. */
public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open() {
        openPath("/");
        waitForPageLoad();
        return this;
    }
}