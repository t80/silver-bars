package com.creditsuisse.silverbars.infrastructure.api.in;

import com.creditsuisse.silverbars.domain.OrderType;
import com.creditsuisse.silverbars.domain.SilverBarOrder;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SilverBarOrderDto implements SilverBarOrder {

    private final String userId;
    @Min(1)
    private final int orderQuantityInGram;
    @Min(1)
    private final int pricePerKgInPence;
    private final OrderType orderType;

    @JsonCreator
    public SilverBarOrderDto(
            @JsonProperty("userId") String userId,
            @JsonProperty("orderQuantityInGram") int orderQuantityInGram,
            @JsonProperty("pricePerKgInPence") int pricePerKgInPence,
            @JsonProperty("orderType") OrderType orderType) {

        this.userId = userId;
        this.orderQuantityInGram = orderQuantityInGram;
        this.pricePerKgInPence = pricePerKgInPence;
        this.orderType = orderType;
    }

    public static SilverBarOrderDtoBuilder builder() {
        return new SilverBarOrderDtoBuilder();
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public int getOrderQuantityInGram() {
        return orderQuantityInGram;
    }

    @Override
    public int getPricePerKgInPence() {
        return pricePerKgInPence;
    }

    @Override
    public OrderType getOrderType() {
        return orderType;
    }

    public static class SilverBarOrderDtoBuilder {
        private String userId;
        private int orderQuantityInGram;
        private int pricePerKgInPence;
        private OrderType orderType;

        public SilverBarOrderDtoBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public SilverBarOrderDtoBuilder orderQuantityInGram(int orderQuantityInGram) {
            this.orderQuantityInGram = orderQuantityInGram;
            return this;
        }

        public SilverBarOrderDtoBuilder pricePerKgInPence(int pricePerKgInPence) {
            this.pricePerKgInPence = pricePerKgInPence;
            return this;
        }

        public SilverBarOrderDtoBuilder orderType(OrderType orderType) {
            this.orderType = orderType;
            return this;
        }

        public SilverBarOrder build() {
            return new SilverBarOrderDto(userId, orderQuantityInGram, pricePerKgInPence, orderType);
        }

        public String toString() {
            return "SilverBarOrderDto.SilverBarOrderDtoBuilder(userId=" + this.userId + ", orderQuantityInGram=" + this.orderQuantityInGram + ", pricePerKg=" + this.pricePerKgInPence + ", orderType=" + this.orderType + ")";
        }
    }
}
