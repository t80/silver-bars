package com.creditsuisse.silverbars.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SilverBarOrderServiceTest {

    @Mock
    private SilverBarOrderRepository orderRepository;
    @Mock
    private SilverBarOrder someSilverBarOrder;

    private final UUID someOrderId = UUID.randomUUID();

    private SilverBarOrderService silverBarOrderService;

    @Before
    public void setUp() {
        silverBarOrderService = new SilverBarOrderService(orderRepository);
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
}