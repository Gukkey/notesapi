CREATE TABLE notes (
                       id SERIAL PRIMARY KEY,
                       created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       tag VARCHAR,
                       title VARCHAR,
                       body TEXT NOT NULL
)