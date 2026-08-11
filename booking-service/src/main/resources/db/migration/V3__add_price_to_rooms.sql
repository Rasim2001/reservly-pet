alter table rooms
    add column price_per_hour numeric(19, 2) not null default 100.00;

alter table rooms
    alter column price_per_hour drop default;