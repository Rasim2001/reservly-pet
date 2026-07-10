package com.reservly.booking.domain.room;

import com.reservly.booking.dto.room.CreateRoomRequest;
import com.reservly.booking.dto.room.RoomResponse;
import com.reservly.booking.dto.room.UpdateRoomRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoomMapper {
    RoomEntity toEntity(CreateRoomRequest createRoomRequest);

    RoomResponse toResponse(RoomEntity entity);
}
