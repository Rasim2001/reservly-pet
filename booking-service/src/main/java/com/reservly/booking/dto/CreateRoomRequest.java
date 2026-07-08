package com.reservly.booking.dto;

import com.reservly.booking.domain.RoomType;
import jakarta.validation.constraints.*;


public record CreateRoomRequest(
        @NotBlank @Size(max = 255) String name,
        @NotNull RoomType type,
        @Positive Integer capacity
) {}
