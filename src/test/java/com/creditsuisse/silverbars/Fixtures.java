package com.creditsuisse.silverbars;

import com.creditsuisse.silverbars.domain.SilverBarOrder;
import com.creditsuisse.silverbars.infrastructure.api.in.SilverBarOrderDto;

import java.util.Collection;

import static com.creditsuisse.silverbars.domain.OrderType.BUY;
import static com.creditsuisse.silverbars.domain.OrderType.SELL;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.*;
import static java.util.stream.Stream.*;

public class Fixtures {

    public static SilverBarOrderDto.SilverBarOrderDtoBuilder someSilverBarOrderWithDefaults() {

        return SilverBarOrderDto.builder()
                .userId("1")
                .orderType(BUY)
                .orderQuantityInGram(1000)
                .pricePerKgInPence(1000);
    }

    public static Collection<SilverBarOrder> buyOrders() {

        return asList(
                new SilverBarOrderDto("1", 1500, 100, BUY),
                new SilverBarOrderDto("1", 1500, 300, BUY),
                new SilverBarOrderDto("1", 2500, 200, BUY),
                new SilverBarOrderDto("1", 3500, 300, BUY),
                new SilverBarOrderDto("1", 4500, 400, BUY)
        );
    }

    public static Collection<SilverBarOrder> sellOrders() {

        return asList(
                new SilverBarOrderDto("1", 1500, 600, SELL),
                new SilverBarOrderDto("1", 500, 200, SELL),
                new SilverBarOrderDto("1", 2500, 300, SELL),
                new SilverBarOrderDto("1", 4500, 300, SELL),
                new SilverBarOrderDto("1", 4500, 500, SELL)
        );
    }

    public static Collection<SilverBarOrder> someListOfSilverBarOrder() {

        return concat(
                sellOrders().stream(),
                buyOrders().stream()).collect(toList());
    }

}
