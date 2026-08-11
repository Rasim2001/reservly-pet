alter table bookings
    add column total_price numeric(19, 2);

update bookings
set total_price = 0
where total_price is null;

alter table bookings
    alter column total_price set not null;