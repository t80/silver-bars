package com.creditsuisse.silverbars.integration;

import com.creditsuisse.silverbars.domain.OrderType;
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

import static java.util.UUID.*;
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
            "1.5",
            "100",
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
    public void deletesAnOrder() throws IOException {

        OrderResponseDto orderResponseDto = silverBarClient.registerOrder(silverBarOrderDto);

        silverBarClient.deleteOrder(orderResponseDto.getOrderId());

        assertThat(silverBarClient.lastHttpStatusCode()).isEqualTo("204");
    }

    @Test
    public void getsLiveOrderSummary() throws IOException {

        OrderSummaryDto liveOrderSummary = silverBarClient.getLiveOrderSummary();

        assertThat(silverBarClient.lastHttpStatusCode()).isEqualTo("200");
    }
}