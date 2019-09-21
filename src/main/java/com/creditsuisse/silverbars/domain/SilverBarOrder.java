package com.creditsuisse.silverbars.domain;

public interface SilverBarOrder {

    String getUserId();
    String getOrderQuantityInKg();
    String getPricePerKg();
    OrderType getOrderType();
}
