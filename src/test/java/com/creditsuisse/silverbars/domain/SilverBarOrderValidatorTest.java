package com.creditsuisse.silverbars.domain;

import org.junit.Test;

import java.security.InvalidParameterException;

import static com.creditsuisse.silverbars.Fixtures.someSilverBarOrderWithDefaults;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SilverBarOrderValidatorTest {

    private final SilverBarOrderValidator silverBarOrderValidator = new SilverBarOrderValidator();

    @Test
    public void throwsOnInvalidPrice() {

        SilverBarOrder orderWithInvalidPrice = someSilverBarOrderWithDefaults()
                .pricePerKgInPence(0)
                .build();

        assertThatThrownBy(() -> silverBarOrderValidator.validate(orderWithInvalidPrice))
                .isExactlyInstanceOf(InvalidParameterException.class)
                .hasMessage("Price must be greater than 0");
    }

    @Test
    public void throwsOnInvalidQuantity() {

        SilverBarOrder orderWithInvalidQuantity = someSilverBarOrderWithDefaults()
                .orderQuantityInGram(0)
                .build();

        assertThatThrownBy(() -> silverBarOrderValidator.validate(orderWithInvalidQuantity))
                .isExactlyInstanceOf(InvalidParameterException.class)
                .hasMessage("Quantity must be greater than 0");
    }

    @Test
    public void throwsOnMultipleInvalidQuantity() {

        SilverBarOrder orderWithInvalidQuantityAndPrice = someSilverBarOrderWithDefaults()
                .orderQuantityInGram(0)
                .pricePerKgInPence(0)
                .build();

        assertThatThrownBy(() -> silverBarOrderValidator.validate(orderWithInvalidQuantityAndPrice))
                .isExactlyInstanceOf(InvalidParameterException.class)
                .hasMessageContaining("Quantity must be greater than 0")
                .hasMessageContaining("Price must be greater than 0");
    }
}