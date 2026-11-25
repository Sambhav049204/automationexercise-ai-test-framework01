package com.automationexercise.framework.components;

import com.automationexercise.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** The footer subscription box. */
public class SubscriptionComponent extends BasePage {

    // The id really is spelled "susbscribe_email" on the site (observed in the page reports)
    private static final By EMAIL = By.id("susbscribe_email");
    private static final By SUBSCRIBE_BUTTON = By.id("subscribe");

    public SubscriptionComponent(WebDriver driver) {
        super(driver);
    }

    /** Immediate check, does not wait. */
    public boolean isVisible() {
        return isDisplayed(EMAIL) && isDisplayed(SUBSCRIBE_BUTTON);
    }

    public void subscribe(String email) {
        scrollIntoView(EMAIL);
        type(EMAIL, email);
        click(SUBSCRIBE_BUTTON);
    }
}