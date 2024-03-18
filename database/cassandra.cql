CREATE KEYSPACE sync
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

CREATE TABLE user_data (
userName TEXT PRIMARY KEY,
credential TEXT,
roleList LIST<TEXT>,
claims MAP<TEXT, TEXT>,
profile TEXT
);

CREATE TABLE user_data (
    user_id TEXT PRIMARY KEY,
    user_name TEXT,
);