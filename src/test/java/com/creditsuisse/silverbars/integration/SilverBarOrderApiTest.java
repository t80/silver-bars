package com.creditsuisse.silverbars.integration;

import com.creditsuisse.silverbars.domain.OrderType;
import com.creditsuisse.silverbars.domain.SilverBarOrder;
import com.creditsuisse.silverbars.infrastructure.api.in.SilverBarOrderDto;
import com.creditsuisse.silverbars.infrastructure.api.out.OrderResponseDto;
import com.creditsuisse.silverbars.infrastructure.api.out.OrderSummaryDto;
import com.creditsuisse.silverbars.integration.utils.SilverBarClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.UUID;

import static com.creditsuisse.silverbars.Fixtures.someSilverBarOrderWithDefaults;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class SilverBarOrderApiTest {

    @LocalServerPort
    private String port;

    private SilverBarClient silverBarClient;

    private SilverBarOrderDto silverBarOrderDto = new SilverBarOrderDto(
            randomUUID().toString(),
            1500,
            100,
            OrderType.BUY
    );

    @Before
    public void setUp() {
        silverBarClient = new SilverBarClient(port);
    }

    @Test
    public void registersOrder() throws IOException {

        OrderResponseDto orderResponseDto = silverBarClient.registerOrder(silverBarOrderDto);

        assertThat(silverBarClient.lastHttpStatusCode()).isEqualTo("201");
        assertThat(fromString(orderResponseDto.getOrderId()))
                .isInstanceOf(UUID.class);
    }

    @Test
    public void validatesPrice() {

        SilverBarOrder silverBarOrderWithInvalidPrice =
                someSilverBarOrderWithDefaults()
                        .pricePerKgInPence(0).build();

        silverBarClient.registerOrder(silverBarOrderWithInvalidPrice);

        assertThat(silverBarClient.lastHttpStatusCode()).isEqualTo("400");
    }

    @Test
    public void validatesQuantity() {

        SilverBarOrder silverBarOrderWithInvalidQuantity =
                someSilverBarOrderWithDefaults()
                        .orderQuantityInGram(0).build();

        silverBarClient.registerOrder(silverBarOrderWithInvalidQuantity);

        assertThat(silverBarClient.lastHttpStatusCode()).isEqualTo("400");
    }

    @Test
    public void deletesAnOrder() {

        OrderResponseDto orderResponseDto = silverBarClient.registerOrder(silverBarOrderDto);

        silverBarClient.deleteOrder(orderResponseDto.getOrderId());

        assertThat(silverBarClient.lastHttpStatusCode()).isEqualTo("204");
    }

    @Test
    public void getsLiveSellOrderSummary() {

        SilverBarOrder order1 = someSilverBarOrderWithDefaults()
                .userId("user1")
                .orderQuantityInGram(3500)
                .pricePerKgInPence(30600)
                .orderType(OrderType.SELL)
                .build();

        SilverBarOrder order2 = someSilverBarOrderWithDefaults()
                .userId("user2")
                .orderQuantityInGram(1200)
                .pricePerKgInPence(31000)
                .orderType(OrderType.SELL)
                .build();

        SilverBarOrder order3 = someSilverBarOrderWithDefaults()
                .userId("user3")
                .orderQuantityInGram(1500)
                .pricePerKgInPence(30700)
                .orderType(OrderType.SELL)
                .build();

        SilverBarOrder order4 = someSilverBarOrderWithDefaults()
                .userId("user4")
                .orderQuantityInGram(2000)
                .pricePerKgInPence(30600)
                .orderType(OrderType.SELL)
                .build();


        silverBarClient.registerOrder(order1);
        silverBarClient.registerOrder(order2);
        silverBarClient.registerOrder(order3);
        silverBarClient.registerOrder(order4);

        OrderSummaryDto liveOrderSummary = silverBarClient.getLiveOrderSummary();

        assertThat(silverBarClient.lastHttpStatusCode()).isEqualTo("200");
        assertThat(liveOrderSummary.getSellOrders().size()).isEqualTo(3);
        assertThat(liveOrderSummary.getBuyOrders().size()).isEqualTo(0);

        assertThat(liveOrderSummary.getSellOrders().get(0))
                .extracting("priceInPounds", "quantityInKg")
                .containsExactly("306.0", "5.5");
        assertThat(liveOrderSummary.getSellOrders().get(1))
                .extracting("priceInPounds", "quantityInKg")
                .containsExactly("307.0", "1.5");
        assertThat(liveOrderSummary.getSellOrders().get(2))
                .extracting("priceInPounds", "quantityInKg")
                .containsExactly("310.0", "1.2");


    }

    @Test
    public void getsLiveBuyOrderSummary() {

        SilverBarOrder order1 = someSilverBarOrderWithDefaults()
                .userId("user1")
                .orderQuantityInGram(3500)
                .pricePerKgInPence(30600)
                .orderType(OrderType.BUY)
                .build();

        SilverBarOrder order2 = someSilverBarOrderWithDefaults()
                .userId("user2")
                .orderQuantityInGram(1200)
                .pricePerKgInPence(31000)
                .orderType(OrderType.BUY)
                .build();

        SilverBarOrder order3 = someSilverBarOrderWithDefaults()
                .userId("user3")
                .orderQuantityInGram(1500)
                .pricePerKgInPence(30700)
                .orderType(OrderType.BUY)
                .build();

        SilverBarOrder order4 = someSilverBarOrderWithDefaults()
                .userId("user4")
                .orderQuantityInGram(2000)
                .pricePerKgInPence(30600)
                .orderType(OrderType.BUY)
                .build();


        silverBarClient.registerOrder(order1);
        silverBarClient.registerOrder(order2);
        silverBarClient.registerOrder(order3);
        silverBarClient.registerOrder(order4);

        OrderSummaryDto liveOrderSummary = silverBarClient.getLiveOrderSummary();

        assertThat(silverBarClient.lastHttpStatusCode()).isEqualTo("200");
        assertThat(liveOrderSummary.getBuyOrders().size()).isEqualTo(3);

        assertThat(liveOrderSummary.getBuyOrders().get(0))
                .extracting("priceInPounds", "quantityInKg")
                .containsExactly("310.0", "1.2");
        assertThat(liveOrderSummary.getBuyOrders().get(1))
                .extracting("priceInPounds", "quantityInKg")
                .containsExactly("307.0", "1.5");
        assertThat(liveOrderSummary.getBuyOrders().get(2))
                .extracting("priceInPounds", "quantityInKg")
                .containsExactly("306.0", "5.5");
    }
}