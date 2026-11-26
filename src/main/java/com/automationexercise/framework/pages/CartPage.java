package com.automationexercise.framework.pages;

import com.automationexercise.framework.base.BasePage;
import com.automationexercise.framework.components.HeaderComponent;
import com.automationexercise.framework.components.SubscriptionComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** The shopping cart (/view_cart). Locators come from the cart page report. */
public class CartPage extends BasePage {

    private static final By ROWS = By.cssSelector("#cart_info_table tr[id^='product-']");
    private static final By EMPTY_CART_MESSAGE = By.id("empty_cart");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public CartPage open() {
        openPath("/view_cart");
        waitForPageLoad();
        return this;
    }

    public HeaderComponent header() {
        return new HeaderComponent(driver);
    }

    public SubscriptionComponent subscription() {
        return new SubscriptionComponent(driver);
    }

    public boolean isEmptyMessageVisible() {
        return isDisplayed(EMPTY_CART_MESSAGE);
    }

    public int itemCount() {
        return findAll(ROWS).size();
    }

    public boolean hasProduct(int productId) {
        return isDisplayed(row(productId));
    }

    public String nameOf(int productId) {
        return getText(By.cssSelector("#product-" + productId + " td.cart_description h4 a"));
    }

    public int priceOf(int productId) {
        return number(getText(By.cssSelector("#product-" + productId + " td.cart_price p")));
    }

    public int quantityOf(int productId) {
        return number(getText(By.cssSelector("#product-" + productId + " td.cart_quantity button")));
    }

    public int totalOf(int productId) {
        return number(getText(By.cssSelector("#product-" + productId + " td.cart_total p.cart_total_price")));
    }

    private static By row(int productId) {
        return By.cssSelector("#product-" + productId);
    }

    /** Turns "Rs. 500" into 500. */
    private static int number(String text) {
        String digits = text.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) {
            throw new IllegalStateException("No number found in '" + text + "'");
        }
        return Integer.parseInt(digits);
    }
}