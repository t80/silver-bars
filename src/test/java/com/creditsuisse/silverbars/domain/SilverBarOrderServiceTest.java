package com.creditsuisse.silverbars.domain;

import com.creditsuisse.silverbars.domain.repository.SilverBarOrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.UUID;

import static com.creditsuisse.silverbars.Fixtures.*;
import static com.creditsuisse.silverbars.domain.OrderType.BUY;
import static com.creditsuisse.silverbars.domain.OrderType.SELL;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SilverBarOrderServiceTest {

    @Mock
    private SilverBarOrderRepository orderRepository;
    @Mock
    private SilverBarOrderValidator silverBarOrderValidator;

    private SilverBarOrder someSilverBarOrder = someSilverBarOrderWithDefaults().build();

    private final UUID someOrderId = UUID.randomUUID();

    private SilverBarOrderService silverBarOrderService;

    @Before
    public void setUp() {
        silverBarOrderService = new SilverBarOrderService(
                orderRepository,
                silverBarOrderValidator);
    }

    @Test
    public void registersOrder() {

        when(orderRepository.add(someSilverBarOrder)).thenReturn(someOrderId);

        UUID orderId = silverBarOrderService.register(someSilverBarOrder);

        verify(orderRepository, times(1)).add(someSilverBarOrder);

        assertThat(orderId).isEqualTo(someOrderId);
    }

    @Test
    public void deletesOrder() {

        silverBarOrderService.delete(someOrderId);

        verify(orderRepository, times(1)).delete(someOrderId);
    }

    @Test
    public void sortsSellOrdersAscending() {

        when(orderRepository.fetchAll()).thenReturn(someListOfSilverBarOrder());

        LiveOrderSummary liveOrderSummary = silverBarOrderService.liveOrderSummary();

        assertThat(liveOrderSummary.getSellOrders().keySet())
                .containsExactly(200, 300, 500, 600);
    }

    @Test
    public void sortsBuyOrdersDescending() {

        when(orderRepository.fetchAll()).thenReturn(someListOfSilverBarOrder());

        LiveOrderSummary liveOrderSummary = silverBarOrderService.liveOrderSummary();

        assertThat(liveOrderSummary.getBuyOrders().keySet())
                .containsExactly(400, 300, 200, 100);
    }

    @Test
    public void handlesEmptyRepository() {

        when(orderRepository.fetchAll()).thenReturn(Collections.emptyList());

        LiveOrderSummary liveOrderSummary = silverBarOrderService.liveOrderSummary();

        assertThat(liveOrderSummary.getBuyOrders()).isEmpty();
        assertThat(liveOrderSummary.getSellOrders()).isEmpty();
    }

    @Test
    public void handlesOnlyBuyOrder() {

        when(orderRepository.fetchAll()).thenReturn(buyOrders());

        LiveOrderSummary liveOrderSummary = silverBarOrderService.liveOrderSummary();

        assertThat(liveOrderSummary.getBuyOrders()).isNotNull();
        assertThat(liveOrderSummary.getSellOrders()).isEmpty();
    }

    @Test
    public void handlesOnlySellOrder() {

        when(orderRepository.fetchAll()).thenReturn(sellOrders());

        LiveOrderSummary liveOrderSummary = silverBarOrderService.liveOrderSummary();

        assertThat(liveOrderSummary.getBuyOrders()).isEmpty();
        assertThat(liveOrderSummary.getSellOrders()).isNotNull();
    }

    @Test
    public void mergesOrdersForTheSamePriceAndType() {

        when(orderRepository.fetchAll()).thenReturn(
                asList(
                        someSilverBarOrderWithDefaults().orderQuantityInGram(2500).pricePerKgInPence(300).orderType(SELL).build(),
                        someSilverBarOrderWithDefaults().orderQuantityInGram(6500).pricePerKgInPence(300).orderType(SELL).build(),
                        someSilverBarOrderWithDefaults().orderQuantityInGram(1500).pricePerKgInPence(300).orderType(BUY).build(),
                        someSilverBarOrderWithDefaults().orderQuantityInGram(5500).pricePerKgInPence(300).orderType(BUY).build()));

        LiveOrderSummary liveOrderSummary = silverBarOrderService.liveOrderSummary();

        assertThat(liveOrderSummary.getSellOrders().size()).isEqualTo(1);
        assertThat(liveOrderSummary.getSellOrders().get(300)).isEqualTo(9000);

        assertThat(liveOrderSummary.getBuyOrders().size()).isEqualTo(1);
        assertThat(liveOrderSummary.getBuyOrders().get(300)).isEqualTo(7000);
    }

    @Test
    public void handlesFailedValidation() {

        doThrow(InvalidParameterException.class)
                .when(silverBarOrderValidator).validate(someSilverBarOrder);

        assertThatThrownBy(() -> silverBarOrderService.register(someSilverBarOrder));

        verifyZeroInteractions(orderRepository);
    }

}