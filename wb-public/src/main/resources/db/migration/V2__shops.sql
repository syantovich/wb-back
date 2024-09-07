-- Создание таблицы shops
CREATE TABLE shops
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    logo_url    VARCHAR(255),
    description TEXT,
    CONSTRAINT unique_shop_name UNIQUE (name)
);

-- Создание таблицы shop_addresses
CREATE TABLE shop_addresses
(
    id               UUID PRIMARY KEY,
    latitude         VARCHAR(255),
    longitude        VARCHAR(255),
    city             VARCHAR(255),
    street           VARCHAR(255),
    house            VARCHAR(255),
    building         VARCHAR(255),
    apartment        VARCHAR(255),
    entrance         VARCHAR(255),
    floor            VARCHAR(255),
    room             VARCHAR(255),
    is_pickup_point  BOOLEAN,
    shop_id          UUID,
    CONSTRAINT shop_id_fk FOREIGN KEY (shop_id) REFERENCES shops (id) ON DELETE CASCADE
);

-- Создание таблицы work_schedules
CREATE TABLE shop_work_schedules
(
    id         UUID PRIMARY KEY,
    monday     VARCHAR(255),
    tuesday    VARCHAR(255),
    wednesday  VARCHAR(255),
    thursday   VARCHAR(255),
    friday     VARCHAR(255),
    saturday   VARCHAR(255),
    sunday     VARCHAR(255),
    address_id UUID,
    CONSTRAINT address_id_fk FOREIGN KEY (address_id) REFERENCES shop_addresses (id) ON DELETE CASCADE
);

-- Создание таблицы shop_contacts
CREATE TABLE shop_contacts
(
    id      UUID PRIMARY KEY,
    type    VARCHAR(255) NOT NULL,
    value   VARCHAR(255) NOT NULL,
    shop_id UUID,
    CONSTRAINT shop_id_fk FOREIGN KEY (shop_id) REFERENCES shops (id) ON DELETE CASCADE
);

-- Индексы
CREATE INDEX idx_shop_name ON shops (name);