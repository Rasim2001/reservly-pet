package com.reservly.booking.service;

import com.reservly.booking.domain.booking.BookingEntity;
import com.reservly.booking.domain.booking.BookingStatus;
import com.reservly.booking.external.PaymentHttpClient;
import com.reservly.booking.external.PaymentResponse;
import com.reservly.booking.external.PaymentStatus;
import com.reservly.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PendingBookingReconciler {

    private final BookingRepository bookingRepository;
    private final PaymentHttpClient paymentHttpClient;
    private final BookingService bookingService;

    @Value("${app.reconciler.threshold-minutes}")
    private Integer thresholdMinutes;

    @Scheduled(fixedDelayString = "${app.reconciler.interval-ms}")
    public void reconcile() {
        Instant threshold = Instant.now().minus(Duration.ofMinutes(thresholdMinutes));

        List<BookingEntity> bookingEntityList = bookingRepository.findAllByStatusAndCreatedAtBefore(
                BookingStatus.PENDING,
                threshold,
                PageRequest.of(0, 50));

        if (bookingEntityList.isEmpty())
            return;

        for (BookingEntity bookingEntity : bookingEntityList) {
            try {
                PaymentResponse payment = paymentHttpClient.getPayment(bookingEntity.getId());

                if (payment.status() == PaymentStatus.SUCCESS) {
                    bookingService.confirm(bookingEntity.getId());
                    log.info("Reconcile: Confirm booking with id = {}", bookingEntity.getId());
                } else if (payment.status() == PaymentStatus.FAILED) {
                    bookingService.markPaymentFailed(bookingEntity.getId());
                    log.info("Reconcile: Payment failed with booking id = {}", bookingEntity.getId());
                }

            } catch (HttpClientErrorException.NotFound exception) {
                bookingService.markPaymentFailed(bookingEntity.getId());

                log.info("Reconcile throw HttpClientErrorException : {}", exception.getMessage());
            } catch (RestClientException exception) {
                log.info("Reconcile throw RestClientException : {}", exception.getMessage());
            }

        }
    }
}
