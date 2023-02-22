CREATE TABLE eservices (
id bigint PRIMARY KEY,
base_path VARCHAR(255) ARRAY NOT NULL,
eservice_name varchar(255) NOT NULL,
eservice_type varchar(255) NOT NULL,
eservice_id uuid NOT NULL,
last_request timestamp NULL,
polling_end_time time NOT NULL,
polling_frequency int NOT NULL,
polling_start_time time NOT NULL,
probing_enabled boolean NOT NULL,
producer_name varchar(255) NOT NULL,
response_received timestamp NULL,
state varchar(10) NOT NULL,
version_id uuid NOT NULL
);