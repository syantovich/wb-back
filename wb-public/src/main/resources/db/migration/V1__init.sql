create table persons
(
    id          uuid primary key,
    name        varchar(255) not null,
    email       varchar(255) not null,
    password    varchar(255) not null,
    is_verified boolean      not null default false,
    created_at  timestamp    not null default current_timestamp,
    updated_at  timestamp    not null default current_timestamp,

    constraint name_uniq unique (email)
);

create table authorities
(
    id        uuid primary key,
    name      varchar(255),
    person_id uuid,

    constraint person_id_fk foreign key (person_id) references persons (id) on DELETE cascade
);

create table refresh_tokens
(
    id          uuid primary key,
    token       varchar(255) not null,
    person_id   uuid,
    expiry_date timestamp    not null,
    constraint person_id_fk foreign key (person_id) references persons (id) on DELETE cascade
);

create table access_tokens
(
    id               uuid primary key,
    token            varchar(255) not null,
    expiry_date      timestamp    not null,
    refresh_token_id uuid,
    constraint refresh_token_id_fk foreign key (refresh_token_id) references refresh_tokens (id) on DELETE cascade
);

create table verification_tokens
(
    id          uuid primary key,
    code        varchar(255) not null,
    person_id   uuid,
    expiry_date timestamp    not null,
    created_at  timestamp    not null default current_timestamp,
    constraint person_id_fk foreign key (person_id) references persons (id) on DELETE cascade
);
