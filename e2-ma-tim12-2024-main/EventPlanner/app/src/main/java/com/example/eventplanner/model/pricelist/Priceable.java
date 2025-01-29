package com.example.eventplanner.model.pricelist;

public interface Priceable {
    String getName();
    double getPrice();
    double getDiscount();
    double getPriceWithDiscount();
}
