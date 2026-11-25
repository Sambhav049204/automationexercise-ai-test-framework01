package com.automationexercise.framework.components;

import com.automationexercise.framework.base.BasePage;
import org.openqa.selenium.WebDriver;

/** The top navigation bar, shared by every page. */
public class HeaderComponent extends BasePage {

    public HeaderComponent(WebDriver driver) {
        super(driver);
    }

    public void open(NavLink link) {
        log.info("Opening '{}' from the top navigation", link);
        click(link.locator());
        waits.waitForUrlContains(link.path());
        waitForPageLoad();
    }

    /** Immediate check, does not wait. */
    public boolean isVisible(NavLink link) {
        return isDisplayed(link.locator());
    }
}