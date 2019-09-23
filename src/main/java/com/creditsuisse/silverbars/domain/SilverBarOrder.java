package com.creditsuisse.silverbars.domain;

public interface SilverBarOrder {

    String getUserId();
    int getOrderQuantityInGram();
    int getPricePerKgInPence();
    OrderType getOrderType();
}
