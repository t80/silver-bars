package com.creditsuisse.silverbars.domain;

import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;

@Service
class SilverBarOrderValidator {

    void validate(SilverBarOrder silverBarOrder) {

        Collection<String> invalidItems = new ArrayList<>();

        if (silverBarOrder.getPricePerKgInPence() < 1) {
            invalidItems.add("Price must be greater than 0");
        }

        if (silverBarOrder.getOrderQuantityInGram() < 1) {
            invalidItems.add("Quantity must be greater than 0");
        }

        if (!invalidItems.isEmpty()) {
            throw new InvalidParameterException(
                    String.join(", ", invalidItems));
        }
    }
}
