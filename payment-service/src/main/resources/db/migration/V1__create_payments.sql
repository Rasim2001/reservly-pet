create table payments
(
    id bigserial primary key,
    booking_id bigint not null,
    status varchar(32) not null,
    total_price numeric not null,
    created_at timestamptz not null default now()
);
