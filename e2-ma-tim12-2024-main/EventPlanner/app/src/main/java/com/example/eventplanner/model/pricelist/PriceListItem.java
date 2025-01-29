package com.example.eventplanner.model.pricelist;

public class PriceListItem {
    private int orderNumber;
    private Priceable item;

    public PriceListItem(int orderNumber, Priceable item) {
        this.orderNumber = orderNumber;
        this.item = item;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public Priceable getItem() {
        return item;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setItem(Priceable item) {
        this.item = item;
    }
}
