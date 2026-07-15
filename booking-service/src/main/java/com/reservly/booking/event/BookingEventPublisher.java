package com.reservly.booking.event;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class BookingEventPublisher {

    @Value("${app.kafka.topic}")
    private String topic;

    private final KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publish(BookingCreatedEvent event) {
        kafkaTemplate.send(topic, event.bookingId().toString(), event);
    }
}
