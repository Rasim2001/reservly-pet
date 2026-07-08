package com.reservly.booking.dto;

import com.reservly.booking.domain.RoomStatus;
import com.reservly.booking.domain.RoomType;

import java.time.Instant;


public record RoomResponse(
        Long id,
        String name,
        RoomType type,
        Integer capacity,
        RoomStatus status,
        Instant createdAt
) {}
