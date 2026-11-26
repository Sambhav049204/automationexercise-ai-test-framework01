package com.automationexercise.framework.model;

import java.util.UUID;

/** Dummy account data for registration. Nothing here is real personal data. */
public record AccountDetails(String password, String firstName, String lastName, String company,
                             String address1, String address2, String state, String city,
                             String zipcode, String mobileNumber) {

    public static AccountDetails sample() {
        return new AccountDetails(
                "Pw1-" + UUID.randomUUID().toString().substring(0, 8),
                "Test", "User", "Example Company",
                "1 Example Street", "Block A", "Example State", "Example City",
                "123456", "9000000000");
    }
}