package com.example.coffeestore.order.domain;

import org.springframework.util.Assert;

public class Email {

    private final String address;

    public Email(String address) {
        Assert.notNull(address, "address should not be null");
        Assert.isTrue(address.length() >= 4 && address.length() <= 50,
            "address length must be between 4 and 50 characters");
        Assert.isTrue(check);
    }
}
