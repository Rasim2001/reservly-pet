package com.reservly.paymentservice.api;

import com.reservly.paymentservice.dto.PaymentRequest;
import com.reservly.paymentservice.dto.PaymentResponse;
import com.reservly.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("api/payments")
@RestController
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponse create(@RequestBody @Valid PaymentRequest request) {
        return paymentService.create(request);
    }

    @PostMapping("/{bookingId}/refund")
    public PaymentResponse refund(@PathVariable Long bookingId) {
        return paymentService.refund(bookingId);
    }

    @GetMapping("/{bookingId}")
    public PaymentResponse getByBookingId(@PathVariable Long bookingId) {
        return paymentService.getByBookingId(bookingId);
    }
}
