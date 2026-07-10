package com.reservly.booking.dto.room;

import com.reservly.booking.domain.room.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateRoomRequest (
        @NotBlank @Size(max = 255) String name,
        @NotNull RoomType type,
        @Positive(message = "must be greater than 0") Integer capacity
){}
