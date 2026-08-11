package com.reservly.booking.dto.booking;

import com.reservly.booking.domain.booking.BookingStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record BookingResponse(
        Long id,
        Long roomId,
        Long userId,
        BookingStatus status,
        BigDecimal totalPrice,
        Instant startTime,
        Instant endTime,
        Instant createdAt
) {
}
