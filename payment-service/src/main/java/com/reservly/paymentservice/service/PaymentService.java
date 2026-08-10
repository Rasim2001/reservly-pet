package com.reservly.paymentservice.service;

import com.reservly.common.NotFoundException;
import com.reservly.paymentservice.domain.PaymentEntity;
import com.reservly.paymentservice.domain.PaymentStatus;
import com.reservly.paymentservice.dto.PaymentMapper;
import com.reservly.paymentservice.dto.PaymentRequest;
import com.reservly.paymentservice.dto.PaymentResponse;
import com.reservly.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final BigDecimal FIXED_LIMIT = BigDecimal.valueOf(1000);

    private final PaymentRepository repository;
    private final PaymentMapper mapper;

    @Transactional
    public PaymentResponse create(PaymentRequest request) {

        Optional<PaymentEntity> found = repository.findByBookingId(request.bookingId());

        if(found.isPresent()){
            log.info("Payment with booking_id = {} already exists", request.bookingId());

            return mapper.toResponse(found.get());
        }

        PaymentStatus paymentStatus = request.totalPrice().compareTo(FIXED_LIMIT) > 0
                ? PaymentStatus.FAILED
                : PaymentStatus.SUCCESS;

        PaymentEntity entity = mapper.toEntity(request);
        entity.setCreatedAt(Instant.now());
        entity.setPaymentStatus(paymentStatus);

        PaymentEntity saved = repository.save(entity);

        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PaymentResponse getByBookingId(Long bookingId) {
        return repository.findByBookingId(bookingId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException(
                        "Payment for booking id = %s not found".formatted(bookingId)));
    }
}
