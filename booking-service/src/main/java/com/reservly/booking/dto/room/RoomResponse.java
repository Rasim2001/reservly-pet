package com.reservly.booking.dto.room;

import com.reservly.booking.domain.room.RoomStatus;
import com.reservly.booking.domain.room.RoomType;

import java.math.BigDecimal;
import java.time.Instant;


public record RoomResponse(
        Long id,
        String name,
        RoomType type,
        BigDecimal pricePerHour,
        Integer capacity,
        RoomStatus status,
        Instant createdAt
) {}
