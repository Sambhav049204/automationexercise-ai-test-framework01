package com.automationexercise.framework.pages;

import com.automationexercise.framework.base.BasePage;
import com.automationexercise.framework.components.HeaderComponent;
import com.automationexercise.framework.components.SubscriptionComponent;
import org.openqa.selenium.WebDriver;

/** Home page of automationexercise.com. */
public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open() {
        openPath("/");
        waitForPageLoad();
        return this;
    }

    public HeaderComponent header() {
        return new HeaderComponent(driver);
    }

    public SubscriptionComponent subscription() {
        return new SubscriptionComponent(driver);
    }
}