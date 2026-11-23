package com.automationexercise.framework.model;

/** A product as listed on automationexercise.com. Price is in Rs. as shown on the site. */
public record Product(int id, String name, int price) {
}