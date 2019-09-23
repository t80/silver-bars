package com.creditsuisse.silverbars.infrastructure.api.out;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.SortedMap;

import static java.util.stream.Collectors.toList;

public class OrderSummaryDto {

    @JsonProperty
    private final List<AggregatedOrder> sellOrders;
    @JsonProperty
    private final List<AggregatedOrder> buyOrders;

    @JsonCreator
    public OrderSummaryDto(
            @JsonProperty("sellOrders") List<AggregatedOrder> sellOrders,
            @JsonProperty("buyOrders") List<AggregatedOrder> buyOrders) {

        this.sellOrders = sellOrders;
        this.buyOrders = buyOrders;
    }

    public OrderSummaryDto(
            SortedMap<Integer, Integer> sellOrdersInPence,
            SortedMap<Integer, Integer> buyOrdersInPence) {

        this.sellOrders = sellOrdersInPence.entrySet()
                .stream()
                .map(es -> new AggregatedOrder(es.getKey(), es.getValue()))
                .collect(toList());

        this.buyOrders = buyOrdersInPence.entrySet()
                .stream()
                .map(es -> new AggregatedOrder(es.getKey(), es.getValue()))
                .collect(toList());
    }

    public List<AggregatedOrder> getSellOrders() {
        return sellOrders;
    }

    public List<AggregatedOrder> getBuyOrders() {
        return buyOrders;
    }
}
