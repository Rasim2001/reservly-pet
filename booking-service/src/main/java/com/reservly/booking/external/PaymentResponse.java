package com.reservly.booking.external;

import java.math.BigDecimal;

public record PaymentResponse(
        Long id,
        Long bookingId,
        PaymentStatus status,
        BigDecimal totalPrice
) {
}
