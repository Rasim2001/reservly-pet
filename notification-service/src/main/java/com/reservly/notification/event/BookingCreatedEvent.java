package com.reservly.notification.event;

import lombok.Builder;

import java.time.Instant;

@Builder
public record BookingCreatedEvent(
        Long bookingId,
        Long userId,
        Instant startTime,
        Instant endTime
){
}
