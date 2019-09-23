package com.creditsuisse.silverbars.domain;

import java.util.SortedMap;

public interface LiveOrderSummary {

    // Maps the price->aggregatedQuantity
    SortedMap<Integer, Integer> getBuyOrders();
    SortedMap<Integer, Integer> getSellOrders();
}
