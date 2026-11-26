package com.automationexercise.tests.regression.cart;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import com.automationexercise.framework.base.BaseTest;
import com.automationexercise.framework.pages.CartPage;
import com.automationexercise.framework.pages.ProductDetailsPage;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

    private static final int FIRST_PRODUCT_ID = 1; // "Blue Top" in the cart report

    private CartPage addFirstProductToCart(int quantity) {
        ProductDetailsPage details = new ProductDetailsPage(getDriver()).open(FIRST_PRODUCT_ID);
        details.setQuantity(quantity);
        details.addToCart();
        assertTrue(details.isAddedConfirmationVisible(), "The Added! confirmation did not appear");
        return details.openCartFromConfirmation();
    }

    @Test(groups = "regression", description = "PDT-005 and ADD-005: quantity 4 is reflected in the cart (TC13)")
    public void productAddedWithQuantityFourShouldShowQuantityFourInCart() {
        CartPage cart = addFirstProductToCart(4);

        assertTrue(cart.hasProduct(FIRST_PRODUCT_ID), "The product is not in the cart");
        assertEquals(cart.nameOf(FIRST_PRODUCT_ID), "Blue Top");
        assertEquals(cart.quantityOf(FIRST_PRODUCT_ID), 4);
    }

    @Test(groups = "regression", description = "ADD-004: the line total equals price times quantity")
    public void cartLineTotalShouldEqualPriceTimesQuantity() {
        CartPage cart = addFirstProductToCart(3);

        int price = cart.priceOf(FIRST_PRODUCT_ID);
        int quantity = cart.quantityOf(FIRST_PRODUCT_ID);

        assertEquals(quantity, 3);
        assertEquals(cart.totalOf(FIRST_PRODUCT_ID), price * quantity);
    }

    @Test(groups = "regression", description = "CRT-002: a new session shows an empty cart")
    public void newSessionShouldShowEmptyCart() {
        CartPage cart = new CartPage(getDriver()).open();

        assertEquals(cart.itemCount(), 0, "The cart should have no items");
        assertTrue(cart.isEmptyMessageVisible(), "The empty-cart message is not visible");
    }
}