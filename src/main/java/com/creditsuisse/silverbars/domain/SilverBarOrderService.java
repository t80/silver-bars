package com.creditsuisse.silverbars.domain;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SilverBarOrderService {

    private final SilverBarOrderRepository silverBarOrderRepository;

    public SilverBarOrderService(SilverBarOrderRepository silverBarOrderRepository) {
        this.silverBarOrderRepository = silverBarOrderRepository;
    }

    public UUID register(SilverBarOrder silverBarOrder) {
        // TODO: validate?
        return silverBarOrderRepository.add(silverBarOrder);
    }

    public void delete(UUID orderId) {
        silverBarOrderRepository.delete(orderId);
    }
}
