package com.reservly.booking.event;

import lombok.Builder;

import java.time.Instant;

@Builder
public record  BookingCreatedEvent (
        Long bookingId,
        Long roomId,
        Long userId,
        Instant startTime,
        Instant endTime,
        Instant createdAt
){
}
