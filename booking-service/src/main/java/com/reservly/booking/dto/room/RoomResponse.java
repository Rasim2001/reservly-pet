package com.reservly.booking.dto.room;

import com.reservly.booking.domain.room.RoomStatus;
import com.reservly.booking.domain.room.RoomType;

import java.time.Instant;


public record RoomResponse(
        Long id,
        String name,
        RoomType type,
        Integer capacity,
        RoomStatus status,
        Instant createdAt
) {}
