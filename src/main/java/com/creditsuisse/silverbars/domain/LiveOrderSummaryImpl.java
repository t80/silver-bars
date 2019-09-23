package com.creditsuisse.silverbars.domain;

import java.util.SortedMap;

class LiveOrderSummaryImpl implements LiveOrderSummary {

    private final SortedMap<Integer, Integer> buyOrdersWithAggregatedQuantities;
    private final SortedMap<Integer, Integer> sellOrdersAggregatedQuantities;

    LiveOrderSummaryImpl(
            SortedMap<Integer, Integer> buyOrdersWithAggregatedQuantities,
            SortedMap<Integer, Integer> sellOrdersAggregatedQuantities) {

        this.buyOrdersWithAggregatedQuantities = buyOrdersWithAggregatedQuantities;
        this.sellOrdersAggregatedQuantities = sellOrdersAggregatedQuantities;
    }

    @Override
    public SortedMap<Integer, Integer> getBuyOrders() {
        return buyOrdersWithAggregatedQuantities;
    }

    @Override
    public SortedMap<Integer, Integer> getSellOrders() {
        return sellOrdersAggregatedQuantities;
    }
}
