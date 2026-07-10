package com.reservly.booking.domain.booking;

import com.reservly.booking.dto.booking.BookingResponse;
import com.reservly.booking.dto.booking.CreateBookingRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {
    BookingEntity toEntity(CreateBookingRequest bookingRequest);

    @Mapping(source = "room.id", target = "roomId")
    BookingResponse toResponse(BookingEntity entity);
}
