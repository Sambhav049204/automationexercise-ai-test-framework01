package com.automationexercise.framework.components;

import com.automationexercise.framework.base.BasePage;
import com.automationexercise.framework.utils.XPaths;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

/** The left sidebar with the Category accordion and the Brands list. */
public class SidebarComponent extends BasePage {

    private static final By CATEGORY_HEADING = XPaths.tagWithText("h2", "Category");
    private static final By BRANDS_HEADING = XPaths.tagWithText("h2", "Brands");

    public SidebarComponent(WebDriver driver) {
        super(driver);
    }

    public boolean isCategoryHeadingVisible() {
        return isDisplayed(CATEGORY_HEADING);
    }

    public boolean isBrandsHeadingVisible() {
        return isDisplayed(BRANDS_HEADING);
    }

    /** Clicking an open group collapses it again, so call this once per group. */
    public void expand(CategoryGroup group) {
        click(By.cssSelector("a[href='" + group.anchor() + "']"));
    }

    /** Waits briefly for the (animated) sub-category link to appear. */
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

    private static By brandLocator(String brand) {
        return By.cssSelector("a[href='/brand_products/" + brand + "']");
    }
}