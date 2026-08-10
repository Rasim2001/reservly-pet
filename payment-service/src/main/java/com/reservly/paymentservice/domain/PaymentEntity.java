package com.reservly.paymentservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "payments")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "total_price", precision = 19, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
