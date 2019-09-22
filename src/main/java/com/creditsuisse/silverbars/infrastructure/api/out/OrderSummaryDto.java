package com.creditsuisse.silverbars.infrastructure.api.out;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.SortedMap;

public class OrderSummaryDto {

    private final SortedMap<Integer, String> buyOrdersInPence;
    private final SortedMap<Integer, String> sellOrdersInPence;

    @JsonCreator
    public OrderSummaryDto(
            @JsonProperty("buyOrdersInPence") SortedMap<Integer, String> buyOrdersInPence,
            @JsonProperty("sellOrdersInPence") SortedMap<Integer, String> sellOrdersInPence
    ) {
        this.buyOrdersInPence = buyOrdersInPence;
        this.sellOrdersInPence = sellOrdersInPence;
    }

    public SortedMap<Integer, String> getBuyOrdersInPence() {
        return buyOrdersInPence;
    }

    public SortedMap<Integer, String> getSellOrdersInPence() {
        return sellOrdersInPence;
    }
}
