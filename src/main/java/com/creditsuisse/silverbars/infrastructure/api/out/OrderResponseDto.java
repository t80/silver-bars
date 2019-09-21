package com.creditsuisse.silverbars.infrastructure.api.out;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class OrderResponseDto {

    private final String orderId;

    @JsonCreator
    public OrderResponseDto(@JsonProperty("orderId") UUID orderId) {
        this.orderId = orderId.toString();
    }

    public String getOrderId() {
        return orderId;
    }
}
