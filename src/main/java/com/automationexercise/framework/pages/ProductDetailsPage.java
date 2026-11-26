package com.automationexercise.framework.pages;

import com.automationexercise.framework.base.BasePage;
import com.automationexercise.framework.components.HeaderComponent;
import com.automationexercise.framework.components.SidebarComponent;
import com.automationexercise.framework.components.SubscriptionComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

/** A product detail page (/product_details/{id}). */
public class ProductDetailsPage extends BasePage {

    // All locators below were observed in the page reports
    private static final By QUANTITY = By.id("quantity");
    private static final By ADD_TO_CART = By.cssSelector("button.cart");
    private static final By ADDED_MODAL_TITLE = By.cssSelector(".modal-title");
    // The "View Cart" link inside the confirmation (the top navigation also links to /view_cart)
    private static final By VIEW_CART_IN_CONFIRMATION =
            By.xpath("//div[contains(@class,'modal-body')]//a[@href='/view_cart']");
    private static final By REVIEW_NAME = By.cssSelector("#review-form #name");
    private static final By REVIEW_EMAIL = By.cssSelector("#review-form #email");
    private static final By REVIEW_TEXT = By.id("review");
    private static final By REVIEW_SUBMIT = By.id("button-review");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public ProductDetailsPage open(int productId) {
        openPath("/product_details/" + productId);
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

    public void setQuantity(int quantity) {
        type(QUANTITY, String.valueOf(quantity));
    }

    public void addToCart() {
        click(ADD_TO_CART);
    }

    public boolean isQuantityAndAddToCartDisplayed() {
        return isDisplayed(QUANTITY) && isDisplayed(ADD_TO_CART);
    }

    /** Waits for the "Added!" confirmation that appears after Add to cart. */
    public boolean isAddedConfirmationVisible() {
        try {
            waits.waitForVisible(ADDED_MODAL_TITLE);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String addedConfirmationTitle() {
        return getText(ADDED_MODAL_TITLE);
    }

    /** Clicks "View Cart" in the confirmation and returns the cart page. */
    public CartPage openCartFromConfirmation() {
        click(VIEW_CART_IN_CONFIRMATION);
        waits.waitForUrlContains("/view_cart");
        waitForPageLoad();
        return new CartPage(driver);
    }

    public boolean isReviewFormDisplayed() {
        return isDisplayed(REVIEW_NAME) && isDisplayed(REVIEW_EMAIL)
                && isDisplayed(REVIEW_TEXT) && isDisplayed(REVIEW_SUBMIT);
    }
}