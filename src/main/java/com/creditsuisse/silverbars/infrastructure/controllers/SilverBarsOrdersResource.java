package com.creditsuisse.silverbars.infrastructure.controllers;

import com.creditsuisse.silverbars.domain.LiveOrderSummary;
import com.creditsuisse.silverbars.domain.SilverBarOrderService;
import com.creditsuisse.silverbars.infrastructure.api.in.SilverBarOrderDto;
import com.creditsuisse.silverbars.infrastructure.api.out.OrderResponseDto;
import com.creditsuisse.silverbars.infrastructure.api.out.OrderSummaryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.TreeMap;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/silverbars/orders")
public class SilverBarsOrdersResource {

    private final SilverBarOrderService silverBarOrderService;

    public SilverBarsOrdersResource(SilverBarOrderService silverBarOrderService) {
        this.silverBarOrderService = silverBarOrderService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HttpEntity<OrderResponseDto> registerOrder(
            @RequestBody @Valid SilverBarOrderDto silverBarOrderDto) {

        UUID orderId = silverBarOrderService.register(silverBarOrderDto);

        return ResponseEntity
                .status(201)
                .body(new OrderResponseDto(orderId));
    }

    @DeleteMapping("{orderId}")
    public HttpEntity<Void> deleteOrder (@PathVariable("orderId") String orderId) {

        silverBarOrderService.delete(UUID.fromString(orderId));

        return ResponseEntity
                .status(204)
                .build();
    }

    @GetMapping(value = "summary", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HttpEntity<OrderSummaryDto> getLiveOrderSummary() {

        LiveOrderSummary liveOrderSummary = silverBarOrderService.liveOrderSummary();

        return ResponseEntity.ok(
                new OrderSummaryDto(
                        liveOrderSummary.getSellOrders(),
                        liveOrderSummary.getBuyOrders()));
    }
}
