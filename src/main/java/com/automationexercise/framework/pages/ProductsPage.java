package com.automationexercise.framework.pages;

import com.automationexercise.framework.base.BasePage;
import com.automationexercise.framework.components.HeaderComponent;
import com.automationexercise.framework.components.SidebarComponent;
import com.automationexercise.framework.components.SubscriptionComponent;
import com.automationexercise.framework.utils.XPaths;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

/** The "ALL PRODUCTS" page (/products), including product search. */
public class ProductsPage extends BasePage {

    // Observed: h2 with classes "title text-center" holds "ALL PRODUCTS"
    private static final By HEADING = By.cssSelector("h2.title.text-center");
    private static final By SEARCH_INPUT = By.id("search_product");
    private static final By SEARCH_BUTTON = By.id("submit_search");
    // Official test case 9 names the heading "SEARCHED PRODUCTS"
    private static final By SEARCH_RESULTS_HEADING = XPaths.containingText("searched products");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public ProductsPage open() {
        openPath("/products");
        waitForPageLoad();
        return this;
    }

    public HeaderComponent header() {
        return new HeaderComponent(driver);
    }

    public SidebarComponent sidebar() {
        return new SidebarComponent(driver);
    }

    public SubscriptionComponent subscription() {
        return new SubscriptionComponent(driver);
    }

    /** The heading as displayed, for example "ALL PRODUCTS". */
    public String headingText() {
        return getText(HEADING);
    }

    public void searchFor(String term) {
        type(SEARCH_INPUT, term);
        click(SEARCH_BUTTON);
    }

    public boolean hasSearchResultsHeading() {
        try {
            waits.waitForVisible(SEARCH_RESULTS_HEADING);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}