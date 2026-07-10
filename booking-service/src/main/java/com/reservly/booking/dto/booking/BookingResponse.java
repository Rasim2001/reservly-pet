package com.reservly.booking.dto.booking;

import com.reservly.booking.domain.booking.BookingStatus;

import java.time.Instant;

public record BookingResponse(
        Long id,
        Long roomId,
        Long userId,
        BookingStatus status,
        Instant startTime,
        Instant endTime,
        Instant createdAt
) {
}
