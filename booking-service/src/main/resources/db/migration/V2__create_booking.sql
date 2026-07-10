create table bookings
(
    id         bigserial primary key,
    room_id    bigint      not null references rooms (id),
    user_id    bigint      not null,
    start_time timestamptz not null,
    end_time   timestamptz not null,
    status     varchar(32) not null,
    created_at timestamptz not null default now(),
    check ( start_time < end_time )
);

create index idx_booking_room_time on bookings (room_id, start_time, end_time);

