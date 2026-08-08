CREATE TABLE computer (
    id_pc       BIGSERIAL PRIMARY KEY,
    proce       VARCHAR(255)     NOT NULL,
    ram         INTEGER          NOT NULL,
    hard_drive  INTEGER          NOT NULL,
    price       DOUBLE PRECISION NOT NULL,
    mac_address VARCHAR(255)     NOT NULL,
    CONSTRAINT uk_computer_mac_address UNIQUE (mac_address)
);
