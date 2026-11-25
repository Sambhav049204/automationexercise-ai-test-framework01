package com.automationexercise.framework.components;

import org.openqa.selenium.By;

/** Sub-categories with the link targets observed on the site (ids are not in sequence). */
public enum Category {
    WOMEN_DRESS(CategoryGroup.WOMEN, 1),
    WOMEN_TOPS(CategoryGroup.WOMEN, 2),
    WOMEN_SAREE(CategoryGroup.WOMEN, 7),
    MEN_TSHIRTS(CategoryGroup.MEN, 3),
    MEN_JEANS(CategoryGroup.MEN, 6),
    KIDS_DRESS(CategoryGroup.KIDS, 4),
    KIDS_TOPS_AND_SHIRTS(CategoryGroup.KIDS, 5);

    private final CategoryGroup group;
    private final int id;

    Category(CategoryGroup group, int id) {
        this.group = group;
        this.id = id;
    }

    public CategoryGroup group() {
        return group;
    }

    public String path() {
        return "/category_products/" + id;
    }

    public By locator() {
        return By.cssSelector("a[href='" + path() + "']");
    }
}