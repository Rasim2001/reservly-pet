package com.reservly.booking.domain;

import com.reservly.booking.dto.CreateRoomRequest;
import com.reservly.booking.dto.RoomResponse;
import com.reservly.booking.dto.UpdateRoomRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoomMapper {
    RoomEntity toEntity(CreateRoomRequest createRoomRequest);
    RoomEntity toEntity(UpdateRoomRequest updateRoomRequest);

    RoomResponse toResponse(RoomEntity entity);
}
