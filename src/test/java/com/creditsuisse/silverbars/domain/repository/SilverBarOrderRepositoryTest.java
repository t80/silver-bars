package com.creditsuisse.silverbars.domain.repository;

import com.creditsuisse.silverbars.domain.SilverBarOrder;
import org.junit.Test;

import java.util.UUID;

import static com.creditsuisse.silverbars.Fixtures.someSilverBarOrderWithDefaults;
import static org.assertj.core.api.Assertions.assertThat;

public class SilverBarOrderRepositoryTest {

    private final SilverBarOrderRepository silverBarOrderRepository = new SilverBarOrderRepository();

    @Test
    public void storesOrder() {

        SilverBarOrder order = someSilverBarOrderWithDefaults().build();

        UUID newOrderId = silverBarOrderRepository.add(order);

        assertThat(newOrderId).isInstanceOf(UUID.class);
        assertThat(silverBarOrderRepository.fetchAll()).containsExactly(order);
    }

    @Test
    public void deletesOrder() {

        SilverBarOrder order = someSilverBarOrderWithDefaults().build();

        UUID newOrderId = silverBarOrderRepository.add(order);
        silverBarOrderRepository.delete(newOrderId);

        assertThat(silverBarOrderRepository.fetchAll()).isEmpty();
    }

    @Test
    public void fetchesAllOrders() {

        SilverBarOrder order1 = someSilverBarOrderWithDefaults().build();
        SilverBarOrder order2 = someSilverBarOrderWithDefaults().build();
        SilverBarOrder order3 = someSilverBarOrderWithDefaults().build();

        silverBarOrderRepository.add(order1);
        silverBarOrderRepository.add(order2);
        silverBarOrderRepository.add(order3);

        assertThat(silverBarOrderRepository.fetchAll())
                .containsExactlyInAnyOrder(order1, order2, order3);
    }
}