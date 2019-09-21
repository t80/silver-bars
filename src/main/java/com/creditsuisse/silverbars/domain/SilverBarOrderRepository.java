package com.creditsuisse.silverbars.domain;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class SilverBarOrderRepository {

    private final ConcurrentMap<UUID, SilverBarOrder> orders = new ConcurrentHashMap<>();

    public UUID add(SilverBarOrder silverBarOrder) {

        UUID orderId = UUID.randomUUID();
        orders.put(orderId, silverBarOrder);

        return orderId;
    }
}
