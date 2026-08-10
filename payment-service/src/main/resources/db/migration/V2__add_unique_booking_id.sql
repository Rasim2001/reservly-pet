alter table payments
    add constraint uq_payments_booking_id unique (booking_id);
