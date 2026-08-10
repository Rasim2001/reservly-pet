package com.reservly.paymentservice.dto;

import com.reservly.paymentservice.domain.PaymentStatus;

import java.math.BigDecimal;

public record PaymentResponse(
        Long id,
        Long bookingId,
        PaymentStatus status,
        BigDecimal totalPrice
) {
}
