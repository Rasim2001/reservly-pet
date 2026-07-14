package com.reservly.booking.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class BookingEventPublisher {

    private static final String TOPIC = "booking.created";

    private final KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publish(BookingCreatedEvent event) {
        kafkaTemplate.send(TOPIC, event.bookingId().toString(), event);
    }
}
