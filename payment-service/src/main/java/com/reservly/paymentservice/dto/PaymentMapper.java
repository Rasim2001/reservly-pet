package com.reservly.paymentservice.dto;

import com.reservly.paymentservice.domain.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {
    PaymentEntity toEntity(PaymentRequest request);

    @Mapping(target = "status", source = "paymentStatus")
    PaymentResponse toResponse(PaymentEntity entity);
}
