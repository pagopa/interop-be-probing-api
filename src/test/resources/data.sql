CREATE TABLE eservices (
id bigint PRIMARY KEY,
base_path VARCHAR(255) ARRAY NULL,
eservice_name varchar(255) NULL,
eservice_type varchar(255) NULL,
eservice_id uuid NULL,
last_request timestamp NULL,
polling_end_time time NULL,
polling_frequency int NULL,
polling_start_time time NULL,
probing_enabled boolean NULL,
producer_name varchar(255) NULL,
response_received timestamp NULL,
state varchar(10) NULL,
version_id uuid NULL
);