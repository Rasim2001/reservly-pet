create table refresh_tokens
(
    id bigserial primary key,
    token varchar(255) unique not null,
    user_id bigint not null references users(id),
    expires_at timestamptz not null,
    created_at timestamptz not null default now()
);
