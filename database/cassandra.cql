CREATE KEYSPACE IF NOT EXISTS sync
WITH replication = {'class': 'SimpleStrategy', 'replication_factor':2};

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

CREATE TABLE IF NOT EXISTS users (
user_id TEXT PRIMARY KEY, 
username TEXT,
credential TEXT,
role_list SET<TEXT>,
claims MAP<TEXT, TEXT>,
profile TEXT,
central_us BOOLEAN,
east_us BOOLEAN,);