package com.reservly.booking.service;

import com.reservly.booking.domain.booking.BookingEntity;
import com.reservly.booking.domain.booking.BookingStatus;
import com.reservly.booking.external.PaymentHttpClient;
import com.reservly.booking.external.PaymentResponse;
import com.reservly.booking.external.PaymentStatus;
import com.reservly.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefundBookingReconciler {

    private final BookingRepository bookingRepository;
    private final PaymentHttpClient paymentHttpClient;
    private final BookingService bookingService;

    @Scheduled(fixedDelayString = "${app.reconciler.interval-ms}")
    public void reconcile() {

        List<BookingEntity> bookingEntityList = bookingRepository
                .findAllByStatus(BookingStatus.CANCEL_PENDING, PageRequest.of(0, 50));

        for (BookingEntity booking : bookingEntityList) {

            try {
                PaymentResponse payment = paymentHttpClient.getPayment(booking.getId());

                if (payment.status() == PaymentStatus.REFUNDED) {
                    bookingService.cancel(booking.getId());
                } else if (payment.status() == PaymentStatus.SUCCESS) {
                    paymentHttpClient.refund(booking.getId());
                    bookingService.cancel(booking.getId());
                } else {
                    bookingService.cancel(booking.getId());
                }

                log.info("Refund reconcile: booking {} cancelled, payment was {}", booking.getId(), payment.status());

            } catch (HttpClientErrorException.NotFound e) {
                bookingService.cancel(booking.getId());

            } catch (RestClientException e) {
                log.info("Refund reconcile failed for booking {}", booking.getId(), e);

            } catch (Exception e) {
                log.info("Refund reconcile: unexpected error for booking {}", booking.getId(), e);
            }
        }
    }
}
