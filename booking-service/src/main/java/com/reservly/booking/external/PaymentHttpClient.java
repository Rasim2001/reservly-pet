package com.reservly.booking.external;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(
        accept = "application/json",
        contentType = "application/json",
        url = "/api/payments"
)
public interface PaymentHttpClient {

    @PostExchange
    PaymentResponse createPayment(@RequestBody PaymentRequest request);

    @PostExchange("{bookingId}/refund")
    PaymentResponse refund(@PathVariable Long bookingId);

}
