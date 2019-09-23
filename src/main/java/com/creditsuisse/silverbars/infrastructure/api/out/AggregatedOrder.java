package com.creditsuisse.silverbars.infrastructure.api.out;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

class AggregatedOrder {

    @JsonProperty
    private final String priceInPounds;
    @JsonProperty
    private final String quantityInKg;

    @JsonCreator
    public AggregatedOrder(
            @JsonProperty("priceInPounds") String priceInPounds,
            @JsonProperty("quantityInKg") String quantityInKg) {

        this.priceInPounds = priceInPounds;
        this.quantityInKg = quantityInKg;
    }

    public AggregatedOrder(Integer priceInPence, Integer quantityInGrams) {
        priceInPounds =  String.valueOf(priceInPence / 100.0);
        quantityInKg = String.valueOf(quantityInGrams / 1000.0);
    }
}
