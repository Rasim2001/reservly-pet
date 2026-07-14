create table notifications(
    id bigserial primary key,
    booking_id bigint not null,
    user_id bigint not null,
    message varchar(255) not null,
    created_at timestamptz not null default now()
);
