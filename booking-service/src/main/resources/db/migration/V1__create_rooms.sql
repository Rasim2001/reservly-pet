create table rooms(
    id bigserial primary key,
    name varchar(255) not null unique,
    type varchar(32) not null,
    capacity int not null check ( capacity > 0 ),
    status varchar(32) not null,
    created_at timestamptz not null default now()
);
