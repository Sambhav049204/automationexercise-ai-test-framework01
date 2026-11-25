package com.automationexercise.tests.regression.products;

import static org.testng.Assert.assertTrue;

import com.automationexercise.framework.base.BaseTest;
import com.automationexercise.framework.pages.ProductDetailsPage;
import org.testng.annotations.Test;

public class ProductDetailsTest extends BaseTest {

    private static final int FIRST_PRODUCT_ID = 1; // /product_details/1 was observed on the site

    @Test(groups = "regression", description = "Detail page shows quantity, Add to cart and the review form")
    public void shouldShowQuantityAddToCartAndReviewForm() {
        ProductDetailsPage page = new ProductDetailsPage(getDriver()).open(FIRST_PRODUCT_ID);

        assertTrue(page.isQuantityAndAddToCartDisplayed(), "Quantity or Add to cart is not visible");
        assertTrue(page.isReviewFormDisplayed(), "Review form fields are not all visible");
    }

    @Test(groups = "regression", description = "ADD-008 and PDT-012: Add to cart shows the Added! confirmation")
    public void addingProductShouldShowConfirmation() {
        ProductDetailsPage page = new ProductDetailsPage(getDriver()).open(FIRST_PRODUCT_ID);

        page.setQuantity(4);
        page.addToCart();

        assertTrue(page.isAddedConfirmationVisible(), "The Added! confirmation did not appear");
        assertTrue("Added!".equalsIgnoreCase(page.addedConfirmationTitle()),
                "Unexpected confirmation title: " + page.addedConfirmationTitle());
    }
}