package com.reservly.paymentservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PaymentRequest(
        @NotNull Long bookingId,
        @NotNull @Positive BigDecimal totalPrice
) {

}
