package com.reservly.booking.service;

import com.reservly.booking.dto.booking.BookingResponse;
import com.reservly.booking.dto.booking.CreateBookingRequest;
import com.reservly.booking.external.PaymentHttpClient;
import com.reservly.booking.external.PaymentRequest;
import com.reservly.booking.external.PaymentResponse;
import com.reservly.booking.external.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingOrchestrator {

    private static final BigDecimal FIXED_AMOUNT = BigDecimal.valueOf(10000);

    private final BookingService bookingService;
    private final PaymentHttpClient paymentHttpClient;

    public BookingResponse createAndPay(CreateBookingRequest request) {

        BookingResponse booking = bookingService.create(request);

        try {
            PaymentResponse payment = paymentHttpClient.createPayment(new PaymentRequest(booking.id(), FIXED_AMOUNT));

            return payment.status() == PaymentStatus.SUCCESS
                    ? bookingService.confirm(booking.id())
                    : bookingService.markPaymentFailed(booking.id());

        } catch (HttpClientErrorException e) {

            log.info("Payment rejected for booking {}: {}", booking.id(), e.getStatusCode());

            return bookingService.markPaymentFailed(booking.id());
        }catch (RestClientException e) {
            log.info("Payment status UNKNOWN for booking {} — оставляем PENDING", booking.id(), e);
            return booking;
        }
    }
}
