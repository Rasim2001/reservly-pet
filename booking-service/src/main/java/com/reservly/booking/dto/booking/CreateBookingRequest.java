package com.reservly.booking.dto.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateBookingRequest (
        @NotNull Long roomId,
        @NotNull @Future Instant startTime,
        @NotNull @Future Instant endTime
){
}
