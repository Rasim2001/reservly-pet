package com.reservly.notification.event;

import com.reservly.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationListener {

    private final NotificationService notificationService;

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void onBookingCreated(final BookingCreatedEvent event) {

        notificationService.save(event);

        log.info("Kafka listener on booking created with bookingId : {}", event.bookingId());
    }
}
