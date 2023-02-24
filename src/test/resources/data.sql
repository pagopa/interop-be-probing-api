CREATE TABLE eservices (
id bigint PRIMARY KEY,
base_path VARCHAR(255) ARRAY NOT NULL,
eservice_name varchar(255) NOT NULL,
eservice_type varchar(255) NOT NULL,
eservice_id uuid NOT NULL,
last_request timestamp with time zone NULL,
polling_end_time time with time zone NOT NULL,
polling_frequency int NOT NULL default 5,
polling_start_time time with time zone NOT NULL,
probing_enabled boolean NOT NULL default false,
producer_name varchar(255) NOT NULL,
response_received timestamp with time zone NULL,
state varchar(10) NOT NULL,
version_id uuid NOT NULL
);