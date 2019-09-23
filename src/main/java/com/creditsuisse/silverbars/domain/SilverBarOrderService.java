package com.creditsuisse.silverbars.domain;

import com.creditsuisse.silverbars.domain.repository.SilverBarOrderRepository;
import org.springframework.stereotype.Service;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;

import static com.creditsuisse.silverbars.domain.OrderType.BUY;
import static com.creditsuisse.silverbars.domain.OrderType.SELL;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.reverseOrder;
import static java.util.Map.Entry;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Service
public class SilverBarOrderService {

    private final SilverBarOrderRepository silverBarOrderRepository;
    private final SilverBarOrderValidator silverBarOrderValidator;

    public SilverBarOrderService(
            SilverBarOrderRepository silverBarOrderRepository,
            SilverBarOrderValidator silverBarOrderValidator) {

        this.silverBarOrderRepository = silverBarOrderRepository;
        this.silverBarOrderValidator = silverBarOrderValidator;
    }

    public UUID register(SilverBarOrder silverBarOrder) {

        silverBarOrderValidator.validate(silverBarOrder);

        return silverBarOrderRepository.add(silverBarOrder);
    }

    public void delete(UUID orderId) {
        silverBarOrderRepository.delete(orderId);
    }

    public LiveOrderSummary liveOrderSummary() {

        Collection<SilverBarOrder> silverBarOrders = silverBarOrderRepository.fetchAll();

        if (silverBarOrders.isEmpty()) {
            return new LiveOrderSummaryImpl(
                    new TreeMap<>(),
                    new TreeMap<>()
            );
        }

        Map<OrderType, List<SilverBarOrder>> ordersByType =
                silverBarOrders
                        .stream()
                        .collect(groupingBy(SilverBarOrder::getOrderType));

        SortedMap<Integer, Integer> sellOrdersWithAggregatedQuantities =
                aggregateQuantitiesForSamePricedOrders(
                        groupOrdersByPrice(ordersByType.get(SELL)),
                        naturalOrder());

        SortedMap<Integer, Integer> buyOrdersWithAggregatedQuantities =
                aggregateQuantitiesForSamePricedOrders(
                        groupOrdersByPrice(ordersByType.get(BUY)),
                        reverseOrder());


        return new LiveOrderSummaryImpl(
                buyOrdersWithAggregatedQuantities,
                sellOrdersWithAggregatedQuantities
        );
    }

    private Map<Integer, List<SilverBarOrder>> groupOrdersByPrice(List<SilverBarOrder> orders) {

        if (orders == null) {
            return Collections.emptyMap();
        }

        return orders.stream()
                .collect(groupingBy(SilverBarOrder::getPricePerKgInPence));
    }

    private TreeMap<Integer, Integer> aggregateQuantitiesForSamePricedOrders(
            Map<Integer, List<SilverBarOrder>> ordersGroupedByPrice,
            Comparator<Integer> keyComparator) {

        return ordersGroupedByPrice.entrySet().stream()
                .map(this::aggregateByOrderQuantity)
                .collect(toMap(
                        Entry::getKey,
                        Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        () -> new TreeMap<>(keyComparator)));
    }

    private Map.Entry<Integer, Integer> aggregateByOrderQuantity(Map.Entry<Integer, List<SilverBarOrder>> silverBarOrdersForPrice) {

        Integer valueOfOrderAtPrice = silverBarOrdersForPrice.getValue().stream()
                .map(SilverBarOrder::getOrderQuantityInGram)
                .reduce((v1, v2) -> v1 + v2).orElse(0);

        return new SimpleEntry<>(
                silverBarOrdersForPrice.getKey(),
                valueOfOrderAtPrice);
    }
}
