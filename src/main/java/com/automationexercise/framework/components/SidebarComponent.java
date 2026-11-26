package com.automationexercise.framework.components;

import com.automationexercise.framework.base.BasePage;
import com.automationexercise.framework.utils.WaitUtils;
import com.automationexercise.framework.utils.XPaths;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

/** The left sidebar with the Category accordion and the Brands list. */
public class SidebarComponent extends BasePage {

    private static final By CATEGORY_HEADING = XPaths.tagWithText("h2", "Category");
    private static final By BRANDS_HEADING = XPaths.tagWithText("h2", "Brands");
    private static final int MAX_EXPAND_ATTEMPTS = 2;

    public SidebarComponent(WebDriver driver) {
        super(driver);
    }

    public boolean isCategoryHeadingVisible() {
        return isDisplayed(CATEGORY_HEADING);
    }

    public boolean isBrandsHeadingVisible() {
        return isDisplayed(BRANDS_HEADING);
    }

    /**
     * Opens a category group and confirms that its sub-categories became visible. If the first click
     * does not open it (for example the page script was not ready yet), it clicks once more.
     * Do not call this for a group that is already open: clicking an open group closes it.
     */
    public void expand(CategoryGroup group) {
        By anchor = By.cssSelector("a[href='" + group.anchor() + "']");
        By firstSubCategory = firstSubCategoryOf(group);
        WaitUtils shortWait = new WaitUtils(driver, Duration.ofSeconds(5));

        for (int attempt = 1; attempt <= MAX_EXPAND_ATTEMPTS; attempt++) {
            click(anchor);
            try {
                shortWait.waitForVisible(firstSubCategory);
                return;
            } catch (TimeoutException e) {
                log.warn("Category group {} did not open after click {} of {}", group, attempt, MAX_EXPAND_ATTEMPTS);
            }
        }
        throw new IllegalStateException("Category group " + group + " did not open after "
                + MAX_EXPAND_ATTEMPTS + " clicks on " + driver.getCurrentUrl());
    }

    /** Waits for a sub-category link to be visible. */
    public boolean waitUntilVisible(Category category) {
        try {
            waits.waitForVisible(category.locator());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void openCategory(Category category) {
        expand(category.group());
        waits.waitForVisible(category.locator());
        click(category.locator());
        waits.waitForUrlContains(category.path());
        waitForPageLoad();
    }

    public boolean isBrandVisible(String brand) {
        return isDisplayed(brandLocator(brand));
    }

    public void openBrand(String brand) {
        click(brandLocator(brand));
        waits.waitForUrlContains("/brand_products/");
        waitForPageLoad();
    }

    private static By firstSubCategoryOf(CategoryGroup group) {
        for (Category category : Category.values()) {
            if (category.group() == group) {
                return category.locator();
            }
        }
        throw new IllegalStateException("No sub-categories defined for " + group);
    }

    private static By brandLocator(String brand) {
        return By.cssSelector("a[href='/brand_products/" + brand + "']");
    }
}