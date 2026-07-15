package com.reservly.notification.service;

import com.reservly.notification.domain.NotificationEntity;
import com.reservly.notification.event.BookingCreatedEvent;
import com.reservly.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    @Transactional
    public void save(BookingCreatedEvent event) {
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setBookingId(event.bookingId());
        notificationEntity.setUserId(event.userId());
        notificationEntity.setCreatedAt(Instant.now());
        notificationEntity.setMessage("Booking %d created for user %d".formatted(event.bookingId(), event.userId()));

        repository.save(notificationEntity);
    }
}
