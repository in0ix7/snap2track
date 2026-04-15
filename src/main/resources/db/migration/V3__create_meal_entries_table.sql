CREATE TABLE IF NOT EXISTS meal_entries (
    id BIGSERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    meal_type VARCHAR(20) NOT NULL CHECK (meal_type IN ('BREAKFAST', 'LUNCH', 'DINNER', 'SNACK')),
    eaten_at TIMESTAMP NOT NULL,
    photo_url TEXT,
    analysis_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_meal_entries_client_id ON meal_entries(client_id);
CREATE INDEX idx_meal_entries_eaten_at ON meal_entries(eaten_at);