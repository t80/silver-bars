package com.creditsuisse.silverbars.infrastructure.api.in;

import com.creditsuisse.silverbars.domain.OrderType;
import com.creditsuisse.silverbars.domain.SilverBarOrder;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SilverBarOrderDto implements SilverBarOrder {

    private final String userId;
    private final String orderQuantityInKg;
    private final String pricePerKg;
    private final OrderType orderType;

    @JsonCreator
    public SilverBarOrderDto(
            @JsonProperty("userId") String userId,
            @JsonProperty("orderQuantityInKg") String orderQuantityInKg,
            @JsonProperty("pricePerKg") String pricePerKg,
            @JsonProperty("orderType") OrderType orderType) {

        this.userId = userId;
        this.orderQuantityInKg = orderQuantityInKg;
        this.pricePerKg = pricePerKg;
        this.orderType = orderType;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getOrderQuantityInKg() {
        return orderQuantityInKg;
    }

    @Override
    public String getPricePerKg() {
        return pricePerKg;
    }

    @Override
    public OrderType getOrderType() {
        return orderType;
    }
}
